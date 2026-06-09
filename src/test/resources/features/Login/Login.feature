@regression @Login
Feature: Validate login api in different scenarios

  @id=
  Scenario: Login with valid credentials - (POST /login)
    Given I have a registered user
    When I send a POST request to the authentication endpoint
    Then The response status code should be 200
    And The response should contain the message "Login realizado com sucesso"
    And The response should contain a token
    And The response contract should match "schemas/Login/login.schema.json"

  @id=
  Scenario Outline: Ensure authentication API validates mandatory fields and formats - (POST /login)
    Given I have a login payload with the "<field>" as "<condition>"
    When I send a POST request to the authentication endpoint
    Then The response status code should be 400
    And The response should contain the message "<expected_message>"

    Examples:
      | field    | condition      | expected_message                  |
      | email    | missing        | email é obrigatório               |
      | password | missing        | password é obrigatório            |
      | email    | an integer     | email deve ser uma string         |
      | password | an integer     | password deve ser uma string      |
      | email    | null           | email deve ser uma string         |
      | password | null           | password deve ser uma string      |
      | email    | invalid format | email deve ser um email válido    |
      | email    | empty          | email não pode ficar em branco    |
      | password | empty          | password não pode ficar em branco |

  @id=
  Scenario: Login with malformed JSON payload - (POST /login)
    Given I have a malformed JSON payload
    When I send a POST request to the authentication endpoint
    Then The response status code should be 400
    And The response should contain the message "Adicione aspas em todos os valores. Para mais informações acesse a issue https://github.com/ServeRest/ServeRest/issues/225"

  @id=
  Scenario Outline: Login with invalid credentials - (POST /login)
    Given I have login credentials with "<condition>"
    When I send a POST request to the authentication endpoint
    Then The response status code should be 401
    And The response should contain the message "<expected_message>"
    And The response should not contain a token

    Examples:
      | condition                  | expected_message           |
      | invalid email              | Email e/ou senha inválidos |
      | invalid password           | Email e/ou senha inválidos |
      | invalid email and password | Email e/ou senha inválidos |