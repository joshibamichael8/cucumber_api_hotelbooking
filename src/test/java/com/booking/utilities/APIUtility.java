package com.booking.utilities;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Utility class for API interactions, such as sending requests and handling responses.
 * This class can be expanded to include methods for GET, POST, PUT, DELETE requests,
 * and other HTTP methods as needed.
    * It can also include methods for setting up request specifications, handling authentication,
 */
public class APIUtility {

    private static final Logger LOGGER = LogManager.getLogger(APIUtility.class);
    private int connectionTimeout;
    private int responseTimeout;
    private String authToken;
    private String baseURL;
    private RequestSpecification requestSpec;
    private CommonUtilities commonUtilities;


    public APIUtility() {
        this.commonUtilities = new CommonUtilities();

        this.baseURL = ConfigReader.getProperty("api.base.url", "https://automationintesting.online/api");
        this.connectionTimeout = Integer.parseInt(ConfigReader.getProperty("api.connection.timeout", "5000"));
        this.responseTimeout = Integer.parseInt(ConfigReader.getProperty("api.timeout", "5000"));
        this.authToken = null;
        setupRestAssured();
    }

    /**
     * Setup RestAssured base configuration
     */
    private void setupRestAssured() {
        RestAssured.baseURI = this.baseURL;
        RestAssured.basePath = "";
        LOGGER.info("RestAssured configured with base URL: " + this.baseURL);
    }

    /**
     * Build request specification with common headers and auth token if available
     */
    private RequestSpecification buildRequestSpec() {
        RequestSpecification spec = RestAssured.given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .log().all();
        
        // Add authorization header if token is stored
        if (authToken != null && !authToken.isEmpty()) {
            spec = spec.header("Authorization", "Bearer " + authToken);
            LOGGER.debug("Authorization header added to request with token: " + authToken.substring(0, Math.min(10, authToken.length())) + "...");
        }
        
        return spec;
    }

     /**
     * Perform GET request
     */
    public Response get(String endpoint) {
        LOGGER.info("Performing GET request to endpoint: " + endpoint);
        try {  
            Response response = buildRequestSpec()
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("GET request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing GET request: " + e.getMessage());
            throw e;
        }
    }

     /**
     * Perform GET request with proxy or additional headers
     */
    public Response getWithHeader(String endpoint, String headerName, String headerValue) {
        LOGGER.info("Performing GET request with custom header: " + headerName);
        try {
            Response response = buildRequestSpec()
                .header(headerName, headerValue)
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("GET request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing GET request with header: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform GET request with query parameters
     */
    public Response getWithQueryParams(String endpoint, String... params) {
        LOGGER.info("Performing GET request to endpoint: " + endpoint + " with query parameters");
        try {
            RequestSpecification spec = buildRequestSpec();
            
            // Add query parameters in pairs
            for (int i = 0; i < params.length; i += 2) {
                String key = params[i];
                String value = params[i + 1];
                spec = spec.queryParam(key, value);
                LOGGER.info("Added query parameter: " + key + " = " + value);
            }
            
            Response response = spec
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("GET request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing GET request with query params: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform POST request
     */
    public Response post(String endpoint, Object body) {
        LOGGER.info("Performing POST request to endpoint: " + endpoint);
        try {
            Response response = buildRequestSpec()
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
            
            LOGGER.info("POST request completed. Status Code: " + response.getStatusCode());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error performing POST request: " + e.getMessage());
            throw e;
        }
    }


    public void setAuthToken(String token) {
        LOGGER.info("Setting authentication token for future requests");
        if (token == null || token.isEmpty()) {
            LOGGER.warn("Empty or null token provided to setAuthToken");
        }
        this.authToken = token;
        LOGGER.info("Authentication token stored successfully");
    }

    public void clearAuthToken() {
        LOGGER.info("Clearing authentication token");
        this.authToken = null;
    }

    private static int actualStatusCode;
    public void setStatusCode(int actualStatus) {
        LOGGER.info("Storing actual status code: " + actualStatus);
        actualStatusCode = actualStatus;
    }
    public int getStatusCode() {
        LOGGER.info("Returning actual status code: " + actualStatusCode);
        return actualStatusCode;
    }

}
