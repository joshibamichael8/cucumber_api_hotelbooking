@E2E @HotelBookingAPI
Feature: End-to-End Booking System Flow - Shady Meadows B&B

    As a user of the Hotel Booking API,
    I want to experience a complete end-to-end flow for booking, updating it, and verifying the changes
    So that I can ensure the entire booking process works seamlessly from start to finish
  
  Description: Comprehensive end-to-end test creating a booking, updating it, and verifying the changes. 
    This scenario simulates a real user journey through the hotel booking process, ensuring that all components of the system work together seamlessly. 
    The test covers authentication, room search, booking creation, booking update, and verification of updated details.

  # ==================== POSITIVE E2E FLOWS ====================

  @e2e @smoke @regression @positive @E2E_1 @api
  Scenario Outline: Complete happy path - Search, Book, and Receive Confirmation
    # Login and generate token  
    When User authenticates as admin credentials:
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
    When User attempts to update booking ID with following details:
     | roomid | firstname | lastname | depositpaid | checkin    | checkout   |
     | 6      | Josh      | Mick     | false       | 2026-03-11 | 2026-03-12 |
    Then Booking update should be successful with status 200
    And Updated booking details should be reflected in system
    When User retrieves booking details using stored booking ID via API

Examples:
      | firstname | lastname | email              | phone       | roomId | checkin    | checkout   |depositpaid|
      | John      | Doe      | john@example.com   | 07358480685 | 1      | 2025-12-15 | 2025-12-18 |false      |
    #   | Jane      | Smith    | jane@example.com   | 07358480686 | 2      | 2025-12-20 | 2025-12-25 |false      |
    #   | Bob       | Johnson  | bob@example.com    | 07358480687 | 3      | 2025-12-10 | 2025-12-12 |false      |

    
    # 
    # Then API response status code should be 200
    # When User attempts to cancel booking with ID
    # Then Booking cancellation should be successful with status 200
    # And User should not be able to retrieve cancelled booking details via API