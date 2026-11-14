"""Sandbox entry point: load the level snapshot, run player code, print moves.

Python_Reader supplies the submitted script path and JSON level snapshot path.
"""

import json
import sys
import game_api

# Keep game_api's command list as the single source of recorded player actions.
if len(sys.argv) != 3:
    raise ValueError("Expected a script path and a level snapshot path.")
filename = sys.argv[1]
level_filename = sys.argv[2]

# Keep the JSON snapshot as data on game_api; no player-facing setup function is needed.
with open(level_filename, encoding="utf-8") as level_file:
    game_api.level_data = json.load(level_file)

# Make the complete object list directly available through the game API.
game_api.objects = game_api.level_data.get("objects", [])

# Initialize the hero's tracked coordinates after level data has been loaded.
hero_start = next(
    (obj for obj in game_api.objects if obj.get("type") == "HERO"),
    None,
)
if hero_start is None:
    raise ValueError("The level snapshot does not contain a hero object.")
game_api.heroX = hero_start["x"]
game_api.heroY = hero_start["y"]



with open(filename, encoding="utf-8") as f:
    code = f.read()

# Execute player code inside the Sandy sandbox configured by Python_Reader.
exec(code)

# Print one enum-compatible command per line for the Java process to parse.
for cmd in game_api.commands:
    print(cmd)