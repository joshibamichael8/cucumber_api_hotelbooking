
Feature: Create Booking

    As a user of the Hotel Booking API,
    I want to create a new booking with valid details
    So that I can reserve a room at the hotel for my desired dates
    
    Description: This scenario tests the creation of a new booking using the Hotel Booking API. 
        It verifies that a booking can be successfully created with valid input data and that the API returns the expected response.
  
    @Background
    Scenario Outline: User authentication with valid credentials
        When User authenticates as user with username "<username>" and password "<password>"
        Then API response status code should be <statusCode>
        And Response should contain a valid authentication token

        Examples:
            | username | password | statusCode |
            | admin    | password | 200        |
        
# ==================== POSITIVE FLOWS ====================

    @smoke @regression @positive @CreateBooking_1 @api
    Scenario Outline: Create a booking with valid details

        When User enters search criteria with checkin "<checkin>" and checkout "<checkout>"
        And User creates a new booking with following details and submits the booking:
            | firstname   | <firstname>   |
            | lastname    | <lastname>    |
            | email       | <email>       |
            | phone       | <phone>       |
            | checkin     | <checkin>     |
            | checkout    | <checkout>    |
            | depositpaid | <depositpaid> |
        Then API response status code should be 201 for booking creation
        And User should see booking confirmation page with booking ID displayed
        And Booking should contain all submitted details
    
        Examples:
            | firstname | lastname  | email               | phone         | checkin    | checkout   | depositpaid |
            | Krishnan  | Krishnan  | suresh1@gmail.com   | 07358480685   | 2025-07-17 | 2025-07-18 | false       |
            | John      | Doe       | john.doe@example.com| +44123456789  | 2025-08-01 | 2025-08-05 | true        |

# ==================== NEGATIVE FLOWS ====================

    @regression @negative @CreateBooking_2 @api
    Scenario Outline: Attempt to create a booking with missing/invalid required fields

        When User enters search criteria with checkin "<checkin>" and checkout "<checkout>"
        And User attempts to create a new booking with missing required fields:
            | firstname   | <firstname>   |
            | lastname    | <lastname>    |
            | email       | <email>       |
            | phone       | <phone>       |
            | checkin     | <checkin>     |
            | checkout    | <checkout>    |
            | depositpaid | <depositpaid> |
        Then API response status code should be 400 for bad request
        And User gets "<FieldError>" error message for missing or invalid input fields
    
        Examples:
            | firstname | lastname  | email               | phone         | checkin    | checkout   | depositpaid  | FieldError                          |
            |           | Doe       |john.doe@example.com | +44123456789  | 2025-08-01 | 2025-08-05 | false        | Firstname should not be blank       |
            |John       |           |john.doe@example.com | +44123456789  | 2025-08-01 | 2025-08-05 | false        | Lastname should not be blank        |
            |John       | Doe       |                     | +44123456789  | 2025-08-01 | 2025-08-05 | false        | must be a well-formed email address |
            |John       | Doe       |john.doe@example.com |               | 2025-08-01 | 2025-08-05 | false        | must not be empty                   |
            |John       | Doe       |john.doe@example.com | +232323       | 2025-08-01 | 2025-08-05 | false        | size must be between 11 and 21      |
            |John       | Doe       |invalid-email        | +44123456789  | 2025-08-01 | 2025-08-05 | false        | must be a well-formed email address |
        