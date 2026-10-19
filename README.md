# Bank Transaction API

A design-first Spring Boot REST API based on the supplied class practical. It does **not** move real money or call external services. It persists transactions in an embedded H2 database.

## Requirements
- Java 21+
- Maven 3.9+

## Run
```bash
mvn spring-boot:run
```
The API starts at `http://localhost:8080`. H2 console: `http://localhost:8080/h2-console` with JDBC URL `jdbc:h2:file:./data/bankdb`, username `sa`, blank password.

## Sample accounts
- Retail customer Ada: `1000000001` and `1000000002`
- Business customer Bola: `2000000001`
- Long-tenure retail customer Chidi: `3000000001`

Demo records are created automatically only when the customer table is empty.

## Endpoints
### Transfer
`POST /api/v1/transactions/transfer`
```json
{"sourceAccount":"1000000001","destinationAccount":"2000000001","amount":60000}
```
Returns the saved transaction with original amount, discount amount, and charged amount. The source customer's transaction count determines eligibility.

### Airtime
`POST /api/v1/transactions/airtime`
```json
{"sourceAccount":"1000000001","networkProvider":"MTN","amount":5000,"phoneNumber":"08012345678"}
```
Airtime is never discounted.

### Transaction history
`GET /api/v1/accounts/1000000001/transactions`
Returns transactions where the account is either source or destination.

Every request under `/api/` gets an audit log containing HTTP method, path, response status, and timestamp, including failed API requests. Audit records are persisted in `audit_logs`.

## Decisions for the five ambiguities
1. **“After three transactions”** means the discount applies starting with the fourth monthly transaction. The service checks the count before saving the current transaction, so `monthlyCount >= 3` qualifies.
2. **Monthly count** is per customer, not per account. A customer with multiple accounts shares one monthly count. This practical's counter counts transactions initiated from the customer's accounts.
3. **Airtime count**: airtime purchases count toward the monthly count because all saved transactions initiated by the customer are included; however, airtime itself is never discounted.
4. **Overlapping discounts**: discounts do not stack. The calculator selects the highest applicable percentage. The long-tenure rule may be evaluated alongside customer-type rules.
5. **Stored amounts**: store original amount, discount amount, and charged amount, so the calculation is auditable and the response explains what happened.

## Discount rules
- Business: 27% when amount is strictly greater than 150,000 and at least three prior monthly transactions exist.
- Retail: 18% when amount is strictly greater than 50,000 and at least three prior monthly transactions exist.
- Customer tenure over four years: 10% for the first three monthly transactions.
- Airtime: 0% always.
- Thresholds are strict (`>`), not inclusive (`>=`).

The rule abstraction (`DiscountRule`) follows Open/Closed: add a new implementation and Spring will inject it into `DiscountCalculator`, without modifying existing rule classes. Controllers handle HTTP; the service handles transaction use cases; repositories are interfaces. `AuditInterceptor` centralizes audit writes.

## Tests
```bash
mvn test
```
Tests cover the discount rules, threshold behaviour, tenure rule and best-discount-wins policy.

## Design files
`docs/use-case-diagram.puml` and `docs/class-diagram.puml` are editable PlantUML sources; PNG and PDF exports are also included.

## Scope notes
This is a class-practical implementation, not production banking software. It has no authentication/authorization, no real balance validation or money movement, and no idempotency protection. Audit storage failure handling and concurrency-safe monthly counting would need additional work for production.
