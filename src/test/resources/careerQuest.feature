Feature: employer creates job
    Scenario: employer makes call to /job
        When the employer posts first job to /job$
        Then the employer receives status code of 200

