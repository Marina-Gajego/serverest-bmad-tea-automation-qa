# ServeRest API Automation QA

[![Badge ServeRest](https://img.shields.io/badge/API-ServeRest-green)](https://github.com/ServeRest/ServeRest/)
[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![REST Assured](https://img.shields.io/badge/REST%20Assured-5.4.0-orange)](https://rest-assured.io/)
[![Cucumber](https://img.shields.io/badge/Cucumber-BDD-brightgreen)](https://cucumber.io/)

Projeto de automação de testes da API [ServeRest](https://github.com/ServeRest/ServeRest), utilizando Java, REST Assured, Cucumber, JUnit e validação de contrato com JSON Schema.

## Objetivo

Validar fluxos da API ServeRest com foco em testes automatizados de contrato, status code, payload e regras de negócio dos endpoints.

## Stack

- Java 17
- Maven
- REST Assured
- Cucumber
- JUnit Platform
- AssertJ
- JSON Schema Validator
- Java Faker

## Cobertura Atual

- Login de usuário.
- Criação de usuários.
- Busca de usuários por query parameters.
- Busca por parâmetros individuais:
  - `_id`
  - `nome`
  - `email`
  - `password`
  - `administrador`
- Busca com parâmetros combinados.
- Busca sem query parameters.
- Validação de parâmetros inválidos ou não suportados.
- Validação de contrato para respostas do endpoint `GET /usuarios`.

## API Utilizada

Este projeto utiliza a API ServeRest, uma API REST pública criada para estudos e práticas de testes manuais e automatizados.

Repositório oficial:
[ServeRest/ServeRest](https://github.com/ServeRest/ServeRest)

Documentação pública:
[https://serverest.dev](https://serverest.dev)

## Executando Com ServeRest Local

Para ter mais controle dos dados durante os testes, suba o ServeRest localmente:

```bash
npx serverest@latest
```

Ou com Docker:

```bash
docker run -p 3000:3000 paulogoncalvesbh/serverest:latest
```

Com a API local ativa, os testes apontam para:

```text
http://localhost:3000
```

## Executando Os Testes

```bash
mvn test
```

Para executar apenas cenários marcados com uma tag específica:

```bash
mvn test -Dcucumber.filter.tags="@teste"
```

## Estrutura

```text
src/test/java/br/com/marina/qa
├── context
├── factory
├── model
├── paths
├── runner
├── services
└── stepsDefinitions

src/test/resources
├── features
└── schemas
```
