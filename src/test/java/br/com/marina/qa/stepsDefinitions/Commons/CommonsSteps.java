package br.com.marina.qa.stepsDefinitions.Commons;

import br.com.marina.qa.context.ScenarioContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CommonsSteps {

    private final ScenarioContext context;

    public CommonsSteps(ScenarioContext context) {
        this.context = context;
    }

    @Given("I have a malformed JSON payload")
    public void iHaveAMalformedJsonPayload() {
        context.setPayload("{ \"invalid-json\": ");
    }

    @And("The response contract should match {string}")
    public void theContractShouldMatchTheLoginResponseSchema(String schemaPath){
        Response response = context.getResponse();
        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(schemaPath));
    }

    @Then("The response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        Response response = context.getResponse();
        int actualStatusCode = response.getStatusCode();

        assertThat(actualStatusCode)
                .as("Status code validation")
                .isEqualTo(expectedStatusCode);
        log.info("Status code validation passed: {}", expectedStatusCode);
    }

    @Then("The response should contain the message {string}")
    public void theResponseShouldContainTheMessage(String expectedMessage) {
        Response response = context.getResponse();
        String body = response.getBody().asString();

        assertThat(body)
                .as("Response body should contain: " + expectedMessage)
                .contains(expectedMessage);

        log.info("Message validation passed");
    }

    @Then("The response should contain the error messages")
    public void theResponseShouldContainTheErrorMessages(DataTable dataTable) {
        Response response = context.getResponse();
        String body = response.getBody().asString();
        List<String> expectedMessages = dataTable.asList();

        assertThat(body)
                .as("Response body should contain all expected error messages")
                .contains(expectedMessages.toArray(new String[0]));

        log.info("Error messages validation passed");
    }
}