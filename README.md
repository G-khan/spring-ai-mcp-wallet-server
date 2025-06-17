# Finance Wallet MCP Server

Article of the project: https://gokhana.medium.com/from-rest-api-to-mcp-server-convert-your-spring-apis-into-ai-tools-with-spring-ai-07b8f36b0212

This project is a simple wallet management backend that exposes REST APIs and integrates with Spring AI's MCP (Model Context Protocol) server for AI-powered tool access.

---

### Spring AI Integration 
- Integrated Spring AI MCP Server starter
- Annotated service methods with `@Tool` for AI tool exposure
- Added configuration for MCP server operation

---

## Key Dependencies

```xml
<properties>
    <java.version>17</java.version>
    <spring-ai.version>1.0.0</spring-ai.version>
</properties>

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server</artifactId>
</dependency>
```

---

## Features
- Wallet Management (Create, Read, Update, Delete)
- Add income and expenses to wallets
- Automatic balance updates
- Input validation and error handling
- H2 in-memory database for easy testing

---

## Technical Stack
- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- H2 Database (in-memory)
- Maven
- Lombok
- Spring AI MCP Server

---

## Data Model

| Field   | Type        | Description         |
|---------|-------------|---------------------|
| id      | Long        | Wallet ID           |
| name    | String      | Wallet name         |
| balance | BigDecimal  | Current balance     |

---

## API Endpoints
- `GET /api/wallets` - Get all wallets
- `GET /api/wallets/{id}` - Get wallet by ID
- `POST /api/wallets` - Create a new wallet
- `PUT /api/wallets/{id}` - Update a wallet
- `DELETE /api/wallets/{id}` - Delete a wallet
- `POST /api/wallets/{id}/expense?amount={amount}` - Add an expense
- `POST /api/wallets/{id}/income?amount={amount}` - Add income

---

## Tool Annotations

The application uses Spring AI Tool annotations to expose service methods as MCP tools.

Example:
```java
@Tool(name = "create-wallet", description = "Creates a new wallet with the given details")
public Wallet createWallet(Wallet wallet) { ... }
```

**Exposed Tools:**
- create-wallet
- get-all-wallets
- get-wallet-by-id
- update-wallet
- delete-wallet
- add-expense
- add-income

---

## MCP Server Configuration

Add the following to your `src/main/resources/application.properties`:
```properties
spring.main.web-application-type=none
spring.ai.mcp.server.name=wallet-mcp
spring.ai.mcp.server.version=0.0.1
server.port=8091
```

Register the tool callback in your configuration:
```java
@Bean
public List<ToolCallback> walletTools(WalletService walletService) {
    return List.of(ToolCallbacks.from(walletService));
}
```

---

## H2 Console
- URL: http://localhost:8091/h2-console
- JDBC URL: `jdbc:h2:mem:walletdb`
- Username: `sa`
- Password: (empty)

---

## Error Handling
- 404 for wallet not found
- 400 for insufficient balance or illegal arguments
- Centralized in `GlobalExceptionHandler`

---

## Running the Application

1. Clone the repository
2. Build with `mvn clean package`
3. Start the MCP server:
   ```bash
   java -jar target/finance-wallet-0.0.1-SNAPSHOT.jar
   ```
   Or use `.cursor/mcp.json`:
   ```json
   {
     "mcpServers": {
       "finance-wallet-mcp": {
         "command": "java",
         "args": [
           "-jar",
           "/Users/gokhanayrancioglu/finance-wallet/target/finance-wallet-0.0.1-SNAPSHOT.jar"
         ]
       }
     }
   }
   ```
