# Desafio Técnico — API de Cupons com Arquitetura Hexagonal

## Descrição do Projeto
API REST para o gerenciamento de cupons de desconto. O projeto segue os princípios de Domain-Driven Design (DDD) e Arquitetura Hexagonal, garantindo que as regras de negócio fiquem isoladas de infraestrutura e frameworks.

## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot 3.2.2**
- **Maven**
- **H2 Database** (em memória)
- **Spring Data JPA & Hibernate**
- **Springdoc OpenAPI (Swagger UI)**
- **Docker & Docker Compose**
- **JUnit 5 & Mockito**
- **Lombok**
- **MapStruct**

## Arquitetura
O projeto segue a **Arquitetura Hexagonal (Ports & Adapters)**, com foco em imutabilidade no núcleo através do uso de **Java Records**:

```text
  [Apresentação (REST)]  ---> [Aplicação (Use Case)]
                                     |
                                     v
  [Infraestrutura (JPA)] <--- [Aplicação (Port Out)] <--- [Domínio (Model Record)]
```

### Estrutura de Pacotes:
- **`domain.model`**: Contém o `Coupon.java` como um **Record imutável** e auto-validável.
- **`domain.exception`**: Exceções de negócio puras.
- **`application.usecase`**: Define e implementa os casos de uso da aplicação.
- **`application.port.out`**: Interfaces de saída para comunicação com infraestrutura.
- **`presentation.rest`**: Controladores REST da API.
- **`presentation.dto`**: Objetos de entrada (Request) e saída (Response).
- **`presentation.mapper`**: Mapeamento entre Domínio e DTOs via MapStruct.
- **`presentation.exception`**: Tratamento global de erros da API.
- **`infrastructure.persistence.jpa`**: Implementações de persistência com Spring Data JPA.
- **`infrastructure.config`**: Configurações de infraestrutura e frameworks.

- **Infraestrutura (JPA)**: Configurações do Hibernate para evitar o problema do **N+1** através do `default_batch_fetch_size: 20` e estatísticas habilitadas para monitoramento.

## Como Executar

### Pré-requisitos
- JDK 17
- Maven
- Docker (opcional)

### Usando Maven (Recomendado)
```bash
# No Windows
.\mvnw clean install
.\mvnw spring-boot:run

# No Linux/macOS (dar permissão de execução primeiro se necessário)
chmod +x mvnw
./mvnw clean install
./mvnw spring-boot:run
```

### Usando Maven (Global - se instalado)
```bash
mvn clean install
mvn spring-boot:run
```

### Usando Docker
```bash
mvn clean install
docker-compose up --build
```

A API estará disponível em `http://localhost:8080`.

## Endpoints Disponíveis

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST   | `/api/v1/coupons` | Cria um novo cupom |
| DELETE | `/api/v1/coupons/{id}` | Realiza a exclusão lógica (soft delete) |

## Regras de Negócio

### Criação de Cupom
- **Sanitização de Código:** Caracteres especiais são removidos. O código final deve ter exatamente 6 caracteres.
- **Valor de Desconto:** Mínimo de 0.5.
- **Data de Expiração:** Não pode ser no passado.
- **Publicação:** Pode ser criado já publicado (default: false).

### Exclusão (Soft Delete)
- O cupom é marcado com `deletedAt = now()` no banco de dados.
- O registro físico permanece no banco, mas é ignorado em buscas padrão do JPA.
- Não é possível deletar um cupom já deletado.

## Testes
Para executar os testes unitários e de integração:
```bash
.\mvnw test
```
A cobertura de testes foca em:
- **Domínio:** Validações de criação e lógica de soft delete.
- **Service:** Mocking do repositório e orquestração.
- **Integração:** Fluxos completos via MockMvc e banco H2.

## Swagger (Documentação)
Com a aplicação em execução, acesse:
`http://localhost:8080/swagger-ui.html`
