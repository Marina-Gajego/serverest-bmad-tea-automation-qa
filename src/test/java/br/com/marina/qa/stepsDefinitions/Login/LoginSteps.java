package br.com.marina.qa.stepsDefinitions.Login;

import br.com.marina.qa.context.ScenarioContext;
import br.com.marina.qa.factory.Login.LoginFactory;
import br.com.marina.qa.services.Login.LoginService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class LoginSteps {

    private final LoginService loginService;
    private final ScenarioContext context;

    public LoginSteps(LoginService loginService, ScenarioContext context) {
        this.loginService = loginService;
        this.context = context;
    }

    @Given("I have a login payload with the {string} as {string}")
    public void iHaveAPayloadWithCondition(String field, String condition) {
        switch (condition.toLowerCase()) {
            case "missing":
                context.setPayload(field.equals("email") ? LoginFactory.missingEmail() : LoginFactory.missingPassword());
                break;
            case "an integer":
                context.setPayload(LoginFactory.fieldAsInteger(field));
                break;
            case "null":
                context.setPayload(LoginFactory.fieldNull(field));
                break;
            case "invalid format":
                context.setPayload(LoginFactory.invalidFormatEmail());
                break;
            case "empty":
                context.setPayload(LoginFactory.fieldEmpty(field));
                break;
            default:
                throw new IllegalArgumentException("Condition not supported: " + condition);
        }
    }

    @Given("I have login credentials with {string}")
    public void iHaveAPayloadWithConditions(String condition) {
        switch (condition) {
            case "invalid email":
                context.setPayload(LoginFactory.wrongEmail());
                break;
            case "invalid password":
                context.setPayload(LoginFactory.wrongPassword());
                break;
            case "invalid email and password":
                context.setPayload(LoginFactory.wrongEmailAndPassword());
                break;
            default:
                throw new IllegalArgumentException("Condition not supported: " + condition);
        }
    }

    @When("I send a POST request to the authentication endpoint")
    public void iSendAPostRequestToAuthenticationEndpoint() {
        Object payload = context.getPayload();

        if (payload == null && context.getEmail() != null) {
            payload = LoginFactory.validCredentials(context.getEmail(), context.getPassword());
            context.setPayload(payload);
        }

        Response response = loginService.login(payload);
        context.setResponse(response);
        log.info(response.asString());
        log.info("Request sent. Status: {}", response.getStatusCode());
    }

    @And("The response should contain a token")
    public void theResponseShouldContainAToken() {
        Response response = context.getResponse();
        String token = response.jsonPath().getString("authorization");
        assertThat(token).as("Response should contain 'authorization' token").isNotNull().isNotEmpty();
        context.setAuthToken(token);
    }

    @And("The response should not contain a token")
    public void theResponseShouldNotContainAToken() {
        Response response = context.getResponse();
        String token = response.jsonPath().getString("authorization");
        assertThat(token).as("Response should not contain 'authorization' token").isNull();
    }
}