@cancelBooking
Feature: Cancel Booking

    As a user of the Hotel Booking API,
    I want to cancel an existing booking
    So that I can free up a reserved room when my plans change
    
    Description: This feature tests the Cancel Booking API which allows users to cancel their existing bookings. 
        The scenarios cover both positive and negative cases to ensure the API functions correctly under various conditions.

# ==================== POSITIVE SCENARIOS ====================
    @smoke @api @regression @positive @CB_1
    Scenario Outline: Complete happy path - Create, Book, and Receive Confirmation
    # Login and generate token  
    When User authenticates as user credentials:
      | username | password |
      | admin    | password |
    And User stores the authentication token
    Then API response status code for E2E should be 200

    # Search for available rooms with specific date range and Create a booking with valid details
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

    # Retrieve the created booking details and verify the booking information
     When User retrieves booking details using stored booking ID via API
     Then API response should contain booking details with status code 200

    # Cancel the booking and verify cancellation
    When User attempts to cancel booking with ID via API
    Then Booking cancellation should be successful with status 200
    And API response should return 404 when retrieving cancelled booking details


Examples:
      | firstname | lastname | email              | phone       | roomId | checkin    | checkout   |depositpaid|
      | Josh      | Mike     | josh@example.com   | 07358480685 | 1      | 2025-12-19 | 2025-12-21 |false      |
   
   # ==================== NEGATIVE SCENARIOS ====================
    @regression @negative @CB_2
    Scenario Outline: Attempt to cancel a non-existent booking

    # Login and generate token  
    When User authenticates as user credentials:
      | username | password |
      | admin    | password |
    And User stores the authentication token
    Then API response status code for E2E should be 200

    When User attempts to cancel booking with non-existent ID "<bookingId>" via API
    Then Response should indicate an error message "<errorMessage>"

    Examples:
      | bookingId | errorMessage              |
      | 9999      | Failed to delete booking  |