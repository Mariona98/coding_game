commands = []

inventory=[]

name=""

scan_data = None

def configure_scan_data(data):
    """Make the current level's dimensions and enemy coordinates available to hero.scan()."""
    global scan_data
    scan_data = data

class Hero:
    def scan(self):
        # Coordinates are returned as (x, y) tuples; SCAN is also sent to the game engine.
        if scan_data is None:
            raise RuntimeError("Scanning is only available in configured levels.")
        enemies = [tuple(position) for position in scan_data["enemies"]]
        commands.append("SCAN")
        return len(enemies), enemies

    def moveRight(self):

        commands.append("MOVE_RIGHT")

    def moveLeft(self):
        commands.append("MOVE_LEFT")

    def moveUp(self):
        commands.append("MOVE_UP") 

    def moveDown(self):
        commands.append("MOVE_DOWN")
    def attack(self):
        commands.append("ATTACK")
    

class Inventory:

    def append(self):
        commands.append("ADD")
       # commands.append(item)

    def remove(self):
        commands.append("DROP")
       # commands.append(item)
       


inventory = Inventory()
  
# One shared API instance is imported by both the runner and submitted code.
hero = Hero()