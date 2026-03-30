@checkRoomAvailability @HotelBookingAPI
Feature: Check Room Availability API
As an user of the Hotel Booking API,
I want to check the availability of rooms
So that I can manage bookings effectively 

Description:
  This feature tests the Check Room Availability API which allows users to check if rooms are available for booking. 
  The scenarios cover both positive and negative cases to ensure the API functions correctly
  under various conditions.

  @Background
  Scenario Outline: User authentication with valid credentials
    When User authenticates as user with username "<username>" and password "<password>"
    Then API response status code should be <statusCode>
    And Response should contain a valid authentication token

    Examples:
      | username | password | statusCode |
      | admin    | password |        200 |
# ==================== POSITIVE SCENARIOS ====================

  @api @regression @positive @roomAvailabilityWithValidDates
  Scenario Outline: Check room availability for specific dates
    When User requests to check room availability with checkin "<checkin>" and checkout "<checkout>"
    Then API response status code of RM should be <statuscode>
    And Response should contain available rooms
    And Available rooms list should not be empty

    Examples:
      | checkin    | checkout   | statuscode |
      | 2026-07-17 | 2026-07-18 |        200 |
      | 2026-07-20 | 2026-07-21 |        200 |
      | 2026-12-15 | 2026-12-20 |        200 |
      | 2026-07-01 | 2026-08-05 |        200 |
      | 2026-07-17 | 2026-07-17 |        200 |

# ==================== NEGATIVE SCENARIOS ====================

  @api @negative @regression @roomAvailabilityWithInvalidDates
  Scenario Outline: Check room availability with invalid date formats and ranges
    When User requests to check room availability with checkin "<checkin>" and checkout "<checkout>"
    Then API response status code of RM should be <statuscode>

    Examples:
      | checkin      | checkout   | statuscode |
      |   2025-07-25 | 2025-07-20 |        400 |
      | invalid-date | 2025-07-21 |        503 |
      |   2020-01-01 | 2020-01-02 |        400 |
