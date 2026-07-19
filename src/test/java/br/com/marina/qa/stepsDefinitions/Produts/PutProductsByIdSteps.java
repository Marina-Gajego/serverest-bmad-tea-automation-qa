package br.com.marina.qa.stepsDefinitions.Produts;

import br.com.marina.qa.context.ScenarioContext;
import br.com.marina.qa.factory.Products.CreateProductsFactory;
import br.com.marina.qa.factory.Products.PutProductsByIdFactory;
import br.com.marina.qa.model.Products.CreateProductsModel;
import br.com.marina.qa.model.Products.PutProductsByIdModel;
import br.com.marina.qa.services.Products.CreateProductsService;
import br.com.marina.qa.services.Products.PutProductsByIdService;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PutProductsByIdSteps {

    private final ScenarioContext context;
    private final PutProductsByIdService putProductsByIdService;
    private final CreateProductsService createProductsService;

    public PutProductsByIdSteps(ScenarioContext context, PutProductsByIdService putProductsByIdService, CreateProductsService createProductsService) {
        this.context = context;
        this.putProductsByIdService = putProductsByIdService;
        this.createProductsService = createProductsService;
    }

    @When("I send a PUT request to the product for update all fields with the created product id")
    public void sendPutProductRequestToUpdateAllFieldsWithCreatedProductId() {
        PutProductsByIdModel updatePayload = PutProductsByIdFactory.updateWithAllFields();
        Response response = putProductsByIdService.updateProductById(context.getProductId(), updatePayload, context.getAuthToken());
        context.setResponse(response);
        context.setProductName(updatePayload.getNome());
        context.setProductPreco(updatePayload.getPreco());
        context.setProductDescricao(updatePayload.getDescricao());
        context.setProductQuantidade(updatePayload.getQuantidade());
    }

    @When("I send a PUT request to the products endpoint without id in the path")
    public void sendPutProductRequestWithoutIdInPath() {
        PutProductsByIdModel updatePayload = PutProductsByIdFactory.updateWithAllFields();
        Response response = putProductsByIdService.updateProductWithoutId(updatePayload, context.getAuthToken());
        context.setResponse(response);
    }

    @When("I send a PUT request to the product using an already registered product name")
    public void sendPutProductRequestUsingAlreadyRegisteredProductName() {
        CreateProductsModel anotherProductPayload = CreateProductsFactory.validProduct();
        Response createResponse = createProductsService.createProduct(anotherProductPayload, context.getAuthToken());

        assertThat(createResponse.getStatusCode())
                .as("Second product should be created before validating duplicated name")
                .isEqualTo(201);

        PutProductsByIdModel updatePayload = PutProductsByIdFactory.updateWithProductData(
                anotherProductPayload.getNome(),
                context.getProductPreco(),
                context.getProductDescricao(),
                context.getProductQuantidade()
        );

        Response response = putProductsByIdService.updateProductById(context.getProductId(), updatePayload, context.getAuthToken());
        context.setResponse(response);
    }

    @When("I send a PUT request to the product with {string}")
    public void sendPutProductRequestWithCondition(String condition) {
        String normalizedCondition = condition.toLowerCase();

        switch (normalizedCondition) {
            case "inexistent id" -> {
                String inexistentId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
                updateProduct(inexistentId, PutProductsByIdFactory.updateWithAllFields(), context.getAuthToken());
            }
            case "context payload", "malformed json payload" -> updateProduct(context.getProductId(), context.getPayload(), context.getAuthToken());
            case "empty body" -> updateProduct(context.getProductId(), PutProductsByIdFactory.emptyBody(), context.getAuthToken());
            case "extra unknown fields" -> updateProduct(context.getProductId(), PutProductsByIdFactory.updateWithExtraUnknownFields(), context.getAuthToken());
            case "invalid token" -> updateProduct(context.getProductId(), PutProductsByIdFactory.updateWithAllFields(), "invalidtoken123456");
            case "without authentication token" -> updateProduct(context.getProductId(), PutProductsByIdFactory.updateWithAllFields(), "");
            case "invalid id" -> updateProduct("invalid@id#chars", PutProductsByIdFactory.updateWithAllFields(), context.getAuthToken());
            case "invalid excededid" -> updateProduct("123456abcde123yhgdri", PutProductsByIdFactory.updateWithAllFields(), context.getAuthToken());
            default -> updateProductWithPayloadCondition(normalizedCondition);
        }
    }

    private void updateProductWithPayloadCondition(String condition) {
        String[] parts = condition.split(" ", 2);

        if (parts.length < 2) {
            throw new IllegalArgumentException("Condição não mapeada: " + condition);
        }

        String modifier = parts[0];
        String field = parts[1].replace(" format", "");

        Object updatePayload = switch (modifier) {
            case "only" -> PutProductsByIdFactory.updateWithSpecificField(field);
            case "missing" -> PutProductsByIdFactory.updateWithMissingField(field);
            case "invalid" -> PutProductsByIdFactory.updateWithInvalidFieldFormat(field);
            case "empty" -> PutProductsByIdFactory.updateWithEmptyField(field);
            case "null" -> PutProductsByIdFactory.updateWithNullField(field);
            case "-10", "0", "-1" -> PutProductsByIdFactory.updateWithFieldValue(field, modifier);
            default -> throw new IllegalArgumentException("Condição não mapeada: " + condition);
        };

        updateProduct(context.getProductId(), updatePayload, context.getAuthToken());
    }

    private void updateProduct(String id, Object payload, String token) {
        Response response = putProductsByIdService.updateProductById(id, payload, token);
        context.setResponse(response);
    }
}