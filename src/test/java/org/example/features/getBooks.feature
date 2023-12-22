Feature: GET method /books
  positive tests for GET method /books

 #try to read non exist book

  Scenario: positive test with all filled in parameters
    Given User is authorized
    When User check books
    Then User can see the list of books

  Scenario Outline: sample of scenario outline
    Given User check books
    When User is authorized with '<userName>' and '<namePassword>'
    Then User can see the list of books
    Examples:
      | userName | namePassword |
      | user4    | 123          |

  Scenario: verify GET method /books
    Given create request to baseURI
    When Send GET request to "books"
    Then Status code is 200

  Scenario: verify GET method /books by ID
    Given create request to baseURI
    When Send GET request to "books" with parameter 3
    Then Status code is 200

  Scenario: verify response value for GET method /books
    Given create request to baseURI
    When Send GET request to "books" with parameter 2
    Then response contains data
      | id          | 2                                   |
      | name        | Test Driven Development: By Example |
      | author      | Kent Beck                           |
      | publication | Addison-Wesley Professional         |
      | category    | Programming                         |
      | pages       | 240                                 |
      | price       | 29.26                               |
