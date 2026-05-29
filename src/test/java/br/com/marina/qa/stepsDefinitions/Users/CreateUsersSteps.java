package br.com.marina.qa.stepsDefinitions.Users;

import br.com.marina.qa.context.ScenarioContext;
import br.com.marina.qa.factory.Users.CreateUsersFactory;
import br.com.marina.qa.model.Users.CreateUsersModel;
import br.com.marina.qa.services.Users.CreateUsersService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CreateUsersSteps {

    private final CreateUsersService createUsersService;
    private final ScenarioContext context;

    public CreateUsersSteps(CreateUsersService createUsersService, ScenarioContext context){
        this.createUsersService = createUsersService;
        this.context = context;
    }

    @Given("I have a registered user")
    public void iHaveARegisteredUser(){
        CreateUsersModel userPayload = CreateUsersFactory.validCreateUser("true");
        Response response = createUsersService.createUser(userPayload);
        log.info(response.asString());
        context.setResponse(response);
        assertThat(response.getStatusCode())
                .as("User should be created before running this scenario")
                .isEqualTo(201);
        context.setId(response.jsonPath().getString("_id"));
        context.setEmail(userPayload.getEmail());
        context.setPassword(userPayload.getPassword());
        context.setNome(userPayload.getNome());
        context.setAdministrador(userPayload.getAdministrador());
        log.info("User created: {} | {}", context.getEmail(), context.getPassword());
    }

    @And("I have a registered user who is not an admin")
    public void iHaveARegisteredUserWhoIsNotAdmin(){
        CreateUsersModel userPayload = CreateUsersFactory.validCreateUser("false");
        Response response = createUsersService.createUser(userPayload);
        log.info(response.asString());
        context.setResponse(response);
        assertThat(response.getStatusCode())
                .as("User should be created before running this scenario")
                .isEqualTo(201);
        context.setId(response.jsonPath().getString("_id"));
        context.setEmail(userPayload.getEmail());
        context.setPassword(userPayload.getPassword());
        context.setNome(userPayload.getNome());
        context.setAdministrador(userPayload.getAdministrador());
        log.info("Non-admin user created: {} | {}", context.getEmail(), context.getPassword());
    }

    @Given("I have a valid user payload")
    public void iHaveAValidUserPayload(){
        CreateUsersModel userPayload = CreateUsersFactory.validCreateUser("true");
        context.setPayload(userPayload);
    }

    @Given("I have a user creation payload with {string}")
    public void iHaveAUserCreationPayloadWith(String condition){
        CreateUsersModel userPayload = CreateUsersFactory.invalidField(condition);
        context.setPayload(userPayload);
    }

    @Given("I have a create user payload with the {string} as {string}")
    public void iHaveACreateUserPayloadWithTheAs(String field, String condition){
        switch (condition.toLowerCase()){
            case "missing":
                context.setPayload(CreateUsersFactory.missingField(field));
                break;
            case "an integer":
                context.setPayload(CreateUsersFactory.fieldAsInteger(field));
                break;
            case "null":
                context.setPayload(CreateUsersFactory.fieldAsNull(field));
                break;

            default:
                throw new IllegalArgumentException("Condition not supported: " + condition);
        }
    }

    @When("I send a POST request to the users endpoint")
    public void iSendAPostRequestToTheUsersEndpoint(){
        Response response = createUsersService.createUser(context.getPayload());
        log.info(response.asString());
        context.setResponse(response);
        log.info("The create user payload is sent. Status: {}", response.getStatusCode());
    }

    @And("The response should contain id")
    public void theResponseShouldContainId(){
        Response response = context.getResponse();
        String id = response.jsonPath().getString("_id");
        assertThat((id)).as("Response should contain '_id'").isNotNull().isNotEmpty();
        context.setId(id);
    }

    @And("The response should not contain id")
    public void theResponseShouldNotContainId(){
        Response response = context.getResponse();
        String id = response.jsonPath().getString("_id");
        assertThat(id).as("Response should not contain '_id'").isNull();
    }
}
