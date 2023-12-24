Feature: DELETE method /books
  positive and negative tests for DELETE method /books

  Background: Creating one books for next deletion
    Given User create request to endpoint "books"
    And User is authorized with default user
    When Send POST request
      | name        | Test Name        |
      | author      | Test author      |
      | publication | Test publication |
      | category    | Test category    |
      | pages       | 999              |
      | price       | 1                |
    And save recently create book's id

  @Positive
  Scenario: verify DELETE method /books
    Given User create request to endpoint "books"
    And User is authorized with default user
    When Delete recently created book
    Then Status code is 200
    And check that last created book was deleted

  @Positive
  Scenario: verify multiple deletion of /books
    Given User create request to endpoint "books"
    And User get all books
    When User delete all book with ID greater then 2
    Then check that max 2 books left in the list of books

  @Positive
  Scenario: verify DELETE method /books
    Given User create request to endpoint "books"
    And User is authorized with default user
    When Send POST request
      | name        | Test Name        |
      | author      | Test author      |
      | publication | Test publication |
      | category    | Test category    |
      | pages       | 999              |
      | price       | 1                |
    And save recently create book's id
    When Delete recently created book
    Then Status code is 200

  @Negative
  Scenario: try to DELETE non-exist book
    Given User create request to endpoint "books"
    And User is authorized with default user
    When Send DELETE request to delete book with id 999999
    Then User see an error message with text "Not Found"
    And Check that error message contains correct timestamp
    And Check that error message's status is 404
    And Check that path is as expected
    And Status code is 404


