package br.com.marina.qa.services.Users;

import io.restassured.response.Response;

import static br.com.marina.qa.paths.Paths.BASE_URL;
import static br.com.marina.qa.paths.Paths.USERS_ENDPOINT;
import static io.restassured.RestAssured.given;

public class DeleteUsersService {

    public Response deleteUserById(String id){
        return given()
                .baseUri(BASE_URL)
                .basePath(USERS_ENDPOINT + "/{id}")
                .contentType("application/json")
                .pathParam("id", id)
                .when()
                .log().all()
                .delete();
    }

    public Response deleteUsersWithoutId(){
        return given()
                .baseUri(BASE_URL)
                .basePath(USERS_ENDPOINT)
                .contentType("application/json")
                .when()
                .log().all()
                .delete();
    }
}
