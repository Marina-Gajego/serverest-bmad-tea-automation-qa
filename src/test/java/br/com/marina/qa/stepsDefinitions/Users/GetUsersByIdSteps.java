package br.com.marina.qa.stepsDefinitions.Users;

import br.com.marina.qa.context.ScenarioContext;
import br.com.marina.qa.services.Users.GetUsersByIdService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class GetUsersByIdSteps {

    private final GetUsersByIdService getUsersByIdService;
    private final ScenarioContext context;

    public GetUsersByIdSteps(GetUsersByIdService getUsersByIdService, ScenarioContext context) {
        this.getUsersByIdService = getUsersByIdService;
        this.context = context;
    }

    @When("I send a GET request to the users endpoint with the created user id")
    public void sendGetUserRequestWithCreatedUserId(){
        String id = context.getId();
        Response response = getUsersByIdService.getUserById(id);
        context.setResponse(response);
        System.out.println(response.asString());
    }

    @When("I send a GET request to the users with {string}")
    public void sendGetUserRequestWithSpecificId(String condition){
        Response response = null;
        if (condition.equalsIgnoreCase("nonexistent")){
            response = getUsersByIdService.getUserById("abcdef1234567890");
        } else if (condition.equalsIgnoreCase("invalid")){
            response = getUsersByIdService.getUserById("invalid@id#chars");
        } else {
            response = getUsersByIdService.getUserById(condition);
        }

        context.setResponse(response);
    }

    @And("The response should contain the correct user details")
    public void theResponseShouldContainTheCorrectUserDetails() {
        Response response = context.getResponse();

        assertThat(response.jsonPath().getString("_id")).isEqualTo(context.getId());
        assertThat(response.jsonPath().getString("nome")).isEqualTo(context.getNome());
        assertThat(response.jsonPath().getString("email")).isEqualTo(context.getEmail());
        assertThat(response.jsonPath().getString("password")).isEqualTo(context.getPassword());
        assertThat(response.jsonPath().getString("administrador")).isEqualTo(context.getAdministrador());
    }
}
