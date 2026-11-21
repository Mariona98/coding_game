
import json
import sys
import game_api

# Keep game_api command list as the single source of recorded player actions.
if len(sys.argv) != 3:
    raise ValueError("Expected a script path and a level snapshot path.")
filename = sys.argv[1]
level_filename = sys.argv[2]

# Keep the JSON snapshot as data.
with open(level_filename, encoding="utf-8") as level_file:
    level_snapshot = json.load(level_file)
game_api.initialize_level(level_snapshot)

with open(filename, encoding="utf-8") as f:
    code = f.read()

# Execute player code inside the Sandy sandbox configured by Python_Reader.
exec(code)

# Print one enum-compatible command per line for the Java process to parse.
for cmd in game_api.commands:
    print(cmd)