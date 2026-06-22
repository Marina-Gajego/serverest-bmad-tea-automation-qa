package br.com.marina.qa.services.Products;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static br.com.marina.qa.paths.Paths.BASE_URL;
import static br.com.marina.qa.paths.Paths.PRODUCTS_ENDPOINT;
import static io.restassured.RestAssured.given;

@Slf4j
public class CreateProductsService {

    public Response createProduct(Object payload, String token){
        Response response = given()
                .baseUri(BASE_URL)
                .basePath(PRODUCTS_ENDPOINT)
                .header("Authorization", token)
                .contentType("application/json")
                .body(payload)
                .when()
                .log().all()
                .post();

        log.info("Create product. Status code: {}", response.getStatusCode());
        return response;
    }
}
