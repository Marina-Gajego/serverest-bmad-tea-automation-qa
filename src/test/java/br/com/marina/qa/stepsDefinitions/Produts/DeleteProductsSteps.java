package br.com.marina.qa.stepsDefinitions.Produts;

import br.com.marina.qa.context.ScenarioContext;
import br.com.marina.qa.services.Products.DeleteProductsService;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeleteProductsSteps {

    private final ScenarioContext scenarioContext;
    private final DeleteProductsService deleteProductsService;

    public DeleteProductsSteps(ScenarioContext scenarioContext, DeleteProductsService deleteProductsService) {
        this.scenarioContext = scenarioContext;
        this.deleteProductsService = deleteProductsService;
    }

    @When("I send a DELETE request to the products endpoint with the product ID")
    public void sendDeleteRequestToProductsEndpoint() {
        String productId = scenarioContext.getProductId();
        scenarioContext.setResponse(deleteProductsService.deleteProductById(productId, scenarioContext.getAuthToken()));
    }

    @When("I send a DELETE request to the products endpoint with the same product ID again")
    public void sendDeleteRequestToProductsEndpointAgain() {
        sendDeleteRequestToProductsEndpoint();
    }

    @When("I send a DELETE request to the products endpoint with a non-existent product ID")
    public void sendDeleteRequestWithNonExistentProductId() {
        String nonExistentProductId = "abcdef1234567890";
        scenarioContext.setResponse(deleteProductsService.deleteProductById(nonExistentProductId, scenarioContext.getAuthToken()));
    }

    @When("I send a DELETE request to the products endpoint with the product ID and an invalid token")
    public void sendDeleteRequestWithInvalidToken() {
        String productId = scenarioContext.getProductId();
        String invalidToken = "invalidtoken123456";
        scenarioContext.setResponse(deleteProductsService.deleteProductById(productId, invalidToken));
    }
}
