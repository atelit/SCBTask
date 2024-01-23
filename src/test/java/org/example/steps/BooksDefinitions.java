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
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.example.data.AppConfig;
import org.example.dto.Book;
import org.example.dto.ErrorMessage;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Getter
@Setter
public class BooksDefinitions {
    private RequestSpecification requestSpecification;
    private Response response;
    private final String baseUri = AppConfig.getProperty("uri");
    private String endpoint;
    private Map<String, String> dataTable = Map.of();
    private Book book;
    List<Book> books;
    private String username;
    private String password;
    private ErrorMessage errorMessage;
    private String currentPath;
    private Cookies cookies = new Cookies();
    private int lastCreatedBookId;

    @Then("User can see empty response body")
    public void returnEmptyResponseBody() {
        assertTrue(response.getBody().asString().isEmpty());
    }

    @Then("User can see the list of books")
    public void returnListOfBooks() throws JsonProcessingException {
        if (!response.getBody().asString().isEmpty()) {
            assertFalse(response.getBody().asString().isEmpty());
            saveListOfBooks();
        } else {
            System.out.println("RESPONSE IS EMPTY!");
        }

    }

    @When("User is authorized with default user")
    public void userIsAuthorizedWithDefaultUser() {
        username = AppConfig.getProperty("user");
        password = AppConfig.getProperty("password");
    }

    @When("User is authorized with username {string} and password {string}")
    public void userIsAuthorizedWithUserNameAndNamePassword(String providedUsername, String providedPassword) {
        username = providedUsername;
        password = providedPassword;
    }

    @Given("User create request to endpoint {string}")
    public void createRequestToBaseURI(String providedEndpoint) {
        requestSpecification = given().baseUri(baseUri);
        endpoint = providedEndpoint;
    }

    @When("Send GET request")
    public void sendGetRequest() throws JsonProcessingException {
        response = requestSpecification.pathParams("endpoint", endpoint).auth().basic(username, password).cookies(cookies).when().get("/{endpoint}");
        if (response.statusCode() == 200) {
            saveListOfBooks();
        }
    }

    @Then("Status code is {int}")
    public void responseCodeIs(int statusCode) {
        response.print();
        System.out.println("Status code is: " + response.getStatusCode());
        assertEquals(statusCode, response.getStatusCode());
    }

    @When("Send GET request to books with parameter {int}")
    public void iSendRequestToWithParameter(int id) {
        response = requestSpecification.pathParams("endpoint", endpoint).auth().basic(username, password).pathParams("id", id).when().get("/{endpoint}/{id}");
        checkForErrorResponse();
        updateCurrentPath(id);
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
        assertAll(() -> assertThat(book.getId(), equalTo(Integer.parseInt(dataTable.get("id")))), () -> assertThat(book.getName(), equalTo(dataTable.get("name"))), () -> assertThat(book.getAuthor(), equalTo(dataTable.get("author"))), () -> assertThat(book.getPublication(), equalTo(dataTable.get("publication"))), () -> assertThat(book.getCategory(), equalTo(dataTable.get("category"))), () -> assertThat(book.getPages(), equalTo(Integer.parseInt(dataTable.get("pages")))), () -> assertThat(book.getPrice(), equalTo(Double.parseDouble(dataTable.get("price")))));
    }

    @SneakyThrows
    @When("Send POST request")
    public void sendPOSTRequestTo(DataTable table) {
        dataTable = table.asMap(String.class, String.class);
        createBookWithoutID();
        response = requestSpecification.pathParams("endpoint", endpoint).auth().basic(username, password).contentType(ContentType.JSON).body(new ObjectMapper().writeValueAsString(book)).when().post("/{endpoint}");
        System.out.println("Created book: " + response.getBody().asString());
    }

    @When("Send POST request with predefined book")
    public void sendPOSTRequestWithPredefinedBook() throws JsonProcessingException {
        response = requestSpecification.pathParams("endpoint", endpoint).auth().basic(username, password).contentType(ContentType.JSON).body(new ObjectMapper().writeValueAsString(book)).when().post("/{endpoint}");
    }

    @SneakyThrows
    @When("Send POST request to books with predefined book")
    public void sendPOSTRequestTo() {
        createBookWithoutID();
        response = requestSpecification.pathParams("endpoint", endpoint).auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password")).contentType(ContentType.JSON).body(new ObjectMapper().writeValueAsString(book)).when().post("/{endpoint}");
    }

    @Then("check that book was created")
    public void checkThatBookWasCreated() {
        Book expectedBook = response.as(Book.class);
        assertAll(() -> assertThat(expectedBook.getName(), equalTo(dataTable.get("name"))), () -> assertThat(expectedBook.getAuthor(), equalTo(dataTable.get("author"))), () -> assertThat(expectedBook.getPublication(), equalTo(dataTable.get("publication"))), () -> assertThat(expectedBook.getCategory(), equalTo(dataTable.get("category"))), () -> assertThat(expectedBook.getPages(), equalTo(Integer.parseInt(dataTable.get("pages")))), () -> assertThat(expectedBook.getPrice(), equalTo(Double.parseDouble(dataTable.get("price")))));
    }

    @Then("Verify fields of recently created book")
    public void verifyFieldsOfRecentlyCreatedBook() {
        Book actualBook = response.as(Book.class);
        assertAll(() -> assertEquals(book.getName(), actualBook.getName(), "Book's name is not as expected. Book's id = " + actualBook.getId().toString()), () -> assertEquals(book.getAuthor(), actualBook.getAuthor(), "Book's author is not as expected. Book's id = " + actualBook.getId().toString()), () -> assertEquals(book.getPublication(), actualBook.getPublication(), "Book's publication is not as expected. Book's id = " + actualBook.getId().toString()), () -> assertEquals(book.getCategory(), actualBook.getCategory(), "Book's category is not as expected. Book's id = " + actualBook.getId().toString()), () -> assertEquals(book.getPages(), actualBook.getPages(), "Book's pages is not as expected. Book's id = " + actualBook.getId().toString()), () -> assertEquals(book.getPrice(), actualBook.getPrice(), "Book's price is not as expected. Book's id = " + actualBook.getId().toString()));
    }

    @When("Send PUT request to {string}")
    public void sendPUTRequestTo(String endpoint, DataTable table) throws JsonProcessingException {
        dataTable = table.asMap(String.class, String.class);
        createBook();
        response = requestSpecification.pathParams("books", endpoint).auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password")).contentType(ContentType.JSON).body(new ObjectMapper().writeValueAsString(book)).when().put("/{books}");
    }

    @When("Send PUT request to books with ID {string}")
    public void sendPUTRequestToWithID(String id, DataTable table) throws JsonProcessingException {
        dataTable = table.asMap(String.class, String.class);
        createBook();
        response = requestSpecification.pathParams("endpoint", endpoint).pathParams("id", id).auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password")).contentType(ContentType.JSON).body(new ObjectMapper().writeValueAsString(book)).when().put("/{endpoint}/{id}");
        checkForErrorResponse();
        updateCurrentPath(Integer.parseInt(id));
    }

    @When("Send PUT request to update recently created book")
    public void sendPutRequestToUpdateRecentlyCreatedBook(DataTable table) throws JsonProcessingException {
        dataTable = table.asMap(String.class, String.class);
        lastCreatedBookId = response.as(Book.class).getId();
        createBook(lastCreatedBookId);
        response = requestSpecification.pathParams("endpoint", endpoint).pathParams("id", lastCreatedBookId).auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password")).contentType(ContentType.JSON).body(new ObjectMapper().writeValueAsString(book)).when().put("/{endpoint}/{id}");
    }

    @Then("check that book was updated")
    public void checkThatBookWasUpdated() {
        response.print();
        Book updatedBook = response.as(Book.class);
        assertEquals(book, updatedBook);
    }

    @When("Send DELETE request to delete book with id {int}")
    public void sendDELETERequestToWithId(int id) {
        response = requestSpecification.pathParams("endpoint", endpoint).pathParams("id", id).auth().basic(username, password).when().delete("/{endpoint}/{id}");
        checkForErrorResponse();
        updateCurrentPath(id);
    }

    @And("book is")
    public void bookIs(DataTable table) {
        dataTable = table.asMap(String.class, String.class);
        createBook();
        System.out.println("Saved book:" + book.toString());
    }


    @And("check that last created book was deleted")
    public void checkThatBookWithIdWasDeleted() throws JsonProcessingException {
        iSendRequestToWithParameter(lastCreatedBookId);
        response.print();
        sendGetRequest();
        response.print();
        assertFalse(books.stream().anyMatch(x -> x.getId() == lastCreatedBookId));
    }

    @And("Change higher book's id to {int}")
    public void changeHigherBookSIdTo(int id) throws IOException {
        sendGetRequest();
        book.setId(id);
        System.out.println("Book be replaced with:" + book);
        response = requestSpecification.pathParams("endpoint", endpoint).pathParams("id", books.stream().max(Comparator.comparing(Book::getId)).orElseThrow(NoSuchElementException::new).getId()).auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password")).contentType(ContentType.JSON).body(new ObjectMapper().writeValueAsString(book)).when().put("/{endpoint}/{id}");
        System.out.println("Book replaced: " + response.asString());
        responseCodeIs(200);
    }

    private void createBookWithoutID() {
        book = Book.builder().name(dataTable.get("name")).author(dataTable.get("author")).publication(dataTable.get("publication")).category(dataTable.get("category")).pages(Integer.parseInt(dataTable.get("pages"))).price(Double.parseDouble(dataTable.get("price"))).build();
    }

    private void createBook() {
        book = Book.builder().id(Integer.parseInt(dataTable.get("id"))).name(dataTable.get("name")).author(dataTable.get("author")).publication(dataTable.get("publication")).category(dataTable.get("category")).pages(Integer.parseInt(dataTable.get("pages"))).price(Double.parseDouble(dataTable.get("price"))).build();
    }

    private void createBook(int bookId) {
        book = Book.builder().id(bookId).name(dataTable.get("name")).author(dataTable.get("author")).publication(dataTable.get("publication")).category(dataTable.get("category")).pages(Integer.parseInt(dataTable.get("pages"))).price(Double.parseDouble(dataTable.get("price"))).build();
    }

    private void saveListOfBooks() throws JsonProcessingException {
        books = new ObjectMapper().readValue(response.getBody().asString(), new TypeReference<>() {
        });
    }

    @Then("User see an error message with text {string}")
    public void userSeeAnErrorMessage(String expectedErrorMessage) {
        assertEquals(expectedErrorMessage, errorMessage.getError());
    }

    private String getDateWithZeroUTC() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(new Date()).replaceAll(".$", "");
    }

    @And("Check that error message contains correct timestamp")
    public void checkThatErrorMessageContainsCorrectTimestamp() {
        assertTrue(errorMessage.getTimestamp().contains(getDateWithZeroUTC()));
    }

    @And("Check that error message's status is {int}")
    public void checkThatErrorMessageSStatusIs(int expectedStatus) {
        assertEquals(expectedStatus, errorMessage.getStatus());
    }

    @And("Check that path is as expected")
    public void checkThatPathIsAsExpected() {
        assertEquals(currentPath, errorMessage.getPath());
    }

    @And("Cookies are saved")
    public void cookiesAreSaved() {
        cookies = response.getDetailedCookies();
    }

    @And("erase saved cookies")
    public void eraseSavedCookies() {
        cookies = new Cookies();
    }

    @When("Send POST request with empty Content-Type")
    public void sendPOSTRequestWithEmptyContentType(DataTable table) throws JsonProcessingException {
        dataTable = table.asMap(String.class, String.class);
        createBookWithoutID();
        response = requestSpecification.pathParams("endpoint", endpoint).auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password")).body(new ObjectMapper().writeValueAsString(book)).when().post("/{endpoint}");
        if (response.getStatusCode() >= 400 && !response.getBody().asString().isEmpty()) {
            errorMessage = response.as(ErrorMessage.class);
        }
        currentPath = "/api/v1/" + endpoint;
    }

    @And("User try to add next book: {string}, {string}, {string}, {string}, {string}, {string}")
    public void userTryToAddNextBookNameAuthorPublicationCategoryPagesPrice(String name, String author, String publication, String category, String pages, String price) {
        book = Book.builder().name(name).author(author).publication(publication).category(category).pages(Integer.parseInt(pages)).price(Double.parseDouble(price)).build();
    }

    @And("Delete recently created book")
    public void deleteRecentlyCreatedBook() {
        lastCreatedBookId = response.as(Book.class).getId();
        response = requestSpecification.pathParams("endpoint", endpoint, "id", lastCreatedBookId).auth().basic(username, password).when().delete("/{endpoint}/{id}");
    }

    @And("Check that recently created books is deleted")
    public void checkThatRecentlyCreatedBooksIsDeleted() throws JsonProcessingException {
        response = requestSpecification.pathParams("endpoint", endpoint).auth().basic(username, password).when().get("/{endpoint}");
        response.print();
        saveListOfBooks();
        assertFalse(books.stream().anyMatch(x -> x.getId() == lastCreatedBookId));
    }

    @When("Send PUT request to books with empty ID")
    public void sendPUTRequestToBooksWithEmpty(DataTable table) throws JsonProcessingException {
        dataTable = table.asMap(String.class, String.class);
        createBookWithoutID();
        response = requestSpecification.pathParams("endpoint", endpoint).pathParams("id", "").auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password")).contentType(ContentType.JSON).body(new ObjectMapper().writeValueAsString(book)).when().put("/{endpoint}/{id}");
        checkForErrorResponse();
        updateCurrentPath();
    }

    private void checkForErrorResponse() {
        if (response.getStatusCode() >= 400 && !response.getBody().asString().isEmpty()) {
            errorMessage = response.as(ErrorMessage.class);
        }
    }

    private void updateCurrentPath(int id) {
        currentPath = "/api/v1/" + endpoint + "/" + id;
    }

    private void updateCurrentPath() {
        currentPath = "/api/v1/" + endpoint + "/";
    }

    @And("User get all books")
    public void userGetAllBooks() throws JsonProcessingException {
        response = requestSpecification.pathParams("endpoint", endpoint).auth().basic(username, password).when().get("/{endpoint}");
        response.print();
        saveListOfBooks();
    }

    @And("save recently create book's id")
    public void saveRecentlyCreateBookSId() {
        lastCreatedBookId = response.as(Book.class).getId();
    }

    @When("User delete all book with ID greater then {int}")
    public void userDeleteAllBookWithIDGreaterThen(int greaterIDForSave) throws JsonProcessingException {
        for (Book currentBook : books) {
            if (currentBook.getId() > greaterIDForSave) {
                response = requestSpecification.pathParams("endpoint", endpoint).pathParams("id", currentBook.getId()).auth().basic(AppConfig.getProperty("user"), AppConfig.getProperty("password")).contentType(ContentType.JSON).body(new ObjectMapper().writeValueAsString(book)).when().delete("/{endpoint}/{id}");
                responseCodeIs(200);
            }
        }
    }

    @Then("check that max {int} books left in the list of books")
    public void checkThatOnlyBooksLeftInTheListOfBooks(int maxBooks) throws JsonProcessingException {
        userGetAllBooks();
        assertEquals(maxBooks, books.size(), "Expected max: " + maxBooks + " but list of books contains: " + books.size());
    }


    @And("check that book's ID equals {int} as expected")
    public void checkThatBookSIDEqualsAsExpected(int expectedId) {
        assertEquals(expectedId, response.as(Book.class).getId(), "Book's id is " + response.as(Book.class).getId() + " but expected " + expectedId);
    }
}
