package br.com.marina.qa.stepsDefinitions.Users;

import br.com.marina.qa.context.ScenarioContext;
import br.com.marina.qa.factory.Users.CreateUsersFactory;
import br.com.marina.qa.factory.Users.PutUsersByIdFactory;
import br.com.marina.qa.model.Users.CreateUsersModel;
import br.com.marina.qa.model.Users.PutUsersByIdModel;
import br.com.marina.qa.services.Users.CreateUsersService;
import br.com.marina.qa.services.Users.PutUsersByIdService;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PutUsersByIdSteps {

    private final PutUsersByIdService putUsersByIdService;
    private final CreateUsersService createUsersService;
    private final ScenarioContext context;

    public PutUsersByIdSteps(PutUsersByIdService putUsersByIdService, CreateUsersService createUsersService, ScenarioContext context) {
        this.putUsersByIdService = putUsersByIdService;
        this.createUsersService = createUsersService;
        this.context = context;
    }

    @When("I send a PUT request to the user for update all fields with the created user id")
    public void sendPutUserRequestToUpdateAllFieldsWithCreatedUserId(){
        PutUsersByIdModel updatePayload = PutUsersByIdFactory.updateWithAllFields();
        Response response = putUsersByIdService.updateUserById(context.getId(), updatePayload);
        System.out.println(response.asString());
        context.setResponse(response);
    }

    @When("I send a PUT request to the user with inexistent id")
    public void sendPutUserRequestWithInexistentId(){
        String inexistentId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        PutUsersByIdModel updatePayload = PutUsersByIdFactory.updateWithAllFields();
        Response response = putUsersByIdService.updateUserById(inexistentId, updatePayload);
        System.out.println(response.asString());
        context.setResponse(response);
    }

    @When("I send a PUT request to the user for update the with {string} with the created user id")
    public void sendPutUserRequestToUpdateSpecificFieldWithCreatedUserId(String field) {
        PutUsersByIdModel updatePayload = PutUsersByIdFactory.updateWithSpecificField(field);
        Response response = putUsersByIdService.updateUserById(context.getId(), updatePayload);
        System.out.println(response.asString());
        context.setResponse(response);
    }

    @When("I send a PUT request to the user with invalid {string} format")
    public void sendPutUserRequestWithInvalidFieldFormat(String field) {
        String id = context.getId();
        Object updatePayload = PutUsersByIdFactory.updateWithInvalidFieldFormat(field);

        Response response = putUsersByIdService.updateUserById(id, updatePayload);
        System.out.println(response.asString());
        context.setResponse(response);
    }

    @When("I send a PUT request to the user with empty {string}")
    public void sendPutUserRequestWithEmptyField(String field) {
        String id = context.getId();
        Object updatePayload = PutUsersByIdFactory.updateWithEmptyField(field);

        Response response = putUsersByIdService.updateUserById(id, updatePayload);
        System.out.println(response.asString());
        context.setResponse(response);
    }

    @When("I send a PUT request to the user with the context payload")
    public void sendPutUserRequestWithContextPayload() {
        Response response = putUsersByIdService.updateUserById(context.getId(), context.getPayload());
        System.out.println(response.asString());
        context.setResponse(response);
    }

    @When("I send a PUT request to the users endpoint without id in the path")
    public void sendPutUserRequestWithoutIdInPath(){
        PutUsersByIdModel updatePayload = PutUsersByIdFactory.updateWithAllFields();
        Response response = putUsersByIdService.updateUserWithoutId(updatePayload);
        System.out.println(response.asString());
        context.setResponse(response);
    }

    @When("I send a PUT request to the user using an already registered email")
    public void sendPutUserRequestUsingAlreadyRegisteredEmail(){
        CreateUsersModel anotherUserPayload = CreateUsersFactory.validCreateUser("true");
        Response createResponse = createUsersService.createUser(anotherUserPayload);

        assertThat(createResponse.getStatusCode())
                .as("Second user should be created before validating duplicated email")
                .isEqualTo(201);

        PutUsersByIdModel updatePayload = PutUsersByIdFactory.updateWithUserData(
                context.getNome(),
                anotherUserPayload.getEmail(),
                context.getPassword(),
                context.getAdministrador()
        );

        Response response = putUsersByIdService.updateUserById(context.getId(), updatePayload);
        System.out.println(response.asString());
        context.setResponse(response);
    }

    @When("I send a PUT request to the user with empty body")
    public void sendPutUserRequestWithEmptyBody(){
        Response response = putUsersByIdService.updateUserById(context.getId(), PutUsersByIdFactory.emptyBody());
        System.out.println(response.asString());
        context.setResponse(response);
    }

    @When("I send a PUT request to the user with the same registered data")
    public void sendPutUserRequestWithSameRegisteredData(){
        PutUsersByIdModel updatePayload = PutUsersByIdFactory.updateWithUserData(
                context.getNome(),
                context.getEmail(),
                context.getPassword(),
                context.getAdministrador()
        );

        Response response = putUsersByIdService.updateUserById(context.getId(), updatePayload);
        System.out.println(response.asString());
        context.setResponse(response);
    }
}
