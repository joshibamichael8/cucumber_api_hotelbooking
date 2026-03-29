@RoomManagement @checkRoomAvailability @HotelBookingAPI
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
      | admin    | password | 200        |

  # ==================== POSITIVE SCENARIOS ====================

  @api @regression @positive @RM_4
  Scenario Outline: Check room availability for specific dates
    When User requests to check room availability with checkin "<checkin>" and checkout "<checkout>"
    Then API response status code of RM should be <statuscode>
    And Response should contain available rooms
    And Available rooms list should not be empty

    Examples:
      | checkin    | checkout   | statuscode |
      | 2025-07-17 | 2025-07-18 | 200        |
