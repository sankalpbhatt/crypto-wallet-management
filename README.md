# crypto-wallet-management

## Overview

The Crypto Wallet Management System is a comprehensive solution for managing cryptocurrency wallets, transactions, and
real-time price fetching. This system allows users to create, retrieve, update, and delete wallets, handle
cryptocurrency transactions, and fetch real-time prices for supported cryptocurrencies. It also includes basic
encryption for private keys to ensure secure storage.

## Features

## Encryption and Authentication

### Ensuring Encryption in Transaction Authorization

In our system, transaction authorization is secured through encryption based on the private key saved in the `Wallet`
entity. Here's how we ensure that transactions are protected:

1. **Private Key Encryption:** When a wallet is created, key pair is generated and public key is saved as is while the
   private key is encrypted before being stored in the database. This ensures that sensitive information is protected
   from unauthorized access.

2. **Transaction Authorization:** To authorize a transaction, the system uses the encrypted private key associated with
   the wallet. The transaction request is processed by decrypting the private key only at the moment of validation. This
   decrypted private key then validates the signature attached to each transaction. A transaction is only permitted if
   the signature is validated

### Request Authentication with Filters

To enhance the security of our API, we have implemented a filter-based authentication mechanism for each request using
JWT:

1. **Filter Implementation:** We use filters to intercept incoming requests and perform authentication checks. This
   ensures that only authorized requests are processed further.

2. **Authentication Process:** Each request must pass through the filter, which verifies the request's credentials or
   tokens. Unauthorized requests are rejected before reaching the core logic of the application.

3. **Filter Configuration:** The filter is configured to bypass certain endpoints. Configuration is currenly hardcoded
   in the filter but can be extracted in the property file. Currently, all the swagger related endpoints are whitelisted
   using `doNotFilter` method


- **User Wallet Management**:
    - Create, retrieve and update user wallets.
    - Support for multiple cryptocurrencies (e.g., Bitcoin, Ethereum).

- **Transaction Handling**:
    - Send and receive crypto assets.
    - Simulate blockchain confirmations and transaction statuses (this is done using ExecutorService adding additional
      delay).
    - Transaction history with timestamps, amounts, and transaction IDs(External reference to store the transaction
      reference of external crypto providers).

- **Real-Time Price Fetching**:
    - Integrated with public APIs to fetch real-time cryptocurrency prices.
    - Calculate and display the total value of a user's wallet in USD.

## Technologies Used

- **Languages**: Java
- **Database**: PostgreSQL
- **Spring Boot**: For building the RESTful APIs
- **JPA/Hibernate**: For ORM and database interactions

## Setup Instructions

### Prerequisites

- JDK 11 or higher
- Gradle
- PostgreSQL
- Docker installed on your machine
- Docker Compose installed on your machine

### PostgreSQL Docker Setup

This guide will help you set up a PostgreSQL database using Docker Compose, and then create a user and a database using
the `psql` terminal.

#### Create and Start the PostgreSQL Container

To create and start the PostgreSQL container, run the following command:

### Clone the Repository

```shell
git clone https://github.com/sankalpbhatt/crypto-wallet-management.git
```

Open terminal and navigate to service folder

```shell
cd crypto-wallet-management
```

```sh
docker-compose -f ./docker-compose.yml up -d
```

## Clone and Build the application

Build and package application

```shell
./gradlew clean build
```

## Run the application

Build and package application

```shell
./gradlew bootRun
```

## Access

Use the below-mentioned base path to invoke various endpoints
[http://localhost:8081/crypto/](http://localhost:8081/crypto/)

## API Documents

Click on below link to access Swagger API Documentation.
[http://localhost:8081/crypto/swagger-ui/index.html#/](http://localhost:8081/crypto/swagger-ui/index.html#/)

Above API document will provide the curls to be used to test the application. This Swagger will not be able to
directly call the APIs as it is not yet configured to generate and pass authorization token. This can manually be
fetched from the key `exampleJwt` in application.yml.

NOTE: This repo has currently hardcoded the keys in yml but for security reasons it is best to store in some kind of key
vault. For e.g. AWS Secret Manager.

## Issues

For raising any issues please use the issue
section [here](https://github.com/sankalpbhatt/crypto-wallet-management/issues)
