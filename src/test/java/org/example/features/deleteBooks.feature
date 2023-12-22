Feature: DELETE method /books
  positive and negative tests for DELETE method /books

  Background: create user with id=3
    Given create request to baseURI
    And book is
      | id          | 3                        |
      | name        | Updated Test Name        |
      | author      | Updated Test author      |
      | publication | Updated Test publication |
      | category    | Updated Test category    |
      | pages       | 111                      |
      | price       | 1                        |
    When Send POST request to "books" with predefined book
    And Change higher book's id to 3
    Then Status code is 200

    #Response code is 200 but not 201
  Scenario: verify DELETE method /books
    Given create request to baseURI
    When Send DELETE request to "books" with id 3
    And book is
      | id          | 3                        |
      | name        | Updated Test Name        |
      | author      | Updated Test author      |
      | publication | Updated Test publication |
      | category    | Updated Test category    |
      | pages       | 111                      |
      | price       | 1                        |
    Then Status code is 200
    And check that book with id 3 was deleted
