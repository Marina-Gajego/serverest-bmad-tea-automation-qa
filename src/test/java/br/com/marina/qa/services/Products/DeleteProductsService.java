package br.com.marina.qa.services.Products;

import io.restassured.response.Response;

import static br.com.marina.qa.paths.Paths.BASE_URL;
import static br.com.marina.qa.paths.Paths.PRODUCTS_ENDPOINT;
import static io.restassured.RestAssured.given;

public class DeleteProductsService {

    public Response deleteProductById(String id, String token){
        return given()
                .baseUri(BASE_URL)
                .basePath(PRODUCTS_ENDPOINT + "/{id}")
                .contentType("application/json")
                .header("Authorization", token)
                .pathParam("id", id)
                .when()
                .log().all()
                .delete();
    }

    public Response deleteProductWithoutId(){
        return given()
                .baseUri(BASE_URL)
                .basePath(PRODUCTS_ENDPOINT)
                .contentType("application/json")
                .when()
                .log().all()
                .delete();
    }
}
