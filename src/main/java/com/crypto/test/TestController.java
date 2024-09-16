package com.crypto.test;

import com.crypto.util.CryptoUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.PrivateKey;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/encrypt")
    public String encrypt(@RequestParam String str) {
        return CryptoUtils.encryptPrivateKey(CryptoUtils.getPrivateKeyFromString(str), "Password");
    }


    @GetMapping("/decrypt")
    public PrivateKey decrypt(@RequestParam String str) {
        return CryptoUtils.decryptPrivateKey(str, "Password");
    }
}
