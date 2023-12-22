Feature: POST method /books
  positive tests for POST method /books

  #415 when header is empty
  @positiveTest
  Scenario: verify POST method /books
    Given create request to baseURI
    When Send POST request to "books"
      | name        | Test Name        |
      | author      | Test author      |
      | publication | Test publication |
      | category    | Test category    |
      | pages       | 999              |
      | price       | 1                |
    Then check that book was created
    And Status code is 200
