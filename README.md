# Hotel Booking API - Automated Test Suite

## 📋 Project Overview

**Project Name:** Hotel Booking API - Cucumber & TestNG Automation Framework  
**Version:** 1.0-SNAPSHOT  
**Purpose:** Comprehensive REST API testing for the Hotel Booking API (Shady Meadows B&B) using    Behavior-Driven Development (BDD) with Cucumber and TestNG  
**Target API:** https://automationintesting.online/api  

This test suite provides automated testing for all critical API endpoints including authentication, room management, booking operations, and booking reports.

---

## 🎯 Framework Overview

### Key Characteristics
- **BDD Framework:** Cucumber 6.10.4 with Gherkin syntax
- **Test Runner:** TestNG 7.4.0
- **API Testing:** Rest Assured 4.3.0
- **Validation:** JSON Schema Validator 4.3.0
- **Build Tool:** Maven 3.8.0
- **Java Version:** 11
- **Logging:** Log4j2 2.17.1
- **Reports:** HTML, JSON, JUnit, Cucumber Reports

### Current Test Coverage
- **Total Scenarios:** 49 (41 executable + 8 data-driven variations)
- **Passing Tests:** 42 (85.7%)
- **Failing Tests:** 7 (14.3%)
- **Feature Files:** 9 (covering 8 API modules)
- **Step Definitions:** 61 steps across 3 classes
- **Codebase Size:** 1,195 lines of step definitions

---

## 📁 Project Structure

```
api_hotel_booking/
│
├── README.md                                    # Project documentation (this file)
├── pom.xml                                      # Maven configuration & dependencies
├── testng.xml                                   # TestNG suite configuration
│
├── src/
│   ├── main/java/com/booking/                  # Main application code (if needed)
│   │
│   └── test/
│       ├── java/com/booking/
│       │   ├── runners/
│       │   │   ├── TestRunner.java             # PRIMARY TEST RUNNER (Execute this)
│       │   │   └── DirectTestRunner.java       # Alternative runner for direct execution 
|       |   |                                     (Cucumber tests via TestNG programmatically.
|       |   |                                       This bypasses Maven Surefire provider detection
|       |   |                                       issues)
│       │   │
│       │   ├── stepDefinitions/
│       │   │   ├── E2EHotelBooking.java        # Core booking operations (809 lines)
│       │   │   ├── LoginAPIStepDefinition.java # Authentication steps (222 lines)
│       │   │   └── RoomManagementAPI.java      # Room availability steps (164 lines)
│       │   │
│       │   ├── hooks/
│       │   │   └── Hooks.java                  # Setup/teardown logic
│       │   │
│       │   └── utilities/
│       │       ├── APIUtility.java             # REST API call helper
│       │       ├── TokenManager.java           # Token handling
│       │       └── CommonUtilities.java        # Common helper functions
│       │
│       └── resources/
│           ├── features/                       # Cucumber feature files
│           │   ├── 01_login.feature            # Authentication scenarios (6 scenarios)
│           │   ├── 02_checkRoomAvailability.feature  # Room availability (9 scenarios)
│           │   ├── 03_getRoomDetails.feature   # Room details retrieval (8 scenarios)
│           │   ├── 04_createBooking.feature    # Booking creation (4 scenarios)
│           │   ├── 05_updateBookingDetails.feature # Booking updates (2 scenarios)
│           │   ├── 06_cancelBooking.feature    # Booking cancellation (5 scenarios)
│           │   ├── 07_getBookingDetail.feature # Booking details (2 scenarios)
│           │   ├── 08_getBookingReport.feature # Report generation (3 scenarios)
│           │   └── 09_e2eHotelBooking.feature  # End-to-end flow (1 scenarios)
│           │
│           ├── config/
│           │   ├── application.properties      # Configuration settings
│           │   ├── cucumber.properties         # Cucumber configuration
│           │   ├── extent-config.xml           # Extent report configuration
│           │   ├── extent-report.properties    # Report properties
│           │   └── log4j2.xml                  # Logging configuration
│           │
│           └── schemas/                        # JSON Schema files for validation
│               ├── bookingDetails.json         # Booking response schema
│               └── roomDetails.json            # Room details schema
│
├── target/                                     # Build output directory
│   ├── test-classes/                           # Compiled test classes
│   └── surefire-reports/                       # Test execution reports
│
├── reports/                                    # TEST CUCUMBER REPORTS (CHECK THIS FOLDER)
│   ├── cucumber-report.html                    # PRIMARY REPORT - Open in browser
│   ├── cucumber.json                           # JSON format results
│   └── cucumber.xml                            # JUnit XML format
│
└── test-output/                                # Additional TestNG reports
    ├── index.html                              # TestNG HTML report
    └── testng-results.xml                      # TestNG XML results
```

---

## 🚀 How to Execute Tests

### Prerequisites
- **Java 11** (or higher)
- **Maven 3.6.0** (or higher)
- **Git** (for version control)
- **Internet Connection** (tests run against live API)

### Quick Start

#### **Option 1: Run All Tests (Recommended)**
```bash
# Navigate to project directory
cd api_hotel_booking

# Execute all tests with Maven
mvn clean test

# Or use TestNG directly
mvn test -Dgroups="@HotelBookingAPI"
```

**Output:** All 49 scenarios will execute and generate Cucumber reports (api_hotel_booking/report)

---

#### **Option 2: Run Specific Feature**
```bash
# Run only Login feature
mvn test -Dgroups="@Login"

# Run only Room Management tests
mvn test -Dgroups="@checkRoomAvailability"

# Run only Booking tests
mvn test -Dgroups="@createBooking"
```

---

#### **Option 3: Run with Specific Tags**
```bash
# Run only positive/passing tests
mvn test -Dgroups="@positive"

# Run only negative/error tests
mvn test -Dgroups="@negative"

# Run regression tests
mvn test -Dgroups="@regression"

# Run smoke tests
mvn test -Dgroups="@smoke"
```

---

#### **Option 4: Skip Tests**
```bash
# Build without running tests
mvn clean install -DskipTests

# Compile only
mvn clean compile
```

---

### Test Runners Explained

#### **Primary Runner: `TestRunner.java`**
📍 Location: `src/test/java/com/booking/runners/TestRunner.java`

**Purpose:** Main test orchestrator used by Maven Surefire plugin via `testng.xml`

**Configuration:**
```java
@CucumberOptions(
    features = "src/test/resources/features/",                  // All feature files
    glue = {"com.booking.stepDefinitions", "com.booking.hooks"},//Location of the Step Definition
    tags = "@HotelBookingAPI",                                  // Tag filter
    plugin = {
        "pretty",                                               // Console output
        "html:reports/cucumber-report.html",                    // HTML report
        "json:reports/cucumber.json",                           // JSON results
        "junit:reports/cucumber.xml"                            // JUnit XML
    },
    dryRun = false,
    monochrome = true,
    publish = true
)
```

**When to Use:**
- ✅ Standard Maven test execution
- ✅ CI/CD pipeline integration
- ✅ Team development environment

---

#### **Alternative Runner: `DirectTestRunner.java`**
📍 Location: `src/test/java/com/booking/runners/DirectTestRunner.java`

**Purpose:** Direct TestNG execution without Maven Surefire

**When to Use:**
- IDE direct execution
- Quick feedback loop during development
- Debugging specific scenarios

---

### Test Configuration: `testng.xml`
📍 Location: `testng.xml` (root directory)

```xml
<suite name="Cucumber Automation Test Suite" parallel="tests" thread-count="2">
    <test name="API Booking Tests">
        <classes>
            <class name="com.booking.runners.TestRunner"/>
        </classes>
    </test>
</suite>
```

**Configuration Details:**
- **Suite Name:** Cucumber Automation Test Suite
- **Execution Mode:** Parallel tests with 2 threads
- **Test Name:** API Booking Tests

---

## 📊 Test Reports

### 🎯 Viewing Reports (IMPORTANT)

After test execution, reports are generated in the **`reports/`** folder:

#### **1. Cucumber HTML Report** (Primary - Most Detailed) ⭐
```
reports/cucumber-report.html
```
**How to View:**
1. Navigate to `reports/` folder in your project
2. Double-click on `cucumber-report.html`
3. Opens in your default web browser
4. Shows:
   - ✅ Passed/Failed/Skipped tests
   - 📝 Step-by-step execution details
   - 📊 Pass/Fail statistics
   - 🔍 Failure root causes with stack traces
   - ⏱️ Execution time per step
   - 🖼️ Screenshots (if configured)

**Best For:**
- Detailed analysis of test failures
- Team communication
- Trend analysis over time

---

#### **2. JSON Report**
```
reports/cucumber.json
```
**Format:** Machine-readable JSON
**Use Case:** Integration with other tools and dashboards

---

#### **3. JUnit XML Report**
```
reports/cucumber.xml
```
**Format:** Standard JUnit format
**Use Case:** CI/CD systems, Jenkins integration

---

#### **4. TestNG HTML Report**
```
test-output/index.html
```
**Generated by:** TestNG framework
**Contains:** Additional TestNG-specific metrics

---

### Report Navigation Guide

```bash
# After running: mvn clean test

# Reports location:
cd reports/

# Open in browser (Windows)
start cucumber-report.html

# Open in browser (Mac)
open cucumber-report.html

# Open in browser (Linux)
firefox cucumber-report.html &
```

---

## 📝 Feature Files Overview

### 1. **01_login.feature** - Authentication
**Scenarios:** 6  
**Purpose:** Test user authentication with valid and invalid credentials
**Covered Tests:**
- ✅ Valid credentials → Token generation
- ✅ Invalid password → 401 error
- ✅ Invalid username → 401 error
- ✅ Missing credentials → 401 error
- ✅ SQL injection attempts → 401 error

---

### 2. **02_checkRoomAvailability.feature** - Room Availability
**Scenarios:** 9  
**Purpose:** Verify room availability checking with various date ranges
**Covered Tests:**
- ✅ Valid date range → Available rooms listed
- ✅ Same checkin/checkout dates → Rooms returned
- ✅ Invalid date ranges → Proper error handling
- ❌ Date format validation → Needs fixing

---

### 3. **03_getRoomDetails.feature** - Room Details
**Scenarios:** 8  
**Purpose:** Retrieve detailed room information
**Covered Tests:**
- ✅ Valid room ID → Room details returned
- ✅ Schema validation → Details match schema
- ❌ Invalid room ID → 404 error handling

---

### 4. **04_createBooking.feature** - Booking Creation
**Scenarios:** 4  
**Purpose:** Create new bookings with various criteria
**Covered Tests:**
- ✅ Valid booking → 201 Created
- ❌ Missing fields → Should return 400 (currently fails)

---

### 5. **05_updateBookingDetails.feature** - Update Booking
**Scenarios:** 2  
**Purpose:** Modify existing booking details
**Covered Tests:**
- ⚠️ Update with valid data → Response validation failing
- ⚠️ Verify updates applied → Assertion issues

---

### 6. **06_cancelBooking.feature** - Booking Cancellation
**Scenarios:** 5  
**Purpose:** Cancel existing bookings
**Covered Tests:**
- ✅ Cancel valid booking → 200 OK
- ✅ Cancelled booking → 404 Not Found
- ⚠️ Cancel non-existent → Error handling issues

---

### 7. **07_getBookingDetail.feature** - Booking Details
**Scenarios:** 2  
**Purpose:** Retrieve specific booking information
**Covered Tests:**
- ✅ Valid booking ID → Details returned
- ✅ Schema conformance → Valid response

---

### 8. **08_getBookingReport.feature** - Booking Report
**Scenarios:** 3  
**Purpose:** Generate and validate booking reports
**Covered Tests:**
- ✅ Get report for date range → Report data returned
- ✅ Report data structure → Validates booking entries
- ✅ Date range filtering → Reports filtered correctly

---

### 9. **09_e2eHotelBooking.feature** - End-to-End Flow
**Scenarios:** 2  
**Purpose:** Complete booking workflow from login to cancellation
**Covered Tests:**
- ⚠️ Full booking cycle → Some steps failing
- ⚠️ Multi-step validation → Assertion failures

---

## 🔧 Step Definitions Overview

### **E2EHotelBooking.java** (809 lines)
**Primary** step definitions for booking operations

**Key Methods:**
- `User_authenticates_as_admin_credentials()` - Login step
- `User_stores_the_authentication_token()` - Token handling
- `User_enters_search_criteria_with_checkin_and_checkout()` - Room search
- `User_creates_a_new_booking_with_following_details_and_submits_the_booking()` - Create booking
- `User_attempts_to_update_booking_ID_with_following_details()` - Update booking
- `User_attempts_to_cancel_booking_with_ID_via_API()` - Cancel booking
- `User_requests_to_get_booking_report_for_data_available()` - Generate reports

**Status:** ⚠️ Partially working - Some negative test cases failing

---

### **LoginAPIStepDefinition.java** (222 lines)
**Authentication** step definitions

**Key Methods:**
- `user_authenticates_as_user()` - Basic login
- `API_response_status_code_should_be()` - Status validation
- `Response_should_contain_a_valid_authentication_token()` - Token validation
- `Response_should_indicate()` - Error message validation
- `API_response_should_contain_token_expiration_time()` - Token expiration

**Status:** ✅ Fully functional and working

---

### **RoomManagementAPI.java** (164 lines)
**Room operations** step definitions

**Key Methods:**
- `User_requests_to_get_list_of_all_rooms()` - List all rooms
- `User_requests_to_get_room_details_for_roomid()` - Get room by ID
- `User_requests_to_check_room_availability_with_checkin_and_checkout()` - Check availability
- `Response_should_contain_available_rooms()` - Validate room list
- `Room_details_should_conform_to_the_room_schema()` - Schema validation

**Status:** ✅ Fully functional and working

---

## 🛠️ Utilities & Helper Classes

### **APIUtility.java**
**Purpose:** Wrapper for Rest Assured API calls
**Methods:**
- `sendPostRequest()` - POST requests
- `sendGetRequest()` - GET requests
- `sendPutRequest()` - PUT requests
- `sendDeleteRequest()` - DELETE requests
- `setRequestHeaders()` - Header configuration
- `setRequestBody()` - Body configuration

---

### **TokenManager.java**
**Purpose:** Handle authentication tokens
**Methods:**
- `storeToken()` - Save token
- `getToken()` - Retrieve token
- `isTokenExpired()` - Check expiration
- `setCookieToken()` - From cookie

---

### **CommonUtilities.java**
**Purpose:** Common helper functions
**Methods:**
- `readProperties()` - Read configuration
- `waitForElement()` - Explicit waits
- `parseJsonResponse()` - JSON parsing
- `validateSchema()` - Schema validation

---

## ⚙️ Configuration Files

### **application.properties**
**Location:** `src/test/resources/config/application.properties`

```properties
# API Configuration
api.baseurl=https://automationintesting.online/api
timeout=5000
retries=3

# Authentication
auth.username=admin
auth.password=password

# Report Configuration
report.location=./reports/
report.format=html
```

---

### **cucumber.properties**
**Cucumber-specific** settings

### **log4j2.xml**
**Logging** configuration for test execution

### **extent-config.xml**
**Extent Report** styling (if applicable)

---

## 📊 Test Results & Metrics

### Current Status (Last Execution)
- **Total Tests:** 49
- **Passed:** 42 ✅
- **Failed:** 7 ❌
- **Success Rate:** 85.7%
- **Duration:** ~171 seconds

### Failing Tests Analysis
| Scenario | Feature | Issue |
|----------|---------|-------|
| 17-19 | createBooking | Missing field validation not returning 400 |
| 23 | checkRoomAvailability | Invalid date range validation |
| 31-32 | updateBookingDetails | Update response validation failing |
| 41 | e2eHotelBooking | Multi-step scenario failure |

### Action Items
- 🔴 **HIGH:** Fix negative test assertions (scenarios 17-23)
- 🟡 **MEDIUM:** Refactor error handling in booking operations
- 🟢 **LOW:** Add comprehensive error message validation

---

## 📚 API Endpoints Tested

| Endpoint | Method | Purpose | Status |
|----------|--------|---------|--------|
| `/auth/login` | POST | User authentication | ✅ |
| `/room` | GET | List all rooms | ✅ |
| `/room/{id}` | GET | Get room details | ✅ |
| `/booking` | POST | Create booking | ⚠️ |
| `/booking/{id}` | GET | Get booking details | ✅ |
| `/booking/{id}` | PUT | Update booking | ⚠️ |
| `/booking/{id}` | DELETE | Cancel booking | ✅ |
| `/report` | GET | Generate booking report | ✅ |

---

## 🐛 Troubleshooting

### Issue: Tests not running
**Solution:**
```bash
# Verify Java installation
java -version

# Verify Maven installation
mvn -version

# Check project structure
mvn validate

# Clean and rebuild
mvn clean install
```

---

### Issue: Cannot find reports
**Solution:**
```bash
# Reports are generated after test execution
# Check these locations:
1. reports/cucumber-report.html (Primary)
2. test-output/index.html (TestNG)
3. Target folder: target/surefire-reports/

# If missing, run tests first:
mvn clean test
```

---

### Issue: Tests failing due to API timeout
**Solution:**
```properties
# Increase timeout in application.properties
timeout=10000  # 10 seconds instead of 5
```

---

### Issue: Authentication token issues
**Check:**
1. Username/password in application.properties
2. API endpoint accessibility
3. Network connectivity
4. Token expiration settings

---

## 📖 Tags Reference

### Feature Tags
- `@HotelBookingAPI` - All tests (Main tag)
- `@Login` - Authentication tests
- `@checkRoomAvailability` - Room availability
- `@getRoomDetails` - Room details
- `@createBooking` - Booking creation
- `@updateBookingDetails` - Booking updates
- `@cancelBooking` - Booking cancellation
- `@getBookingDetail` - Booking retrieval
- `@getBookingReport` - Report generation

### Test Type Tags
- `@positive` - Positive test cases ✅
- `@negative` - Negative test cases (error handling) ❌
- `@smoke` - Quick sanity checks
- `@regression` - Full regression suite
- `@api` - API testing

---

## 🔐 Best Practices

### 1. **Before Committing Code**
```bash
# Always run full test suite
mvn clean test

# Verify all reports generated
ls -la reports/
```

---

### 2. **For CI/CD Integration**
```bash
# Use CI-friendly command
mvn clean test -DskipTests=false

# Check exit code
echo $?  # 0 = success, non-zero = failure
```

---

### 3. **Test Data Management**
- Avoid hardcoding test data
- Use data tables in feature files
- Store sensitive data in properties files
- Never commit credentials

---

### 4. **Reporting**
- Always check all report formats
- Archive reports for regression analysis
- Use JSON reports for automation
- Share HTML reports with team

---

## 📞 Support & Maintenance

### Code Review Checklist
- [ ] All scenarios have clear tags
- [ ] Step definitions follow naming conventions
- [ ] API endpoints documented
- [ ] Error handling implemented
- [ ] Test reports reviewed
- [ ] Performance acceptable

### Regular Maintenance
- Weekly: Review failing tests
- Monthly: Add new test scenarios
- Quarterly: Refactor and optimize
- Annually: Framework upgrade review

---

## 📄 License & Author

**Project:** Hotel Booking API - Test Automation Suite  
**Created:** 2026  
**Maintained By:** QA Team  
**Framework:** Cucumber BDD with TestNG

---

## ✅ Quick Reference Checklist

- [ ] Java 11+ installed
- [ ] Maven 3.6.0+ installed
- [ ] Project cloned/downloaded
- [ ] `mvn clean test` executed successfully
- [ ] Reports viewed in `reports/cucumber-report.html`
- [ ] All scenarios reviewed
- [ ] Framework structure understood
- [ ] Ready for feature development/testing

---

## 📖 Additional Resources

- [Cucumber Official Documentation](https://cucumber.io/docs/cucumber/)
- [TestNG Documentation](https://testng.org/doc/)
- [Rest Assured Guide](https://rest-assured.io/)
- [Maven Documentation](https://maven.apache.org/guides/)
- [JSON Schema Validation](https://json-schema.org/)

---

**Last Updated:** March 30, 2026  
**Framework Version:** 1.0-SNAPSHOT  

For questions or issues, refer to the detailed sections above or check the test reports for specific failure information.