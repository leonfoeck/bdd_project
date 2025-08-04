Feature: Pretty print the additives.
    Scenario: Pretty print the additives.
        Given I open the webapp for pretty printing additives
        When I clean the date field for pretty print additives
        And I select the date 5 December 2022 for pretty print additives
        And I press the submit button for pretty print additives
        Then The additives for the Cannelloni should be pretty looking
