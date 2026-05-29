package br.com.marina.qa.services.Users;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import static br.com.marina.qa.paths.Paths.BASE_URL;
import static br.com.marina.qa.paths.Paths.USERS_ENDPOINT;
import static io.restassured.RestAssured.given;

@Slf4j
public class GetUsersByIdService {

    public Response getUserById(String id){
        Response response = given()
                .baseUri(BASE_URL)
                .basePath(USERS_ENDPOINT + "/{id}")
                .contentType("application/json")
                .pathParam("id", id)
                .when()
                .log().all()
                .get();

        log.info("Get user by ID. Status code: {}", response.getStatusCode());
        return response;
    }
}
