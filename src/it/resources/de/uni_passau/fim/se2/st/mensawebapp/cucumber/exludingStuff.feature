Feature: Excluding Stuff from the Menu
    Scenario: Excluding additives, tags and allergens from the menu
        Given I open webapp to exclude stuff
        When I clean the date field for pretty excluding
        And I select the date 5 December 2022 for excluding
        And I exclude additives, tags and allergens from the menu
        And I press the submit button for pretty excluding
        Then I should have a menu without some additives, tags and allergens
