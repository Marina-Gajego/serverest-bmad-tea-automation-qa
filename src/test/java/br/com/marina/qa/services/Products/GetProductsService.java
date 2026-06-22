package br.com.marina.qa.services.Products;

import br.com.marina.qa.model.Products.GetProductsModel;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static br.com.marina.qa.paths.Paths.BASE_URL;
import static br.com.marina.qa.paths.Paths.PRODUCTS_ENDPOINT;
import static io.restassured.RestAssured.given;

@Slf4j
public class GetProductsService {

    private static final long WAIT_BEFORE_GET_IN_MILLISECONDS = 500;

    public Response getProducts(GetProductsModel queryParams) {
        return getProducts(toQueryParams(queryParams));
    }

    public Response getProductsWithoutQueryParams() {
        waitBeforeGet();

        Response response = given()
                .baseUri(BASE_URL)
                .basePath(PRODUCTS_ENDPOINT)
                .contentType("application/json")
                .when()
                .log().all()
                .get();

        log.info("Get products. Status code: {}", response.getStatusCode());
        return response;
    }

    public Response getProducts(Map<String, Object> queryParams) {
        waitBeforeGet();

        Response response = given()
                .baseUri(BASE_URL)
                .basePath(PRODUCTS_ENDPOINT)
                .contentType("application/json")
                .queryParams(queryParams)
                .when()
                .log().all()
                .get();

        log.info("Get products. Status code: {}", response.getStatusCode());
        return response;
    }

    private void waitBeforeGet() {
        try {
            Thread.sleep(WAIT_BEFORE_GET_IN_MILLISECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting before GET products request", exception);
        }
    }

    private Map<String, Object> toQueryParams(GetProductsModel queryParams) {
        Map<String, Object> params = new HashMap<>();

        addIfPresent(params, "_id", queryParams.getId());
        addIfPresent(params, "nome", queryParams.getNome());
        addIfPresent(params, "preco", queryParams.getPreco());
        addIfPresent(params, "descricao", queryParams.getDescricao());
        addIfPresent(params, "quantidade", queryParams.getQuantidade());

        return params;
    }

    private void addIfPresent(Map<String, Object> params, String key, Object value) {
        if (value != null) {
            params.put(key, value);
        }
    }
}
