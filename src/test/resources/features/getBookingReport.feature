@getBookingReport
Feature: Get Booking Report

    As a user of the Hotel Booking API,
    I want to retrieve booking reports
    So that I can analyze booking data and trends
    
    Description: This feature tests the Get Booking Report API which allows users to retrieve reports on bookings. 
        The scenarios cover both positive and negative cases to ensure the API functions correctly under various conditions.

    Background: User authentication with valid credentials
        When User authenticates as user with username "admin" and password "password"
        Then API response status code should be 200
        And Response should contain a valid authentication token

    # ==================== POSITIVE SCENARIOS ====================

    @smoke @api @regression @positive @BR_1
    Scenario: Get booking report for a specific date range
        When User requests to get booking report for data available
        Then API response status code of BR should be 200
        And Response should contain booking report data
        And Each booking in the report should have valid details
