package br.com.marina.qa.stepsDefinitions.Produts;

import br.com.marina.qa.context.ScenarioContext;
import br.com.marina.qa.factory.Products.CreateProductsFactory;
import br.com.marina.qa.services.Products.CreateProductsService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CreateProductsSteps {

    private final ScenarioContext context;
    private final CreateProductsService createProductsService;

    public CreateProductsSteps(ScenarioContext context, CreateProductsService createProductsService) {
        this.context = context;
        this.createProductsService = createProductsService;
    }

    @Given("I have a valid product payload")
    public void iHaveAValidProductPayload() {
        context.setPayload(CreateProductsFactory.validProduct());
    }

    @Given("I have a product payload with the {string} as {string}")
    public void iHaveProductPayloadWithFieldAs(String field, String condition) {
        Object product = switch (condition.toLowerCase()) {
            case "missing"        -> CreateProductsFactory.productWithMissingField(field);
            case "null"           -> CreateProductsFactory.productWithNullField(field);
            case "empty"          -> CreateProductsFactory.productWithEmptyField(field);
            case "an integer"     -> CreateProductsFactory.productWithIntegerField(field);
            case "invalid format" -> CreateProductsFactory.productWithInvalidEmailFormat(field);
            default -> throw new IllegalArgumentException("Condition not supported: " + condition);
        };
        context.setPayload(product);
    }

    @Given("I have a product payload with {string} as {string}")
    public void iHaveProductPayloadWithInvalidType(String field, String invalidType) {
        Object product = switch (invalidType.toLowerCase()) {
            case "string"  -> CreateProductsFactory.productWithStringPrice();
            case "integer" -> CreateProductsFactory.productWithIntegerField(field);
            default        -> CreateProductsFactory.productWithField(field, invalidType);
        };
        context.setPayload(product);
    }

    @Given("I have no authentication token")
    public void iHaveNoAuthenticationToken() {
        context.setAuthToken("");
    }

    @Given("I have a product payload with an invalid token")
    public void iHaveProductPayloadWithInvalidToken() {
        context.setPayload(CreateProductsFactory.validProduct());
        context.setAuthToken("invalid-token-12345");
    }

    @Given("I have a product payload with {string} as a very long string {string}")
    public void iHaveProductWithVeryLongField(String field, String lengthIndicator) {
        context.setPayload(CreateProductsFactory.productWithVeryLongField(field));
    }

    @Given("I have a product payload with {string} containing special characters {string}")
    public void iHaveProductWithSpecialCharacters(String field, String specialChars) {
        context.setPayload(CreateProductsFactory.productWithSpecialCharacters(field));
    }

    @Given("I have a product payload with extra unknown fields")
    public void iHaveProductPayloadWithExtraFields() {
        context.setPayload(CreateProductsFactory.productWithExtraUnknownFields());
    }

    @When("I send a POST request to create a product")
    public void iSendAPostRequestToCreateProductEndpoint() {
        Response response = createProductsService.createProduct(context.getPayload(), context.getAuthToken());
        context.setResponse(response);
        log.info("Response Status: {}", response.getStatusCode());
        log.info("Response Body: {}", response.asString());
    }

    @Given("I have a product payload with a short name")
    public void iHaveAProductPayloadWithShortName() {
        context.setPayload(CreateProductsFactory.productWithShortName());
    }

    @Given("I have a product payload with a long name")
    public void iHaveAProductPayloadWithLongName() {
        context.setPayload(CreateProductsFactory.productWithLongName());
    }

    @And("The response should contain a product id")
    public void theResponseShouldContainAProductId() {
        String id = context.getResponse().jsonPath().getString("_id");
        assertThat(id).as("Response should contain '_id'").isNotNull().isNotEmpty();
        context.setProductId(id);
    }
}
