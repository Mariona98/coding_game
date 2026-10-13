"""Expose the movement API available to a player's submitted Python program.

Import ``hero`` and call its movement methods; each call records a command for
the Java game to execute after the sandboxed program finishes.
"""

commands = []


class Hero:
    """Record supported movement requests in the shared command list."""

    def moveRight(self):
        """Queue one step to the right."""
        commands.append("MOVE_RIGHT")

    def moveLeft(self):
        """Queue one step to the left."""
        commands.append("MOVE_LEFT")

    def moveUp(self):
        """Queue one step upward."""
        commands.append("MOVE_UP") 

    def moveDown(self):
        """Queue one step downward."""
        commands.append("MOVE_DOWN")

  
# One shared API instance is imported by both the runner and submitted code.
hero = Hero()