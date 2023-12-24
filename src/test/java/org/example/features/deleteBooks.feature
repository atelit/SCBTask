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

  Scenario: verify DELETE method /books
    Given User create request to endpoint "books"
    And User is authorized with default user
    When Delete recently created book
    Then Status code is 200
    And check that last created book was deleted
