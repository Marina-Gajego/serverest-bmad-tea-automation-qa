package br.com.marina.qa.stepsDefinitions.Produts;

import br.com.marina.qa.context.ScenarioContext;
import br.com.marina.qa.factory.Products.GetProductsFactory;
import br.com.marina.qa.model.Products.GetProductsModel;
import br.com.marina.qa.services.Products.GetProductsService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GetProductsSteps {

    private final GetProductsService getProductsService;
    private final ScenarioContext context;

    public GetProductsSteps(GetProductsService getProductsService, ScenarioContext context) {
        this.getProductsService = getProductsService;
        this.context = context;
    }

    @When("I send a GET request to the products endpoint with the {string} query parameter")
    public void sendGetProductsRequestWithQueryParameter(String field) {
        Object value = getCreatedProductFieldValue(field);
        GetProductsModel parameters = GetProductsFactory.getProductBy(field, value);
        sendGetProductsRequest(parameters);
    }

    @When("I send a GET request to the products endpoint with the {string} query parameter and value {string}")
    public void sendGetProductsRequestWithQueryParameterAndValue(String field, String value) {
        Object queryParamValue = resolveQueryParamValue(field, value);

        if (isUnsupportedQueryParam(field)) {
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put(field, queryParamValue);

            sendGetProductsRequest(queryParams);
            return;
        }

        GetProductsModel parameters = GetProductsFactory.getProductBy(field, queryParamValue);
        sendGetProductsRequest(parameters);
    }

    @When("I send a GET request to the products endpoint without query parameters")
    public void sendGetProductsRequestWithoutQueryParameters() {
        Response response = getProductsService.getProductsWithoutQueryParams();
        context.setResponse(response);
    }

    @When("I send a GET request to the products endpoint")
    public void sendGetProductsRequestWithAllQueryParameters() {
        GetProductsModel parameters = GetProductsFactory.getProductAllParams(
                context.getProductId(),
                context.getProductName(),
                context.getProductPreco(),
                context.getProductDescricao(),
                context.getProductQuantidade()
        );
        sendGetProductsRequest(parameters);
    }

    @And("The response should contain the correct product")
    public void theResponseShouldContainTheCorrectProduct() {
        Response response = context.getResponse();
        List<Map<String, Object>> products = response.jsonPath().getList("produtos");

        assertThat(products)
                .anySatisfy(product -> {
                    assertThat(product.get("_id")).isEqualTo(context.getProductId());
                    assertThat(product.get("nome")).isEqualTo(context.getProductName());
                    assertThat(product.get("preco")).isEqualTo(context.getProductPreco());
                    assertThat(product.get("descricao")).isEqualTo(context.getProductDescricao());
                    assertThat(product.get("quantidade")).isEqualTo(context.getProductQuantidade());
                });
    }

    @And("The response should not contain any products")
    public void theResponseShouldNotContainAnyProducts() {
        Response response = context.getResponse();
        assertThat(response.jsonPath().getInt("quantidade")).isZero();
        assertThat(response.jsonPath().getList("produtos")).isEmpty();
    }

    @And("The response should not contain the created product")
    public void theResponseShouldNotContainTheCreatedProduct() {
        Response response = context.getResponse();
        assertThat(response.asString())
                .doesNotContain(context.getProductId())
                .doesNotContain(context.getProductName());
    }

    private Object getCreatedProductFieldValue(String field) {
        return switch (field.toLowerCase()) {
            case "_id" -> context.getProductId();
            case "nome" -> context.getProductName();
            case "preco" -> context.getProductPreco();
            case "descricao" -> context.getProductDescricao();
            case "quantidade" -> context.getProductQuantidade();
            default -> throw new IllegalArgumentException("Field not supported: " + field);
        };
    }

    private Object resolveQueryParamValue(String field, String value) {
        String normalizedValue = value.toLowerCase();

        return switch (normalizedValue) {
            case "specialcharacters" -> getCreatedProductFieldValue(field) + "@#$";
            case "uppercase" -> getCreatedProductFieldValue(field).toString().toUpperCase();
            case "leadingspaces" -> "   " + context.getProductName();
            default -> value;
        };
    }

    private boolean isUnsupportedQueryParam(String field) {
        return field.equalsIgnoreCase("categoria");
    }

    private void sendGetProductsRequest(GetProductsModel parameters) {
        Response response = getProductsService.getProducts(parameters);
        context.setResponse(response);
    }

    private void sendGetProductsRequest(Map<String, Object> parameters) {
        Response response = getProductsService.getProducts(parameters);
        context.setResponse(response);
    }
}
