package br.com.marina.qa.services.Users;

import io.restassured.response.Response;

import static br.com.marina.qa.paths.Paths.BASE_URL;
import static br.com.marina.qa.paths.Paths.USERS_ENDPOINT;
import static io.restassured.RestAssured.given;

public class PutUsersByIdService {

    public Response updateUserById(String id, Object payload){
        return given()
                .baseUri(BASE_URL)
                .basePath(USERS_ENDPOINT + "/{id}")
                .contentType("application/json")
                .pathParam("id", id)
                .body(payload)
                .when()
                .log().all()
                .put();
    }

    public Response updateUserWithoutId(Object payload){
        return given()
                .baseUri(BASE_URL)
                .basePath(USERS_ENDPOINT)
                .contentType("application/json")
                .body(payload)
                .when()
                .log().all()
                .put();
    }
}
