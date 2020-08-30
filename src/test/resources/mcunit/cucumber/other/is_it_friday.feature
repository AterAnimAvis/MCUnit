Feature: Is it Friday

  @skip
  Scenario: Testing
    Given today is "Tuesday"
    When I ask whether it's Friday yet
    Then I should be told "No"