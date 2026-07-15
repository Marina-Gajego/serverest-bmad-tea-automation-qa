@regression @products
Feature: Validate DELETE products API in different scenarios

  @id=
  Scenario: Delete product by ID - (DELETE /produtos/{id})
    Given I have a registered product
    When I send a DELETE request to the products endpoint with the product ID
    Then The response status code should be 200
    And The response should contain the message "Registro excluído com sucesso"

  @id=
  Scenario: Delete non-existent product - (DELETE /produtos/{id})
    Given I have a registered product
    When I send a DELETE request to the products endpoint with a non-existent product ID
    Then The response status code should be 200
    And The response should contain the message "Nenhum registro excluído"

  @id=
  Scenario: Delete product with invalid token - (DELETE /produtos/{id})
    Given I have a registered product
    When I send a DELETE request to the products endpoint with the product ID and an invalid token
    Then The response status code should be 401
    And The response should contain the message "Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"

  @id=
  Scenario: Delete product that has already been deleted - (DELETE /produtos/{id})
    Given I have a registered product
    When I send a DELETE request to the products endpoint with the product ID
    And I send a DELETE request to the products endpoint with the same product ID again
    Then The response status code should be 200
    And The response should contain the message "Nenhum registro excluído"

  ##Add caso de teste onde tenta deletar um produto que esta dentro de um carrinho