Feature: Angular Simple Math
  #In order to solve simple math problems
  #As a user
  #I want to perform simple arithmetic on two numbers
  @test1
  Scenario Outline: Add two numbers
    Given I navigate to the url
    And I have a new calculator
    When I add "<first>" and "<second>" for "<IsAngular>"
    Then the latest result should be "<expResult>" for "<IsAngular>"

    Examples:
      | first | second | expResult | IsAngular |
#      | 2     | 3      | 5         | True      |
#      | -1    | 1      | 1         | True      |
      | 2     | 3      | 5         | False     |
      | -1    | 1      | 1         | False     |

  @test2
  Scenario Outline: Divide two numbers
    Given I navigate to the url
    Given I have a new calculator
    When I divide "<first>" by "<second>" for "<IsAngular>"
    Then the latest result should be "<expectedResult>" for "<IsAngular>"

    Examples:
      | first | second | expectedResult | IsAngular |
#      | 2     | 2      | 1              | True      |
#      | 1     | 0      | Infinity       | True      |
      | 2     | 2      | 1              | False     |
      | 1     | 0      | Infinity       | False     |
