package br.com.marina.qa.context;

import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;


@Setter
@Getter
public class ScenarioContext {

    private Response response;
    private Object payload;
    private String authToken;
    private final Map<String, Object> testData = new HashMap<>();

    //Login
    private String email;
    private String password;

    //Users
    private String id;
    private String nome;
    private String administrador;

    public void clearAll() {
        response = null;
        payload = null;
        authToken = null;
        testData.clear();
        email = null;
        password = null;
        id = null;
        nome = null;
        administrador = null;
    }
}