# Documentation Index

This repository contains API automation for the ServeRest project, guided by BMad TEA test architecture workflows.

## Core Documentation

- [Project Context](../project-context.md): AI-facing rules, stack details, architecture patterns, naming conventions, and critical implementation rules.
- [Local Setup](LOCAL_SETUP.md): How to run the project locally and execute the test suite.

## Project Summary

- Language: Java 17
- Build tool: Maven
- Test stack: REST Assured, Cucumber, JUnit 5
- Purpose: API test automation for ServeRest, with JSON Schema validation, BDD scenarios, and BMad TEA workflows for automation, review, and CI/CD evolution

## Main Areas

- `src/test/java/br/com/marina/qa/context`: shared scenario state
- `src/test/java/br/com/marina/qa/factory`: test data builders
- `src/test/java/br/com/marina/qa/model`: request and response DTOs
- `src/test/java/br/com/marina/qa/paths`: endpoint and base URL constants
- `src/test/java/br/com/marina/qa/services`: REST Assured API calls
- `src/test/java/br/com/marina/qa/stepsDefinitions`: Cucumber step bindings
- `src/test/resources/features`: feature files
- `src/test/resources/schemas`: JSON Schema contracts

## Notes

- The repository already has a strong `project-context.md`; this index is the navigation layer for humans and agents.
- The project is structured as a single test automation codebase rather than a multi-part application.
