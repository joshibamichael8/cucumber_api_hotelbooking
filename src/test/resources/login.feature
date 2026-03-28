@Login

Feature: Login to the Hotel Booking API -Shady Meadows B&B API

As a user of the Hotel Booking API, 
I want to be able to log in with valid credentials
So that I can access protected resources and perform authorized actions.

Description: Test suite for the user authentication functionality of the Hotel Booking API.

Scenario Outline: User authentication with valid credentials
  When User authenticates as admin with username "<username>" and password "<password>"
  Then API response status code should be 200
  And Response should contain a valid authentication token
  And Token should not be empty

Examples:
    | username | password | statusCode |
    | admin    | password | 200        |