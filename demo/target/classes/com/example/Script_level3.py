from game_api import totem
from game_api import hero


def attack_all_enemies():
    totem.attack()

    for _ in range(2):
        totem.moveRight()
    totem.attack()

    for _ in range(3):
        totem.moveRight()
    totem.attack()

    for _ in range(3):
        totem.moveRight()
    totem.attack()

    for _ in range(2):
        totem.moveDown()
    totem.attack()

    for _ in range(3):
        totem.moveLeft()
    totem.attack()


hero.command(attack_all_enemies)
