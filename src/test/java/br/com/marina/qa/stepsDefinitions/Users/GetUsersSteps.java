package br.com.marina.qa.stepsDefinitions.Users;

import br.com.marina.qa.context.ScenarioContext;
import br.com.marina.qa.factory.Users.GetUsersFactory;
import br.com.marina.qa.model.Users.GetUsersModel;
import br.com.marina.qa.services.Users.GetUsersService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GetUsersSteps {

    private final GetUsersService getUsersService;
    private final ScenarioContext context;

    public GetUsersSteps(GetUsersService getUsersService, ScenarioContext context) {
        this.getUsersService = getUsersService;
        this.context = context;
    }

    @When("I send a GET request to the users endpoint with the {string} query parameter")
    public void sendGetUsersRequestWithQueryParameter(String field){
        String value = getCreatedUserFieldValue(field);
        GetUsersModel parameters = GetUsersFactory.getUserBy(field, value);

        sendGetUsersRequest(parameters);
    }

    @When("I send a GET request to the users endpoint with the {string} query parameter and value {string}")
    public void sendGetUsersRequestWithQueryParameterAndValue(String field, String value){
        String queryParamValue = resolveQueryParamValue(field, value);

        if (isUnsupportedQueryParam(field)) {
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put(field, queryParamValue);

            sendGetUsersRequest(queryParams);
            return;
        }

        GetUsersModel parameters = GetUsersFactory.getUserBy(field, queryParamValue);
        sendGetUsersRequest(parameters);
    }

    @When("I send a GET request to the users endpoint with the {string} and {string} query parameters")
    public void sendGetUsersRequestWithTwoQueryParameters(String firstField, String secondField){
        String firstValue = getCreatedUserFieldValue(firstField);
        String secondValue = getCreatedUserFieldValue(secondField);

        GetUsersModel params = GetUsersFactory.getUserByTwoQueryParams(firstField, firstValue, secondField, secondValue);
        sendGetUsersRequest(params);
    }
    
    @When("I send a GET request to the users endpoint with the {string} query parameter and the {string} query parameter with value {string}")
    public void sendGetUsersRequestWithOneValidAndOneCustomQueryParameter(String firstField, String secondField, String value){
        String firstValue = getCreatedUserFieldValue(firstField);

        GetUsersModel params = GetUsersFactory.getUserByTwoQueryParams(firstField, firstValue, secondField, value);
        sendGetUsersRequest(params);
    }

    @When("I send a GET request to the users endpoint")
    public void sendGetUsersRequestWithAllQueryParameters(){
        GetUsersModel parameters = GetUsersFactory.getUserAllParams(context.getNome(), context.getEmail(), context.getPassword(), context.getAdministrador(), context.getId());
        sendGetUsersRequest(parameters);
    }

    @And("The response should contain the correct user")
    public void theResponseShouldContainTheCorrectUser(){
        Response response = context.getResponse();

        List<Map<String, Object>> users = response.jsonPath().getList("usuarios");

        assertThat(users)
                .anySatisfy(user -> {
                    assertThat(user.get("_id")).isEqualTo(context.getId());
                    assertThat(user.get("nome")).isEqualTo(context.getNome());
                    assertThat(user.get("email")).isEqualTo(context.getEmail());
                    assertThat(user.get("password")).isEqualTo(context.getPassword());
                    assertThat(user.get("administrador")).isEqualTo(context.getAdministrador());
                });
    }

    @And("The response should not contain any users")
    public void theResponseShouldNotContainAnyUsers(){
        Response response = context.getResponse();
        assertThat(response.jsonPath().getInt("quantidade")).isZero();
        assertThat(response.jsonPath().getList("usuarios")).isEmpty();
    }

    @And("The response should not contain the created user")
    public void theResponseShouldNotContainTheCreatedUser(){
        Response response = context.getResponse();
        assertThat(response.asString())
                .doesNotContain(context.getId())
                .doesNotContain(context.getEmail());
    }

    private String getCreatedUserFieldValue(String field) {
        return switch (field.toLowerCase()) {
            case "_id" -> context.getId();
            case "nome" -> context.getNome();
            case "email" -> context.getEmail();
            case "password" -> context.getPassword();
            case "administrador" -> context.getAdministrador();
            default -> throw new IllegalArgumentException("Field not supported: " + field);
        };
    }

    private String resolveQueryParamValue(String field, String value) {
        String normalizedValue = value.toLowerCase();

        return switch (normalizedValue) {
            case "malformed" -> context.getEmail().replace("@", "4");
            case "specialcharacters" -> context.getNome() + "@#$";
            case "uppercase" -> getCreatedUserFieldValue(field).toUpperCase();
            case "leadingspaces" -> "   " + context.getNome();
            case "trailingspaces" -> context.getEmail() + " ";
            default -> value;
        };
    }

    private boolean isUnsupportedQueryParam(String field) {
        return field.equalsIgnoreCase("idade");
    }

    private void sendGetUsersRequest(GetUsersModel parameters) {
        Response response = getUsersService.getUsers(parameters);
        context.setResponse(response);
    }

    private void sendGetUsersRequest(Map<String, Object> parameters) {
        Response response = getUsersService.getUsers(parameters);
        context.setResponse(response);
    }
}
