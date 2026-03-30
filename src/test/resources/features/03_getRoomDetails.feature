@RoomManagement @getRoomDetails @HotelBookingAPI
Feature: Get Room Details API
  As an user of the Hotel Booking API,
  I want to retrieve details of rooms
  So that I can manage room information effectively 

  Description:
    This feature tests the Get Room Details API which allows users to retrieve information about rooms. 
    The scenarios cover both positive and negative cases to ensure the API functions correctly under various conditions.  

  @Background
  Scenario Outline: User authentication with valid credentials
    When User authenticates as user with username "<username>" and password "<password>"
    Then API response status code should be <statusCode>
    And Response should contain a valid authentication token

    Examples:
      | username | password | statusCode |
      | admin    | password |        200 |

  # ==================== POSITIVE SCENARIOS ====================

  @smoke @api @regression @positive @getListOfAllRooms
  Scenario Outline: Get list of all available rooms
    When User requests to get list of all rooms
    Then API response status code of RM should be <statuscode>
    And Response should contain list of rooms
    And Each room should have room details

    Examples:
      | statuscode |
      |        200 |

  @smoke @api @regression @positive @getRoomDetailsById
  Scenario Outline: Get room details by room ID
    When User requests to get room details for roomid "<roomid>"
    Then API response status code of RM should be <statuscode>
    And Response should contain room information
    And Room details should contain "roomid"

    Examples:
      | roomid | statuscode |
      |      1 |        200 |
  
# ==================== NEGATIVE SCENARIOS ====================

  @negative @api @regression @getRoomDetailsWithInvalidId
  Scenario Outline: Get room details with invalid room ID
    When User requests to get room details for roomid "<roomid>"
    Then API response status code of RM should be <statuscode>
    And Response should indicate room not found "<errorMessage>"

    Examples:
      | roomid | statuscode | errorMessage        |
      |  99999 |        503 | Service Unavailable |

  @SchemaValidation @api @regression @getRoomDetailsResponseSchemaValidation
  Scenario Outline: Validate room details response structure against schema
    When User requests to get room details for roomid "<roomid>"
    Then API response status code of RM should be <statuscode>
    And Response should contain room information
    And Room details should conform to the room schema

    Examples:
      | roomid | statuscode |
      |      1 |        200 |
      |      2 |        200 |
