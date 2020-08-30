# We currently filter on @minecraft to run the minecraft client
@minecraft
Feature: Testing Auto World Creation

  Scenario: The player can join a world
    Given the player is in a world called "Test World 1"

    When the player switches to slot 1
    Then the player is holding a "minecraft:diamond_sword"

    When the player switches to slot 2
    Then the player is holding nothing

  Scenario: The player can join another world
    Given the player is in a world called "Test World 2"
    When the player switches to slot 1
    Then the player is holding nothing
