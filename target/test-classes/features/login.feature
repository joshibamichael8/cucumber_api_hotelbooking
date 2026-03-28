@Login
Feature: Login to the Hotel Booking API -Shady Meadows B&B API

As a user of the Hotel Booking API, 
I want to be able to log in with valid credentials
So that I can access protected resources and perform authorized actions.

Description: Test suite for the user authentication functionality of the Hotel Booking API.

# ==================== POSITIVE SCENARIOS ====================

@api @positive @regression @authReg1
Scenario Outline: User authentication with valid credentials
  When User authenticates as user with username "<username>" and password "<password>"
  Then API response status code should be <statusCode>
  And Response should contain a valid authentication token
  And Token should not be empty

Examples:
    | username | password | statusCode |
    | admin    | password | 200        |

# ==================== NEGATIVE SCENARIOS ====================

  @api @negative @regression @authReg2
  Scenario Outline: User authentication with invalid credentials
    When User authenticates as user with username "<username>" and password "<password>"
    Then API response status code should be <statusCode>
    And Response should indicate "<errorMessage>"
    And Response should not contain authentication token

    Examples:
      | username           | password        | statusCode | errorMessage         |
      | admin              | wrongpassword   | 401        |  Invalid credentials |#wrong input
      | invaliduser        | password        | 401        |  Invalid credentials |#wrong input
      |                    | password        | 401        | Invalid credentials  |#missing input
      | admin              |                 | 401        | Invalid credentials  |#missing input
      |                    |                 | 401        | Invalid credentials  |#missing input
      | admin' OR '1'='1   | password        | 401        | Invalid credentials  |#SQL injection
      | admin              | ' OR '1'='1     | 401        | Invalid credentials  |#SQL injection
      | admin              | PASSWORD        | 401        | Invalid credentials  |#case sensitivity

