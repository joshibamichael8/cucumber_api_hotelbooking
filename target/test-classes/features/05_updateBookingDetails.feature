@updateBookingDetails @HotelBookingAPI
Feature: Update Booking Details - Shady Meadows B&B
    As a user of the Hotel Booking API,
    I want to update my existing booking details
    So that I can modify my reservation information as needed
    
    
    Description: This feature tests the Update Booking API which allows users to update their existing bookings. 
        The scenarios cover both positive and negative cases to ensure the API functions correctly under various conditions.
  # ==================== POSITIVE FLOWS ====================

  @e2e @smoke @regression @positive @E2E_1 @api
  Scenario Outline: Complete happy path - Create, Update, and Verify Booking Details
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
    #Update the booking with new details and verify the update
    When User attempts to update booking ID with following details:
      | roomid | firstname | lastname | depositpaid | checkin    | checkout   |
      |      6 | Josh      | Juan     | false       | 2026-03-11 | 2026-03-12 |
    Then Booking update should be successful with status 200
    And Updated booking details should be reflected in system
    # Retrieve the updated booking details and verify the changes
    When User retrieves booking details using stored booking ID via API
    Then API response should contain updated booking details with status code 200

    Examples:
      | firstname | lastname | email            | phone       | roomId | checkin    | checkout   | depositpaid |
      | Juan      | Jeff     | juan@example.com | 07358480685 |      1 | 2025-12-12 | 2025-12-13 | false       |
