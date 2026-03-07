import sys
import game_api

filename = sys.argv[1]

with open(filename) as f:
    code = f.read()

# execute player code
exec(code)

# print commands recorded by the hero API
for cmd in game_api.commands:
    print(cmd)