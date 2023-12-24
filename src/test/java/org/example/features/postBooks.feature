Feature: POST method /books
  positive tests for POST method /books

  @Positive @Smoke
  Scenario: verify POST method /books
    Given User create request to endpoint "books"
    And User is authorized with default user
    When Send POST request
      | name        | Refactoring: Improving the Design of Existing Code |
      | author      | Martin Fowler                                      |
      | publication | Addison-Wesley Professional                        |
      | category    | Programming                                        |
      | pages       | 448                                                |
      | price       | 35.50                                              |
    Then check that book was created
    And Status code is 200

  @Negative
  Scenario: Verify that system return 415 when user send request without Content-Type
    Given User create request to endpoint "books"
    And User is authorized with default user
    When Send POST request with empty Content-Type
      | name        | Test Name        |
      | author      | Test author      |
      | publication | Test publication |
      | category    | Test category    |
      | pages       | 999              |
      | price       | 1                |
    Then User see an error message with text "Unsupported Media Type"
    And Check that error message contains correct timestamp
    And Check that error message's status is 415
    And Check that path is as expected
    And Status code is 415

  #id can't be replaced
  @Negative
  Scenario: User try to get access to POST /books with incorrect credentials
    Given User create request to endpoint "books"
    And User is authorized with username "" and password ""
    And erase saved cookies
    When Send POST request
      | name        | Test Name        |
      | author      | Test author      |
      | publication | Test publication |
      | category    | Test category    |
      | pages       | 999              |
      | price       | 1                |
    Then Status code is 401

  @Positive
  Scenario Outline: Verify all fields while creating a multiple books
    Given User create request to endpoint "books"
    And User is authorized with default user
    And User try to add next book: '<name>', '<author>', '<publication>', '<category>', '<pages>', '<price>'
    When Send POST request with predefined book
    Then Verify fields of recently created book
    And Status code is 200
    And Delete recently created book
    Examples:
      | name                                               | author        | publication                 | category    | pages | price  |
      |                                                    |               |                             |             | 0     | 0      |
      |                                                    | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 35.50  |
      | Refactoring: Improving the Design of Existing Code |               | Addison-Wesley Professional | Programming | 448   | 35.50  |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler |                             | Programming | 448   | 35.50  |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional |             | 448   | 35.50  |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 0     | 35.50  |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 0      |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | -35.50 |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | -448  | 35.50  |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 35.50  |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 35.50  |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 35.50  |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 35.50  |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 35.50  |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 35.50  |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 35.50  |

