Feature: Show all possible Tags in Selector.

    Scenario: Show all possible Tags in Selector.
        Given I open the webapp for show tags
        When I choose tag-selection
        Then I get all possible tags
