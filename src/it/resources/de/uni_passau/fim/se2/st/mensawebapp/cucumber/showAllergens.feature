Feature: Show all possible Allergens in Selector.

    Scenario: Show all possible Allergens in Selector.
        Given I open the webapp for show allergens
        When I choose allergens-selection
        Then I get all possible allergens
