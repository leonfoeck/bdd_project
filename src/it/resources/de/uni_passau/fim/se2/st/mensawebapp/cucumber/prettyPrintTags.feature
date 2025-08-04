Feature: Pretty print the tags.
    Scenario: Pretty print the tags.
        Given I open the webapp for pretty printing tags
        When I clean the date field for pretty print tags
        And I select the date 5 December 2022 for pretty print tags
        And I press the submit button for pretty print tags
        Then The tags for the Cannelloni should be pretty looking
