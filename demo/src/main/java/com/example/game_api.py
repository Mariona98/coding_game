commands = []


class Hero:

    def moveRight(self):
        commands.append("MOVE_RIGHT")

    def moveLeft(self):
        commands.append("MOVE_LEFT")

    def moveUp(self):
        commands.append("MOVE_UP") 

    def moveDown(self):
        commands.append("MOVE_DOWN")

    def collect(self):
        commands.append("COLLECT")

    # NEW: Opens a chest at the hero's current position.
    def openChest(self):
        commands.append("OPEN_CHEST")

    # NEW: Shoots an arrow upward.
    def shootUp(self):
        commands.append("SHOOT_UP")

    # NEW: Shoots an arrow downward.
    def shootDown(self):
        commands.append("SHOOT_DOWN")

    # NEW: Shoots an arrow to the left.
    def shootLeft(self):
        commands.append("SHOOT_LEFT")

    # NEW: Shoots an arrow to the right.
    def shootRight(self):
        commands.append("SHOOT_RIGHT")


hero = Hero()