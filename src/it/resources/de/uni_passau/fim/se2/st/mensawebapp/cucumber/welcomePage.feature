Feature: Welcome page

    Scenario: Show menu for today when arriving on the page.
        Given I open the webapp for welcome page
        Then I see the date for today in the date field
        And I should see the menu for today
