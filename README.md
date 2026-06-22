# ServeRest BMad TEA Automation QA

[![Badge ServeRest](https://img.shields.io/badge/API-ServeRest-green)](https://github.com/ServeRest/ServeRest/)
[![BMad TEA](https://img.shields.io/badge/BMad-TEA_Test_Architecture-blueviolet)](https://bmad-code-org.github.io/bmad-method-test-architecture-enterprise/)
[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![REST Assured](https://img.shields.io/badge/REST%20Assured-5.4.0-orange)](https://rest-assured.io/)
[![Cucumber](https://img.shields.io/badge/Cucumber-BDD-brightgreen)](https://cucumber.io/)

Este projeto não é apenas um repositório convencional de automação de testes de API.

Ele usa a API [ServeRest](https://github.com/ServeRest/ServeRest) como alvo prático para aplicar uma abordagem de engenharia de testes orientada pelo [BMad Method - Test Architecture Enterprise (TEA)](https://bmad-code-org.github.io/bmad-method-test-architecture-enterprise/), combinando automação com análise, revisão, evolução de cobertura e qualidade contínua.

## Proposta

Automatizar cenários da API ServeRest com Java, REST Assured, Cucumber, JUnit e JSON Schema, usando o BMad TEA como framework de apoio para pensar e evoluir a estratégia de testes.

A ideia central é fugir do formato "só escrever cenários automatizados" e tratar a automação como um produto de qualidade:

- com intenção de cobertura;
- com revisão crítica dos testes;
- com evolução contínua;
- com integração a pipeline;
- com rastreabilidade entre risco, comportamento e evidência.

## BMad TEA no Projeto

O [Test Architect (TEA)](https://bmad-code-org.github.io/bmad-method-test-architecture-enterprise/) é um módulo do BMad voltado para estratégia e automação de testes. Neste projeto, o foco está principalmente nestes workflows:

- [CI/CD Integration](https://bmad-code-org.github.io/bmad-method-test-architecture-enterprise/how-to/workflows/setup-ci): direciona a criação ou melhoria da execução automatizada em pipeline, com atenção a feedback rápido, artefatos e estabilidade.
- [Test Automation](https://bmad-code-org.github.io/bmad-method-test-architecture-enterprise/how-to/workflows/run-automate): apoia a expansão da cobertura automatizada a partir de comportamentos e riscos relevantes.
- [Test Review](https://bmad-code-org.github.io/bmad-method-test-architecture-enterprise/how-to/workflows/run-test-review): avalia a qualidade dos testes, identifica lacunas, falsos positivos, duplicações e oportunidades de melhoria.

## Objetivo

Validar fluxos da API ServeRest com foco em:

- status code;
- payload de request e response;
- contratos JSON Schema;
- regras de negócio dos endpoints;
- cenários positivos e negativos;
- qualidade e manutenibilidade dos testes;
- evolução guiada por BMad TEA.

## Stack

- Java 17
- Maven
- REST Assured 5.4.0
- Cucumber 7.15.0
- JUnit Platform
- AssertJ
- JSON Schema Validator
- Java Faker
- BMad Method - Test Architecture Enterprise

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
- Atualização de usuários por ID.
- Exclusão de usuários por ID.
- Criação de produtos.
- Validação de parâmetros inválidos ou não suportados.
- Validação de contrato com JSON Schema.

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

Também existe um guia local em [docs/LOCAL_SETUP.md](docs/LOCAL_SETUP.md).

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

## Como o Projeto Deve Evoluir

O crescimento da suíte deve seguir uma lógica de arquitetura de testes:

- novos endpoints devem nascer com model, factory, service, steps, feature e schema quando aplicável;
- casos negativos devem gerar payloads realmente inválidos, evitando testes verdes que não exercitam o comportamento prometido;
- revisões com BMad Test Review devem ser usadas para encontrar lacunas de cobertura e falsos positivos;
- a automação deve evoluir para CI/CD com evidência de execução, relatórios e critérios de qualidade.