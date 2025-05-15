Feature: client calls test
    Scenario: client makes call to GET /test
        When the client calls /test
        Then the client receives status code of 200
