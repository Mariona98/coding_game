"""Sandbox entry point: execute the submitted script and print recorded moves.

Called by Python_Reader with the submitted script path as the first argument.
"""

import sys
import json
import game_api

# Keep game_api's command list as the single source of recorded player actions.
filename = sys.argv[1]

# Java adds scan data only for levels listed in Python_Reader.SCANNABLE_LEVELS.
# It is passed as one JSON argument so dimensions and all enemy coordinates stay together.
if len(sys.argv) == 4 and sys.argv[2] == "--scan-data":
    game_api.configure_scan_data(json.loads(sys.argv[3]))
elif len(sys.argv) != 2:
    raise ValueError("Expected a script path and optional scan data.")

with open(filename) as f:
    code = f.read()

# Execute player code inside the Sandy sandbox configured by Python_Reader.
exec(code)

# Print one enum-compatible command per line for the Java process to parse.
for cmd in game_api.commands:
    print(cmd)