package br.com.marina.qa.services.Products;

import io.restassured.response.Response;

import static br.com.marina.qa.paths.Paths.BASE_URL;
import static br.com.marina.qa.paths.Paths.PRODUCTS_ENDPOINT;
import static io.restassured.RestAssured.given;

public class PutProductsByIdService {

    public Response updateProductById(String id, Object payload, String token) {
        return given()
                .baseUri(BASE_URL)
                .basePath(PRODUCTS_ENDPOINT + "/{id}")
                .contentType("application/json")
                .header("Authorization", token)
                .pathParam("id", id)
                .body(payload)
                .when()
                .log().all()
                .put();
    }

    public Response updateProductWithoutId(Object payload, String token) {
        return given()
                .baseUri(BASE_URL)
                .basePath(PRODUCTS_ENDPOINT)
                .contentType("application/json")
                .header("Authorization", token)
                .body(payload)
                .when()
                .log().all()
                .put();
    }
}
