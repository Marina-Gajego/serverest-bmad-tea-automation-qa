@regression @GetUsuarios
Feature: Validate GET users API in different scenarios

  @id=
  Scenario Outline: Get user by <param> query parameter - (GET /usuarios)
    Given I have a registered user
    When I send a GET request to the users endpoint with the "<param>" query parameter
    Then The response status code should be 200
    And The response should contain the correct user
    And The response contract should match "schemas/CreateUser/get_users.schema.json"

    Examples:
      | param         |
      | _id           |
      | nome          |
      | email         |
      | password      |
      | administrador |

  @id=
  Scenario: Get user by all query parameters - (GET /usuarios)
    Given I have a registered user
    When I send a GET request to the users endpoint
    Then The response status code should be 200
    And The response should contain the correct user
    And The response contract should match "schemas/CreateUser/get_users.schema.json"

  @id=
  Scenario Outline: Get user by combined query parameters - (GET /usuarios)
    Given I have a registered user
    When I send a GET request to the users endpoint with the "<param1>" and "<param2>" query parameters
    Then The response status code should be 200
    And The response should contain the correct user
    And The response contract should match "schemas/CreateUser/get_users.schema.json"

    Examples:
      | param1 | param2        |
      | _id    | email         |
      | email  | administrador |
      | nome   | administrador |
      | _id    | nome          |

  @id=
  Scenario Outline: Get created user is not returned by <param> query parameter with a different value - (GET /usuarios)
    Given I have a registered user
    When I send a GET request to the users endpoint with the "<param>" query parameter and value "<value>"
    Then The response status code should be 200
    And The response should not contain the created user
    And The response contract should match "schemas/CreateUser/get_users.schema.json"

    Examples:
      | param         | value            |
      | _id           | 12345            |
      | nome          | Marina           |
      | email         | marina@qa.com.br |
      | password      | marina123        |
      | administrador | false            |

  @id=
  Scenario Outline: Get created user is not returned by combined query parameters with one different value - (GET /usuarios)
    Given I have a registered user
    When I send a GET request to the users endpoint with the "<param1>" query parameter and the "<param2>" query parameter with value "<value>"
    Then The response status code should be 200
    And The response should not contain the created user
    And The response contract should match "schemas/CreateUser/get_users.schema.json"

    Examples:
      | param1 | param2        | value            |
      | _id    | email         | marina@qa.com.br |
      | email  | administrador | false            |
      | nome   | _id           | 12345            |
      | email  | password      | teste            |

  @id=
  Scenario Outline: Reject invalid query parameter values - (GET /usuarios)
    Given I have a registered user
    When I send a GET request to the users endpoint with the "<param>" query parameter and value "<condition>"
    Then The response status code should be 400
    And The response should not contain the created user
    And The response should contain the message "<message_expected>"

    Examples:
    | param         | condition          | message_expected                         |
    | idade         | 23                 | idade não é permitido                    |
    | email         | malformed          | email deve ser um email válido           |
    | administrador | uppercase          | administrador deve ser 'true' ou 'false' |
    | email         | trailingSpaces     | email deve ser um email válido           |
    | email         |                    | email deve ser uma string                |

  @id=
  Scenario Outline: Get no users when query parameter value is transformed - (GET /usuarios)
    Given I have a registered user
    When I send a GET request to the users endpoint with the "<param>" query parameter and value "<condition>"
    Then The response status code should be 200
    And The response should not contain any users
    And The response contract should match "schemas/CreateUser/get_users.schema.json"

    Examples:
      | param | condition         |
      | nome  | specialCharacters |
      | email | uppercase         |

  @id=
  Scenario: Get user by nome query parameter using different letter casing - (GET /usuarios)
    Given I have a registered user
    When I send a GET request to the users endpoint with the "nome" query parameter and value "uppercase"
    Then The response status code should be 200
    And The response should contain the correct user
    And The response contract should match "schemas/CreateUser/get_users.schema.json"

  @id=
  Scenario: Get created user is not returned when nome has leading spaces - (GET /usuarios)
    Given I have a registered user
    Given I have a registered user
    When I send a GET request to the users endpoint with the "nome" query parameter and value "leadingSpaces"
    Then The response status code should be 200
    And The response should not contain the created user
    And The response contract should match "schemas/CreateUser/get_users.schema.json"