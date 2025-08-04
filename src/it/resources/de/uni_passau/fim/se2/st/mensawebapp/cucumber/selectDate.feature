Feature: Selection of Data
    Scenario: Selecting 21.12.2023
        Given I open the webapp for selecting date
        When I choose date-selection
        And I input 21.12.2023 in the date field
        Then I get the date 21.12.2023
