package br.com.marina.qa.stepsDefinitions.Produts;

import br.com.marina.qa.context.ScenarioContext;
import br.com.marina.qa.services.Products.GetProductsByIdService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class GetProductsByIdSteps {

    private final GetProductsByIdService getProductsByIdService;
    private final ScenarioContext scenarioContext;

    public GetProductsByIdSteps(GetProductsByIdService getProductsByIdService, ScenarioContext scenarioContext) {
        this.getProductsByIdService = getProductsByIdService;
        this.scenarioContext = scenarioContext;
    }

    @When("I send a GET request to the product endpoint with the created product id")
    public void iSendAGetRequestToTheProductEndpointWithTheCreatedProductId() {
        String id = scenarioContext.getProductId();
        Response response = getProductsByIdService.getProductsById(id);
        scenarioContext.setResponse(response);
    }

    @When("I send a GET request to the products with {string}")
    public void iSendAGetRequestToTheProductsWithCondition(String condition) {
        Response response = switch (condition) {
            case "nonexistent" -> getProductsByIdService.getProductsById("abcdef1234567890");
            case "invalid" -> getProductsByIdService.getProductsById("invalid@id#chars");
            case "idminusculo" -> getProductsByIdService.getProductsById(scenarioContext.getProductId().toLowerCase());
            case "excededid" -> getProductsByIdService.getProductsById("123456abcde123yhgdri");
            default -> throw new IllegalArgumentException("Condição não mapeada: " + condition);
        };
        scenarioContext.setResponse(response);
    }

    @Then("The response should contain the correct product details")
    public void theResponseShouldContainTheCorrectProductDetails() {
        Response response = scenarioContext.getResponse();
        assertThat(response.jsonPath().getString("nome")).isEqualTo(scenarioContext.getProductName());
        assertThat(response.jsonPath().getInt("preco")).isEqualTo(scenarioContext.getProductPreco());
        assertThat(response.jsonPath().getString("descricao")).isEqualTo(scenarioContext.getProductDescricao());
        assertThat(response.jsonPath().getInt("quantidade")).isEqualTo(scenarioContext.getProductQuantidade());
        assertThat(response.jsonPath().getString("_id")).isEqualTo(scenarioContext.getProductId());
    }
}