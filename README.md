# Desafio Técnico — API de Cupons

## 1. Descrição
API REST para gerenciamento de cupons de desconto usando Java 17, Spring Boot 3 e Arquitetura Hexagonal (Ports & Adapters), com regras de negócio encapsuladas no domínio.

## 2. Tecnologias utilizadas
- Java 17
- Spring Boot 3
- Maven
- Spring Web
- Spring Data JPA + Hibernate
- H2 (em memória)
- Springdoc OpenAPI (Swagger)
- Lombok
- MapStruct
- JUnit 5 + Mockito
- Docker + Docker Compose

## 3. Arquitetura
Estrutura baseada em Hexagonal Architecture:

```text
            +---------------------------+
            |      Adapter In (Web)     |
            |   CouponController/DTOs   |
            +------------+--------------+
                         |
                         v
+------------------------+-------------------------+
|                Application Services              |
|     CreateCouponService / DeleteCouponService    |
+------------------------+-------------------------+
                         |
                         v
+------------------------+-------------------------+
|            Domain (Model + Ports)                |
| Coupon + Exceptions + Port In/Out                |
+------------------------+-------------------------+
                         |
                         v
            +------------+--------------+
            |   Adapter Out (Persistence)|
            |  JPA Entity/Repository     |
            +----------------------------+
```

## 4. Como executar
### Maven
```bash
mvn spring-boot:run
```

### Docker
```bash
mvn clean package -DskipTests
docker compose up --build
```

## 5. Endpoints disponíveis
| Método | Path | Descrição |
|---|---|---|
| POST | `/api/v1/coupons` | Cria cupom de desconto |
| DELETE | `/api/v1/coupons/{id}` | Realiza soft delete de cupom |

## 6. Regras de negócio
- `code`, `description`, `discountValue`, `expirationDate` são obrigatórios.
- `code` é sanitizado removendo caracteres especiais.
- Após sanitização, `code` deve ser alfanumérico com exatamente 6 caracteres.
- `discountValue` deve ser `>= 0.5`.
- `expirationDate` não pode estar no passado.
- `published` pode ser informado na criação (default `false`).
- DELETE é soft delete (`deletedAt = now`).
- Não pode deletar cupom já deletado (409).
- Cupom inexistente retorna 404.

## 7. Testes
Rodar todos os testes:
```bash
mvn clean test
```

Rodar verificação de cobertura (mínimo esperado 80%):
```bash
mvn clean verify
```

## 8. Swagger
- OpenAPI JSON: `http://localhost:8080/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
