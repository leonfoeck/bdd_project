Feature: Show all possible Additives in Selector.

    Scenario: Show all possible Additives in Selector.
        Given I open the webapp for show additives
        When I choose additive-selection
        Then I get all possible additives
