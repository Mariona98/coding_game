"""Minimal game API used by the challenge runner.

This module defines a tiny command-recording DSL that submitted player code can use.
Every action does not execute game logic immediately; instead it appends a token to the
shared `commands` list. The game runner reads that list afterwards and turns each token
into an in-game action.
"""

# Shared command queue: the runner reads these strings in order and performs the moves.
# Example entries: "MOVE_RIGHT", "TOTEM_ATTACK", "ADD".
commands = []

# Tracks whether we are currently inside a hero.command(...) block.
# Totems are only allowed to issue commands while this flag is True.
_totem_program_active = False

# Legacy/incomplete inventory state kept for compatibility with the API shape.
# The actual item payload is not implemented in this file yet.
inventory = []

# Unused placeholder name kept to match the expected public API surface.
name = ""

level_data = None
objects = []
heroX =0
heroY =0

class Hero:
    """Represents the player-controlled hero that can issue actions on the map."""

    def command(self, function):
        """Run a player-defined function as a single scripted command block.

        This is the main entry point for code submitted by the player: it starts a program
        context, calls the provided function, and then closes that context. The runner
        can later parse the generated command tokens and execute them in order.
        """
        global _totem_program_active
        if not callable(function):
            raise TypeError("hero.command expects a function defined with def.")

        # Mark the start of a program so totem commands are allowed.
        commands.append("TOTEM_PROGRAM")
        _totem_program_active = True
        try:
            function()
        finally:
            # Always end the command block even if the player code raises an exception.
            _totem_program_active = False
            commands.append("TOTEM_PROGRAM_END")

    def moveRight(self):
        """Queue a hero move one tile to the right."""
        global heroX
        heroX += 1
        commands.append("MOVE_RIGHT")

    def moveLeft(self):
        """Queue a hero move one tile to the left."""
        global heroX
        heroX -= 1
        commands.append("MOVE_LEFT")

    def moveUp(self):
        """Queue a hero move one tile upward."""
        global heroY
        heroY -= 1
        commands.append("MOVE_UP")

    def moveDown(self):
        """Queue a hero move one tile downward."""
        global heroY
        heroY += 1
        commands.append("MOVE_DOWN")

    def attack(self):
        """Queue a melee attack from the hero."""
        commands.append("ATTACK")
        
    def getObjectId(self,DIR):
        lookx = heroX
        looky = heroY
        if DIR == "UP":
            looky -= 1
        elif DIR == "DOWN":
            looky += 1
        elif DIR == "LEFT":
            lookx -= 1
        elif DIR == "RIGHT":
            lookx += 1

        for obj in level_data.get("objects", []):
            if lookx == obj.get("x") and looky == obj.get("y") :
                return obj.get("id", None)
        
        


class Totem:
    """Represents a magical structure that can only act while a hero program is active."""

    @staticmethod
    def _require_program():
        """Ensure the current code is running inside hero.command(...)."""
        if not _totem_program_active:
            raise RuntimeError(
                "The totem only accepts commands inside a function called with hero.command()."
            )

    def moveRight(self):
        """Queue a totem move one tile to the right, but only in a valid program block."""
        self._require_program()
        commands.append("TOTEM_MOVE_RIGHT")

    def moveLeft(self):
        """Queue a totem move one tile to the left, but only in a valid program block."""
        self._require_program()
        commands.append("TOTEM_MOVE_LEFT")

    def moveUp(self):
        """Queue a totem move one tile upward, but only in a valid program block."""
        self._require_program()
        commands.append("TOTEM_MOVE_UP")

    def moveDown(self):
        """Queue a totem move one tile downward, but only in a valid program block."""
        self._require_program()
        commands.append("TOTEM_MOVE_DOWN")

    def attack(self):
        """Queue a totem attack, but only during a hero-controlled command block."""
        self._require_program()
        commands.append("TOTEM_ATTACK")


class Inventory:
    """Thin API wrapper for inventory manipulations.

    The original design intended to add/remove items by appending item-specific commands,
    but the actual item value is intentionally left commented out. The class is kept as a
    placeholder so submitted code can call inventory.append() / inventory.remove().
    """

    def append(self):
        """Record an inventory add action.

        In a fuller version this would also include the item being added.
        """
        commands.append("ADD")
        # commands.append(item)

    def remove(self):
        """Record an inventory remove/drop action.

        In a fuller version this would also include the item being removed.
        """
        commands.append("DROP")
        # commands.append(item)


# Create the shared API objects used by both the game runner and submitted code.
# Each script imports this same module, so all generated commands are collected in one place.
inventory = Inventory()
hero = Hero()
totem = Totem()


