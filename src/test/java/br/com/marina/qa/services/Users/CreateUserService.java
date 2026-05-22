package br.com.marina.qa.services.Users;

import io.restassured.response.Response;
import static br.com.marina.qa.paths.Paths.*;
import lombok.extern.slf4j.Slf4j;
import static io.restassured.RestAssured.given;

@Slf4j
public class CreateUserService {

    public Response createUser(Object payload){
        Response response = given()
                .baseUri(BASE_URL)
                .basePath(USERS_ENDPOINT)
                .contentType("application/json")
                .body(payload)
                .when()
                .log().all()
                .post();

        log.info("User created with success. Status code: {}", response.getStatusCode());
        return response;
    }
}