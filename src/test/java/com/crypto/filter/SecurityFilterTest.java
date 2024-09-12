package com.crypto.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SecurityFilterTest {

    @InjectMocks
    private SecurityFilter securityFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(securityFilter, "pubKey", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxuZuUaE+NkQM3L5JReCxmsU7iXkxntG4yU+RsD4GCqkZf/8lXskOmSuQx4f4nDZrOttfnQ5mbpT2mnA3/rMILQ77a+YaBu8mYV8KgIVLs70rsAr3m58RElYmDp3RGXGoX3xtouhHa8YGMWYqiNgAn/0j1+NnC8fg7Ko+so+LpPRj6a5beZANgzAV2vlUCowEA0WC9EuFqL09dZZYa19Ki+xYrMrXRQCe1Y2xvn4K5sQ8tt1ZnyaDIeAor6rdTMrMaOFQbHkW1lyd04JWCuoSTTR/JX/Vy5NCWMy/+tEwNMHcPp+Hlqxye4CVTDg0JnT2QsJ0rqx+sajG5xT32I0QnwIDAQAB"); // Replace with actual public key
    }

    @Test
    void shouldThrowExceptionWhenAuthorizationHeaderIsMissing() {
        when(request.getHeader("Authorization")).thenReturn("");

        assertThatThrownBy(() -> securityFilter.doFilterInternal(request, response, filterChain))
                .isInstanceOf(MyServiceException.class)
                .hasMessage("Permission Issue : Authorization header missing")
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PERMISSION);
    }

    @Test
    void shouldThrowExceptionWhenJWTTokenIsEmpty() {
        when(request.getHeader("Authorization")).thenReturn("Bearer ");

        assertThatThrownBy(() -> securityFilter.doFilterInternal(request, response, filterChain))
                .isInstanceOf(MyServiceException.class)
                .hasMessage("Permission Issue : Authorization jwtToken")
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PERMISSION);
    }

    @Test
    void shouldPassFilterWhenJWTTokenIsValid() throws ServletException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJjbGllbnRJZCI6InlvdXJfY2xpZW50X2lkIn0.bPuQxUr8eICFhU2cxbB2FLKzFwyy6cwbhcwPiUVEa8D5Al8_N2nIB1STgIIAbnPDaVULSw44ZpewfQc_z9J3eM8UiCA_gvHpaFHB6IDv41yOL6z-NhNRJU6vry-oM8Q5e7_rkpvLyeCvpQsbsZNCVaz8szlHyPNunYMxkVZNW03XHMWok2Zn4QwXXqkC4nBop3MkZ3AL8oi8GFwsieGu81a0U2DYgyXQyfN4oTG0NbOEXGlLrwI8vElGEPb71d1mjWFuio0UestLgCzir0o39gxdEQWaI7OCTDQwyjYIEOd5l1HQE3qoI_JAKX1Z0892Gk_JC9urUU8glP1bxCp4PA";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        byte[] publicKeyBytes = Base64.getDecoder().decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxuZuUaE+NkQM3L5JReCxmsU7iXkxntG4yU+RsD4GCqkZf/8lXskOmSuQx4f4nDZrOttfnQ5mbpT2mnA3/rMILQ77a+YaBu8mYV8KgIVLs70rsAr3m58RElYmDp3RGXGoX3xtouhHa8YGMWYqiNgAn/0j1+NnC8fg7Ko+so+LpPRj6a5beZANgzAV2vlUCowEA0WC9EuFqL09dZZYa19Ki+xYrMrXRQCe1Y2xvn4K5sQ8tt1ZnyaDIeAor6rdTMrMaOFQbHkW1lyd04JWCuoSTTR/JX/Vy5NCWMy/+tEwNMHcPp+Hlqxye4CVTDg0JnT2QsJ0rqx+sajG5xT32I0QnwIDAQAB"); // Replace with actual base64 encoded public key
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        JWT.require(algorithm).build().verify(jwtToken);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotFilterSwaggerPaths() {
        when(request.getRequestURI()).thenReturn("/swagger-ui/index.html");
        when(request.getContextPath()).thenReturn("");

        boolean result = securityFilter.shouldNotFilter(request);

        assertThat(result).isTrue();
    }
}
