@regression @Users
Feature: Validate DELETE user API in different scenarios

  @id=
  Scenario: Delete an existing user successfully - (DELETE /usuarios/{_id})
    Given I have a registered user
    When I send a DELETE request to the users endpoint with the created user id
    Then The response status code should be 200
    And The response should contain the message "Registro excluído com sucesso"
    And The response contract should match "schemas/CreateUser/delete_user_success.schema.json"

  @id=
  Scenario: Delete an already deleted user - (DELETE /usuarios/{_id})
    Given I have a registered user
    When I send a DELETE request to the users endpoint with the created user id
    And I send a DELETE request to the users endpoint with the created user id
    Then The response status code should be 200
    And The response should contain the message "Nenhum registro excluído"
    And The response contract should match "schemas/CreateUser/delete_user_no_record.schema.json"

  @id=
  Scenario: Reject DELETE users request without id - (DELETE /usuarios)
    Given I have a registered user who is not an admin
    When I send a DELETE request to the users endpoint without id
    Then The response status code should be 405
    And The response should contain the message "Não é possível realizar DELETE em /usuarios. Acesse http://localhost:3000 para ver as rotas disponíveis e como utilizá-las."

  @id=
  Scenario: Attempt to delete user with non-existent ID - (DELETE /usuarios/{_id})
    Given I have a registered user
    When I send a DELETE request to the users with "nonexistent"
    Then The response status code should be 200
    And The response should contain the message "Nenhum registro excluído"
    And The response contract should match "schemas/CreateUser/delete_user_no_record.schema.json"

  @id=
  Scenario: Attempt to delete user with invalid ID format - (DELETE /usuarios/{_id})
    Given I have a registered user
    When I send a DELETE request to the users with "invalid"
    Then The response status code should be 200
    And The response should contain the message "Nenhum registro excluído"
    And The response contract should match "schemas/CreateUser/delete_user_no_record.schema.json"

# TODO: Implement the test case below once Carts API is integrated.
# Expected response when trying to delete a user with an active shopping cart:
# {
#   "message": "Não é permitido excluir usuário com carrinho cadastrado",
#   "idCarrinho": "qbMqntef4iTOwWfg"
# }