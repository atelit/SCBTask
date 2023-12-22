Feature: PUT method /books
  positive and negative tests for PUT method /books

  #405 when ID is empty
  @positiveTest
  Scenario: verify PUT method /books
    Given create request to baseURI
    When Send PUT request to "books" with ID 3
      | id          | 3                        |
      | name        | Updated Test Name        |
      | author      | Updated Test author      |
      | publication | Updated Test publication |
      | category    | Updated Test category    |
      | pages       | 111                      |
      | price       | 1                        |
    Then check that book was updated
    And Status code is 200