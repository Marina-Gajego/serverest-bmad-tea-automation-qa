@regression @CreateUser
Feature: Validate create users api in different scenarios

  @id=
  Scenario: Create user with success - (POST /usuarios)
    Given I have a valid user payload
    When I send a POST request to the users endpoint
    Then The response status code should be 201
    And The response should contain the message "Cadastro realizado com sucesso"
    And The response should contain id
    And The response contract should match "schemas/CreateUser/post_create_user.schema.json"

  @id=
  Scenario Outline: Ensure post create user API validates mandatory fields and formats - (POST /login)
    Given I have a create user payload with the "<field>" as "<condition>"
    When I send a POST request to the users endpoint
    Then The response status code should be 400
    And The response should contain the message "<expected_message>"

    Examples:
      | field         | condition      | expected_message                         |
      | nome          | missing        | nome é obrigatório                       |
      | email         | missing        | email é obrigatório                      |
      | password      | missing        | password é obrigatório                   |
      | administrador | missing        | administrador é obrigatório              |
      | nome          | an integer     | nome deve ser uma string                 |
      | email         | an integer     | email deve ser uma string                |
      | password      | an integer     | password deve ser uma string             |
      | administrador | an integer     | administrador deve ser 'true' ou 'false' |
      | nome          | null           | nome deve ser uma string                 |
      | email         | null           | email deve ser uma string                |
      | password      | null           | password deve ser uma string             |
      | administrador | null           | administrador deve ser 'true' ou 'false' |

  @id=
  Scenario Outline: Fail to create user with invalid data - (POST /usuarios)
    Given I have a user creation payload with "<condition>"
    When I send a POST request to the users endpoint
    Then The response status code should be 400
    And The response should not contain id
    And The response should contain the message "<expected_message>"

    Examples:
      | condition                  | expected_message                         |
      | invalid email              | email deve ser um email válido           |
      | invalid administrador      | administrador deve ser 'true' ou 'false' |