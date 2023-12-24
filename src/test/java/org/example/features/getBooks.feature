Feature: GET method /books
  Positive and negative tests for GET method /books

  @Positive
  Scenario: User try to get access to GET /books with correct credentials
    Given User create request to endpoint "books"
    And User is authorized with default user
    When Send GET request
    Then User can see the list of books
    And Status code is 200

  @Negative
  Scenario Outline: User try to get access to GET /books with incorrect credentials
    Given User create request to endpoint "books"
    And User is authorized with username '<userName>' and password '<namePassword>'
    When Send GET request
    Then User can see empty response body
    And Status code is 401
    Examples:
      | userName | namePassword |
      | user3    | hlB5U1rA     |
      | user4    | password     |
      |          |              |
      | user3    |              |
      |          | password     |

  @Positive
  Scenario: verify response value for GET method /books
    Given User create request to endpoint "books"
    And User is authorized with default user
    When Send GET request to books with parameter 2
    Then response contains data
      | id          | 2                                   |
      | name        | Test Driven Development: By Example |
      | author      | Kent Beck                           |
      | publication | Addison-Wesley Professional         |
      | category    | Programming                         |
      | pages       | 240                                 |
      | price       | 29.26                               |
    And Status code is 200

  @Negative
  Scenario: User trying to read non exist book
    Given User create request to endpoint "books"
    And User is authorized with default user
    When Send GET request to books with parameter 999999
    Then User see an error message with text "Not Found"
    And Check that error message contains correct timestamp
    And Check that error message's status is 404
    And Check that path is as expected
    And Status code is 404

  @Positive
  Scenario: Check that cookies are working as expected
    Given User create request to endpoint "books"
    And User is authorized with default user
    And Send GET request
    And Cookies are saved
    When User is authorized with username "" and password ""
    And Send GET request
    Then Status code is 200
    And erase saved cookies
    
    
