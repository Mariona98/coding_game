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


hero = Hero()