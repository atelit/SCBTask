package org.example.steps;

import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.example.data.AppConfig;
import org.example.dto.Book;
import org.junit.Assert;

import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class GetBooksDefinitions {
    private RequestSpecification requestSpecification;
    public Response response;
    protected String baseUri = AppConfig.getProperty("uri");
    private Map<String, String> dataTable = Map.of();

    protected Book book;

    List<Book> books;

    @Given("User check books")
    public void user_check_books() {
    }

    @When("User is authorized")
    public void userIsAuthorized() {


    }

    @Then("User can see the list of books")
    public void returnListOfBooks() {
    }

    @When("User is authorized with {string}")
    public void userIsAuthorizedWithDefaultUser() {

    }

    @When("User is authorized with {string} and {string}")
    public void userIsAuthorizedWithUserNameAndNamePassword() {
    }

    @Given("create request to baseURI")
    public void createRequestToBaseURI() {
        requestSpecification = given().baseUri(baseUri);
    }

    @When("Send GET request to {string}")
    public void iSendRequestTo(String arg0) throws IOException {
        response = requestSpecification.pathParams("books", arg0)
                .auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password"))
                .when().get("/{books}");
        books = new ObjectMapper().readValue(response.getBody().asString(), new TypeReference<>() {});
    }

    @Then("Status code is {int}")
    public void responseCodeIs(int statusCode) {
        response.getBody().print();
        System.out.println("Status code is: " + response.getStatusCode());
        assertEquals(statusCode, response.getStatusCode());
    }

    @When("Send GET request to {string} with parameter {int}")
    public void iSendRequestToWithParameter(String endpoint, int id) {
        response = requestSpecification.pathParams("books", endpoint)
                .auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password"))
                .pathParams("id", id)
                .when()
                .get("/{books}/{id}");
    }

    @Then("response contains {string}")
    public void responseContains(String arg0) {
        System.out.println(response.asString());
        assertTrue(response.asString().contains(arg0));
    }

    @Then("response contains data")
    public void responseContainsData(DataTable table) {
        Map<String, String> dataTable = table.asMap(String.class, String.class);
        Book book = response.as(Book.class);
        assertAll(
                () -> assertThat(book.getId(), equalTo(Integer.parseInt(dataTable.get("id")))),
                () -> assertThat(book.getName(), equalTo(dataTable.get("name"))),
                () -> assertThat(book.getAuthor(), equalTo(dataTable.get("author"))),
                () -> assertThat(book.getPublication(), equalTo(dataTable.get("publication"))),
                () -> assertThat(book.getCategory(), equalTo(dataTable.get("category"))),
                () -> assertThat(book.getPages(), equalTo(Integer.parseInt(dataTable.get("pages")))),
                () -> assertThat(book.getPrice(), equalTo(Double.parseDouble(dataTable.get("price"))))
        );
    }

    @SneakyThrows
    @When("Send POST request to {string}")
    public void sendPOSTRequestTo(String endpoint, DataTable table) {
        dataTable = table.asMap(String.class, String.class);
        createBookWithoutID();
        response = requestSpecification.pathParams("books", endpoint)
                .auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password"))
                .contentType(ContentType.JSON)
                .body(new ObjectMapper().writeValueAsString(book))
                .when().post("/{books}");
    }

    @SneakyThrows
    @When("Send POST request to {string} with predefined book")
    public void sendPOSTRequestTo(String endpoint) {
        createBookWithoutID();
        response = requestSpecification.pathParams("books", endpoint)
                .auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password"))
                .contentType(ContentType.JSON)
                .body(new ObjectMapper().writeValueAsString(book))
                .when().post("/{books}");
    }

    @Then("check that book was created")
    public void checkThatBookWasCreated() {
        response.print();
        Book book = response.as(Book.class);
        assertAll(
                () -> assertThat(book.getName(), equalTo(dataTable.get("name"))),
                () -> assertThat(book.getAuthor(), equalTo(dataTable.get("author"))),
                () -> assertThat(book.getPublication(), equalTo(dataTable.get("publication"))),
                () -> assertThat(book.getCategory(), equalTo(dataTable.get("category"))),
                () -> assertThat(book.getPages(), equalTo(Integer.parseInt(dataTable.get("pages")))),
                () -> assertThat(book.getPrice(), equalTo(Double.parseDouble(dataTable.get("price"))))
        );
    }

    @When("Send PUT request to {string}")
    public void sendPUTRequestTo(String endpoint, DataTable table) throws JsonProcessingException {
        dataTable = table.asMap(String.class, String.class);
        createBook();
        response = requestSpecification.pathParams("books", endpoint)
                .auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password"))
                .contentType(ContentType.JSON)
                .body(new ObjectMapper().writeValueAsString(book))
                .when().put("/{books}");
    }



    @When("Send PUT request to {string} with ID {int}")
    public void sendPUTRequestToWithID(String endpoint, int id, DataTable table) throws JsonProcessingException {
        dataTable = table.asMap(String.class, String.class);
        createBook();
        response = requestSpecification
                .pathParams("books", endpoint)
                .pathParams("id", id)
                .auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password"))
                .contentType(ContentType.JSON)
                .body(new ObjectMapper().writeValueAsString(book))
                .when().put("/{books}/{id}");
    }

    @Then("check that book was updated")
    public void checkThatBookWasUpdated() {
        response.print();
        Book updatedBook = response.as(Book.class);
        assertEquals(book, updatedBook);
    }

    @When("Send DELETE request to {string} with id {int}")
    public void sendDELETERequestToWithId(String endpoint, int id) {
        response = requestSpecification
                .pathParams("books", endpoint)
                .pathParams("id", id)
                .auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password"))
                .when().delete("/{books}/{id}");

    }

    @And("book is")
    public void bookIs(DataTable table) {
        dataTable = table.asMap(String.class, String.class);
        createBook();
        System.out.println("Saved book:" + book.toString());
    }


    @And("check that book with id {int} was deleted")
    public void checkThatBookWithIdWasDeleted(int id) throws IOException {
        iSendRequestToWithParameter("books", id);
        response.print();
        iSendRequestTo("books");
        assertFalse(books.stream().anyMatch(x->x.getId() == id));
    }

    @And("Change higher book's id to {int}")
    public void changeHigherBookSIdTo(int id) throws IOException {
        iSendRequestTo("books");
        book.setId(id);
        System.out.println("Book be replaced with:" + book);
        response = requestSpecification
                .pathParams("books", "books")
                .pathParams("id", books.stream().max(Comparator.comparing(Book::getId)).orElseThrow(NoSuchElementException::new).getId())
                .auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password"))
                .contentType(ContentType.JSON)
                .body(new ObjectMapper().writeValueAsString(book))
                .when().put("/{books}/{id}");
        System.out.println("Book replaced: " + response.asString());
        responseCodeIs(200);
    }

    private void createBookWithoutID(){
        book = Book.builder()
                .name(dataTable.get("name"))
                .author(dataTable.get("author"))
                .publication(dataTable.get("publication"))
                .category(dataTable.get("category"))
                .pages(Integer.parseInt(dataTable.get("pages")))
                .price(Double.parseDouble(dataTable.get("price")))
                .build();
    }

    private void createBook(){
        book = Book.builder()
                .id(Integer.parseInt(dataTable.get("id")))
                .name(dataTable.get("name"))
                .author(dataTable.get("author"))
                .publication(dataTable.get("publication"))
                .category(dataTable.get("category"))
                .pages(Integer.parseInt(dataTable.get("pages")))
                .price(Double.parseDouble(dataTable.get("price")))
                .build();
    }
}
