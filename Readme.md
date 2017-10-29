
### How to build

Execute
```
./mvnw clean package
```
to build application. During build integration tests will
execute. Integration tests use H2 database by default.

Application expects PostgreSQL database to be available on
"production" environment.
Use spring boot parameters to provide database name and
credentials (see `application.properties` for details).

To start application and *create* database run:
```
java -jar target/checkout3-0.0.1-SNAPSHOT.jar --spring.jpa.hibernate.ddl-auto=create
```

Best way to test REST api is to use [Postman](https://www.getpostman.com/).
In integration test resources you may find example requests and responses.

### Pricing idea

There are two types of price discount rules:
* Quantity rule - price is smaller if you buy more
* Double sell rule - price is smaller if you buy two things

Pricing logic follows rules:
1. Execute quantity rules
2. Execute double sell rules

Point (2) may cause items to be split to so called pricing segments.
For example I have following rules:
1. Buy ITEM_A and ITEM_B and you get 10% discount on ITEM_A
2. Buy ITEM_A and ITEM_C and you get 20% discount on ITME_A

If we order 10 ITEM_A, 1 ITEM_C and 1 ITEM_B we get prices
(assuming that at the beginning all items costs 100$):
```
ITEM_C - 100$ x 1
ITEM_B - 100$ x 1
ITEM_A - 90$ x 1 (pair with ITEM_B)
ITEM_A - 80$ x 1 (pair with ITEM_C)
ITEM_A - 100$ x 8
```
