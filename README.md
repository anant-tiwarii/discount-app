# Project Setup

Follow these steps to set up, build, and run the discount application locally using gradle.

---

## Prerequisites

### Java Environment

This project requires **Java 22**. Maybe **Eclipse Temurin 22.0.2**. Ensure your `JAVA_HOME` is set correctly before proceeding.
If you are using IntelliJ, then you download/set it in 'Project Structure'

### Database

PostgreSQL installed locally or a valid connection string for a cloud or vm postgress(password based). If you do not have it, you can download it from the official site:
[https://www.postgresql.org/download/](https://www.postgresql.org/download/)

---

## Installation and Build

1. Clone the repository to your local machine.
2. Open a terminal in the project root directory.
3. Clean the previous build files:
```bash
./gradlew clean

```


4. Build the distribution:
```bash
./gradlew installDist

```
If you are using IntelliJ, on successfully cloning the repository, an action will pop up to 'Load Gradle Project'. Then you can use the gradle terminal to do above actions easily.


---

## Environment Configuration

Set the following environment variables on your system or within your IDE run configuration to allow the application to connect to your local/cloud database.


If using IntelliJ, you can set these in 'Modify Run Configurations'

Below is an example:
```text
PSG_URL=jdbc:postgresql://localhost:5432/<database-name>
PSG_USER=postgres
PSG_PASSWORD=postgres
HIKARI_POOL_SIZE=10

```
---

## Database Schema Setup

Execute the following queries in pgAdmin or your PostgreSQL shell to create the required tables and initial data.

### Products Table

```sql
CREATE TABLE products (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    base_price NUMERIC(10, 2) NOT NULL CHECK (base_price > 0),
    country TEXT NOT NULL
);

CREATE INDEX idx_products_country ON products(country);

```

### Product Discounts Table

```sql
CREATE TABLE product_discounts (
    product_id UUID NOT NULL,
    discount_id TEXT NOT NULL,
    percent NUMERIC(5, 2) NOT NULL CHECK (percent > 0 AND percent < 100),

    CONSTRAINT pk_product_discount PRIMARY KEY (product_id, discount_id),
    CONSTRAINT fk_product FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE CASCADE
);

```

### Country VAT Table

```sql
CREATE TABLE country_vat (
    country TEXT PRIMARY KEY,
    vat_percent NUMERIC(5, 2) NOT NULL CHECK (vat_percent >= 0 AND vat_percent <= 100),
    updated_at TIMESTAMP DEFAULT NOW()
);

INSERT INTO country_vat (country, vat_percent) VALUES
  ('Sweden', 25.00),
  ('Germany', 19.00),
  ('France', 20.00)
ON CONFLICT (country) DO UPDATE SET vat_percent = EXCLUDED.vat_percent;

```

---

## Running the Application

After configuring the database and environment variables, you can start the service. By default, the service starts on port **8080**. You can change this in ```resources/application.conf```

### From Command Line

Run the generated executable:

```bash
./app/build/install/app/bin/app

```

### From IDE

Alternatively, run the `Application.kt` file directly from your IDE.

---

## cURLs

### Get Products by Country

```bash
curl --location 'http://localhost:8082/api/v1/products?country=<country>' \
--header 'Content-Type: application/json'

```

### Apply Discount to Product

```bash
curl --location --request PUT 'http://localhost:8082/api/v1/products/<product-id>/discount' \
--header 'Content-Type: application/json' \
--data '{
    "discountId": "check10",
    "percent": 10
}'

```

---

## Tests

The integration tests use **Testcontainers** to spin up a PostgreSQL instance in Docker. Ensure that **Docker Desktop** is running on your machine before starting the tests.

Execute tests using Gradle:

```bash
./gradlew test

```