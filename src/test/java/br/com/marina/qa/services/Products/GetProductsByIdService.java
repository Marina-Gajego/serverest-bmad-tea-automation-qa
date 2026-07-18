package br.com.marina.qa.services.Products;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static br.com.marina.qa.paths.Paths.*;
import static io.restassured.RestAssured.given;

@Slf4j
public class GetProductsByIdService {

    public Response getProductsById(String id) {
        Response response = given()
                .baseUri(BASE_URL)
                .basePath(PRODUCTS_ENDPOINT + "/{id}")
                .contentType("application/json")
                .pathParam("id", id)
                .when()
                .log().all()
                .get();
        log.info("Get products by ID. Status code: {}", response.getStatusCode());
        return response;
    }
}
