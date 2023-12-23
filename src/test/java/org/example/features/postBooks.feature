Feature: POST method /books
  positive tests for POST method /books

  #415 when header is empty
  @positiveTest
  Scenario: verify POST method /books
    Given User create request to endpoint "books"
    When Send POST request
      | name        | Test Name        |
      | author      | Test author      |
      | publication | Test publication |
      | category    | Test category    |
      | pages       | 999              |
      | price       | 1                |
    Then check that book was created
    And Status code is 200

  Scenario:
    Given User create request to endpoint "books"
    And User is authorized with username "" and password ""
    When Send POST request
    Then Status code is 200
