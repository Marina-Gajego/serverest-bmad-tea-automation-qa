package br.com.marina.qa.services.Users;

import br.com.marina.qa.model.Users.GetUsersModel;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static br.com.marina.qa.paths.Paths.BASE_URL;
import static br.com.marina.qa.paths.Paths.USERS_ENDPOINT;
import static io.restassured.RestAssured.given;

@Slf4j
public class GetUsersService {

    private static final long WAIT_BEFORE_GET_IN_MILLISECONDS = 500;

    public Response getUsers(GetUsersModel queryParams){
        waitBeforeGet();

        Response response = given()
                .baseUri(BASE_URL)
                .basePath(USERS_ENDPOINT)
                .contentType("application/json")
                .queryParams(toQueryParams(queryParams))
                .when()
                .log().all()
                .get();

        log.info("Get users. Status code: {}", response.getStatusCode());
        return response;
    }

    public Response getUsersWithoutQueryParams(){
        waitBeforeGet();

        Response response = given()
                .baseUri(BASE_URL)
                .basePath(USERS_ENDPOINT)
                .contentType("application/json")
                .when()
                .log().all()
                .get();

        log.info("Get users. Status code: {}", response.getStatusCode());
        return response;
    }

    public Response getUsers(Map<String, Object> queryParams){
        waitBeforeGet();

        Response response = given()
                .baseUri(BASE_URL)
                .basePath(USERS_ENDPOINT)
                .contentType("application/json")
                .queryParams(queryParams)
                .when()
                .log().all()
                .get();

        log.info("Get users. Status code: {}", response.getStatusCode());
        return response;
    }

    private void waitBeforeGet() {
        try {
            Thread.sleep(WAIT_BEFORE_GET_IN_MILLISECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting before GET users request", exception);
        }
    }

    private Map<String, Object> toQueryParams(GetUsersModel queryParams) {
        Map<String, Object> params = new HashMap<>();

        addIfPresent(params, "nome", queryParams.getNome());
        addIfPresent(params, "email", queryParams.getEmail());
        addIfPresent(params, "password", queryParams.getPassword());
        addIfPresent(params, "administrador", queryParams.getAdministrador());
        addIfPresent(params, "_id", queryParams.getId());

        return params;
    }

    private void addIfPresent(Map<String, Object> params, String key, Object value) {
        if (value != null) {
            params.put(key, value);
        }
    }
}
