# Available commands the player can write
#
# Import the objects the player can use
from game_api import hero, totem, inventory

# Hero movement
hero.moveRight()   # Move the hero 1 tile to the right
hero.moveLeft()    # Move the hero 1 tile to the left
hero.moveUp()      # Move the hero 1 tile upward
hero.moveDown()    # Move the hero 1 tile downward

# Hero attack
hero.attack()      # Attack from the hero's current tile

# Program the totem inside a function called by the hero
# The function must be called with hero.command(function_name)
def move_totem():
    totem.moveRight()  # Move the totem 1 tile to the right
    totem.moveLeft()   # Move the totem 1 tile to the left
    totem.moveUp()     # Move the totem 1 tile up
    totem.moveDown()   # Move the totem 1 tile down
    totem.attack()     # Make the totem attack an enemy

hero.command(move_totem)

# Inventory actions
inventory.append()  # Add an item to the inventory / pick up an item
inventory.remove()  # Remove/drop an item from the inventory

# Extra look-up helper
hero.getObjectId("RIGHT")  # Check the object in front of the hero
hero.getObjectId("LEFT")
hero.getObjectId("UP")
hero.getObjectId("DOWN")
# What each command does:
# - hero.moveRight() / hero.moveLeft() / hero.moveUp() / hero.moveDown(): move the hero one tile in that direction.
# - hero.attack(): attack from the hero's current position.
# - hero.command(function_name): runs a player-defined function and allows the totem to act inside it.
# - totem.moveRight() / totem.moveLeft() / totem.moveUp() / totem.moveDown(): move the totem one tile in that direction.
# - totem.attack(): attack with the totem.
# - inventory.append(): add/pick up an item.
# - inventory.remove(): drop/remove an item.
# - hero.getObjectId("DIRECTION"): returns the id of the object in that direction, if there is one.
