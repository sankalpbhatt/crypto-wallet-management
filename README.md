# crypto-wallet-management

## Overview

The Crypto Wallet Management System is a comprehensive solution for managing cryptocurrency wallets, transactions, and
real-time price fetching. This system allows users to create, retrieve, update, and delete wallets, handle
cryptocurrency transactions, and fetch real-time prices for supported cryptocurrencies. It also includes basic
encryption for private keys to ensure secure storage.

## Features

- **User Wallet Management**:
    - Create, retrieve, update, and delete user wallets.
    - Support for multiple cryptocurrencies (e.g., Bitcoin, Ethereum).

- **Transaction Handling**:
    - Send and receive crypto assets.
    - Simulate blockchain confirmations and transaction statuses.
    - Transaction history with timestamps, amounts, and transaction IDs.

- **Real-Time Price Fetching**:
    - Integrated with public APIs to fetch real-time cryptocurrency prices.
    - Calculate and display the total value of a user's wallet in USD.

- **Security Measures**:
    - Secure API endpoints with authentication and authorization using JWT.

## Technologies Used

- **Languages**: Java
- **Database**: PostgreSQL
- **Spring Boot**: For building the RESTful APIs
- **JPA/Hibernate**: For ORM and database interactions
- **Spring Security**: For securing API endpoints

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

## Issues

For raising any issues please use the issue
section [here](https://github.com/sankalpbhatt/crypto-wallet-management/issues)
