"""Sandbox entry point: execute the submitted script and print recorded moves.

Called by Python_Reader with the submitted script path as the first argument.
"""

import sys
import game_api

# Keep game_api's command list as the single source of recorded player actions.
filename = sys.argv[1]

with open(filename) as f:
    code = f.read()

# Execute player code inside the Sandy sandbox configured by Python_Reader.
exec(code)

# Print one enum-compatible command per line for the Java process to parse.
for cmd in game_api.commands:
    print(cmd)