@room-details @schema-validation @api
Feature: Room Information Retrieval
  As a hotel customer, I want to retrieve comprehensive room information
  And ensure the response structure validates against the defined schema

  @room-details @schema-validation
  Scenario Outline: Retrieve and validate room information by ID
    Given user want to retrieve room information for a specific room ID
    When user request room details for the following ID:
      | <roomid> |
    Then the room information should contain:
      | roomName  |
      | roomPrice |
    And the API response conforms to "roomDetails.json" schema

    Examples:
      | roomid |
      | 1      |
      | 2      |
      | 3      |

    @room-details @schema-validation
    Scenario: Attempt to retrieve room information with invalid ID
      Given user want to retrieve room information for a specific room ID
      When user request room details for the following ID:
        | <roomid> |
      Then API response status code should be 404 for not found
      And User gets "Room not found" error message

      Examples:
        | roomid  |
        | 999     |
        | -1      |
        | abc     |
        | !@#     |
            
