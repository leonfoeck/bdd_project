Feature: Pretty print the allergens.
    Scenario: Pretty print the allergens.
        Given I open the webapp for pretty printing allergens
        When I clean the date field for pretty print
        And I select the date 5 December 2022 for pretty print
        And I press the submit button
        Then The allergens for the Cannelloni should be pretty looking
