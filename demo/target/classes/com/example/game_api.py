
#To store the commands
commands = []

# Tracks whether we are currently inside a hero.command(...) block.
# Totems are only allowed to issue commands while this flag is True.
totemactivated = False


inventory = []



level_data = None
objects = []
heroX =0
heroY =0

#initialize the level get the starting coordinates
def initialize_level(snapshot):
    
    global level_data, objects, heroX, heroY
    level_data = snapshot
    objects = level_data.get("objects", [])

    hero_start = None
    for obj in objects:
        if obj.get("type") == "HERO":
            hero_start = obj
            break

    if hero_start is None:
        raise ValueError("The level snapshot does not contain a hero object.")

    heroX = hero_start["x"]
    heroY = hero_start["y"]


class Hero:
    #controls the hero

    def command(self, function):

        global totemactivated
        if not callable(function):
            raise TypeError("hero.command expects a function defined with def.")

        # Mark the start of a program so totem commands are allowed.
        commands.append("TOTEM_PROGRAM")
        totemactivated = True
        try:
            function()
        finally:
            # Always end the command block even if the player code raises an exception.
            totemactivated = False
            commands.append("TOTEM_PROGRAM_END")

    def moveRight(self):
      
        global heroX
        heroX += 1
        commands.append("MOVE_RIGHT")

    def moveLeft(self):
   
        global heroX
        heroX -= 1
        commands.append("MOVE_LEFT")

    def moveUp(self):
     
        global heroY
        heroY -= 1
        commands.append("MOVE_UP")

    def moveDown(self):
   
        global heroY
        heroY += 1
        commands.append("MOVE_DOWN")

    def attack(self):
       
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
    
    
    
    def requireprogram(self):
       
        if not totemactivated:
            raise RuntimeError(
                "The totem only accepts commands inside a function called with hero.command()."
            )

    def moveRight(self):

        self.requireprogram()
        commands.append("TOTEM_MOVE_RIGHT")

    def moveLeft(self):
       
        self.requireprogram()
        commands.append("TOTEM_MOVE_LEFT")

    def moveUp(self):
       
        self.requireprogram()
        commands.append("TOTEM_MOVE_UP")

    def moveDown(self):
        
        self.requireprogram()
        commands.append("TOTEM_MOVE_DOWN")

    def attack(self):
       
        self.requireprogram()
        commands.append("TOTEM_ATTACK")


class Inventory:


    def append(self):
 
        commands.append("ADD")
        # commands.append(item)

    def remove(self):

        commands.append("DROP")
        # commands.append(item)


# Create the shared API objects used by both the game runner and submitted code.
# Each script imports this same module, so all generated commands are collected in one place.
inventory = Inventory()
hero = Hero()
totem = Totem()


