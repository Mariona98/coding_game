"""Example solution for Level 3's randomly placed enemies."""

from collections import deque
from game_api import hero


# Keep these values in sync with Level_3_Data if the Level 3 map or hero start changes.
MAP_WIDTH = 12
MAP_HEIGHT = 12
hero_position = (3, 5)

# Scanning returns (enemy count, [(x, y), ...]). The positions are in the same
# order as the Java level objects, which is also the order used by hero.attack().
enemy_count, scanned_enemies = hero.scan()
enemies = [tuple(position) for position in scanned_enemies]
if enemy_count != len(enemies):
    raise RuntimeError("The scan count does not match the scanned enemy positions.")


# Directions match the game grid: x grows to the right and y grows downward.
DIRECTIONS = (
    (1, 0, "moveRight"),
    (-1, 0, "moveLeft"),
    (0, -1, "moveUp"),
    (0, 1, "moveDown"),
)


def adjacent_enemy(position, remaining_enemies):
    """Return the first living enemy next to a tile, matching Java's attack order."""
    x, y = position
    for enemy in remaining_enemies:
        enemy_x, enemy_y = enemy
        if abs(x - enemy_x) + abs(y - enemy_y) == 1:
            return enemy
    return None


def path_to_enemy(position, remaining_enemies):
    """Find a shortest route to any tile from which an enemy can be attacked."""
    queue = deque([position])
    previous = {position: None}
    move_used = {}

    while queue:
        current = queue.popleft()
        victim = adjacent_enemy(current, remaining_enemies)
        if victim is not None:
            path = []
            while previous[current] is not None:
                path.append(move_used[current])
                current = previous[current]
            path.reverse()
            return path, victim

        x, y = current
        for dx, dy, method_name in DIRECTIONS:
            next_position = (x + dx, y + dy)
            next_x, next_y = next_position
            if not (0 <= next_x < MAP_WIDTH and 0 <= next_y < MAP_HEIGHT):
                continue
            if next_position in previous:
                continue
            previous[next_position] = current
            move_used[next_position] = method_name
            queue.append(next_position)

    raise RuntimeError("No path to a remaining enemy was found.")


# The map is fully walkable. Recompute the nearest attack route after each
# defeat, since an attack removes the first adjacent enemy in Java's object order.
while enemies:
    movement_path, defeated_enemy = path_to_enemy(hero_position, enemies)
    for method_name in movement_path:
        getattr(hero, method_name)()

    # Update the planned position from the actual directions just issued.
    for method_name in movement_path:
        for move_dx, move_dy, candidate_method in DIRECTIONS:
            if candidate_method == method_name:
                hero_position = (
                    hero_position[0] + move_dx,
                    hero_position[1] + move_dy,
                )
                break

    # Every generated route ends next to a living enemy, so this attack removes one.
    hero.attack()
    enemies.remove(defeated_enemy)