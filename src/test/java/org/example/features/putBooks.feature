Feature: PUT method /books
  positive and negative tests for PUT method /books

  @Positive
  Scenario: verify PUT method /books
    Given User create request to endpoint "books"
    And User is authorized with default user
    And Send POST request
      | name        | Refactoring: Improving the Design of Existing Code |
      | author      | Martin Fowler                                      |
      | publication | Addison-Wesley Professional                        |
      | category    | Programming                                        |
      | pages       | 448                                                |
      | price       | 35.50                                              |
    When Send PUT request to update recently created book
      | name        | Updated Test Name        |
      | author      | Updated Test author      |
      | publication | Updated Test publication |
      | category    | Updated Test category    |
      | pages       | 111                      |
      | price       | 1                        |
    Then Status code is 200
    And check that book was updated
    And Delete recently created book

  @Positive
  Scenario: verify PUT method /books without updating price
    Given User create request to endpoint "books"
    And User is authorized with default user
    And Send POST request
      | name        | Refactoring: Improving the Design of Existing Code |
      | author      | Martin Fowler                                      |
      | publication | Addison-Wesley Professional                        |
      | category    | Programming                                        |
      | pages       | 448                                                |
      | price       | 35.50                                              |
    When Send PUT request to update recently created book
      | name        | Updated Test Name        |
      | author      | Updated Test author      |
      | publication | Updated Test publication |
      | category    | Updated Test category    |
      | pages       | 448                      |
      | price       | 1                        |
    Then Status code is 200
    And check that book was updated
    And Delete recently created book

  @Negative
  Scenario: verify that Status code is 500 when user send PUT request to update non-exist book
    Given User create request to endpoint "books"
    And User is authorized with default user
    When Send PUT request to books with ID "99999"
      | id          | 99999                    |
      | name        | Updated Test Name        |
      | author      | Updated Test author      |
      | publication | Updated Test publication |
      | category    | Updated Test category    |
      | pages       | 448                      |
      | price       | 1                        |
    Then User see an error message with text "Internal Server Error"
    And Check that error message contains correct timestamp
    And Check that error message's status is 500
    And Check that path is as expected
    And Status code is 500

  @Negative
  Scenario: verify that Status code is 405 when user send PUT request to update book without ID
    Given User create request to endpoint "books"
    And User is authorized with default user
    When Send PUT request to books with empty ID
      | id          |                          |
      | name        | Updated Test Name        |
      | author      | Updated Test author      |
      | publication | Updated Test publication |
      | category    | Updated Test category    |
      | pages       | 448                      |
      | price       | 1                        |
    Then User see an error message with text "Method Not Allowed"
    And Check that error message contains correct timestamp
    And Check that error message's status is 405
    And Check that path is as expected
    And Status code is 405

  @Positive
  Scenario: user try to update book's ID
    Given User create request to endpoint "books"
    And User is authorized with default user
    And User get all books
    And book is
      | id          | 3                        |
      | name        | Updated Test Name        |
      | author      | Updated Test author      |
      | publication | Updated Test publication |
      | category    | Updated Test category    |
      | pages       | 111                      |
      | price       | 1                        |
    When Send POST request with predefined book
    And Change higher book's id to 3
    Then Status code is 200

