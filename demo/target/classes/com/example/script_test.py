

#1) Baseline sanity check: valid movement still works
from game_api import hero

# Start at (3,5). This path stays on the valid row the level data creates.
hero.moveRight()
hero.moveRight()
hero.moveRight()
hero.moveRight()
hero.moveRight()




#2) Test file read restriction ,passed
from pathlib import Path

# Try to read a file outside the temporary working directory.
p = Path(r"C:\Users\pittm\OneDrive\Desktop\Paper\flow state.txt")

with p.open("r", encoding="utf-8") as f:
    text = f.read()
    print(text[:200])
    





#3) Test file write restriction,passed
from pathlib import Path

# Attempt to write to a location outside the sandboxed work directory.
p = Path(r"C:\Users\pittm\OneDrive\Desktop\Paper\outside_sandbox_test.txt")
p.write_text("sandbox test", encoding="utf-8")
print("written")



#4) Test child process restriction,passed
import subprocess

result = subprocess.run(
    ["cmd", "/c", "echo sandbox-escape"],
    capture_output=True,
    text=True
)
print(result.stdout)


#5) Test OS shell restriction,passed
import os

os.system("echo sandbox-escape")

#6) Test network access restriction,passed
import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.settimeout(3)
s.connect(("example.com", 80))
print("connected")
s.close()

#7) Test timeout / infinite loop restriction
counter = 0
while True:
    counter += 1
    if counter % 1000000 == 0:
        print("still running", counter)
        
#8) Test import-execution restriction,shown only on the console, not in the game output

import os
import sys

print("Python executable:", sys.executable)
print("Current dir:", os.getcwd())
print("Home:", os.path.expanduser("~"))

#9)Test “non-command output” behavior, ignored
 
from game_api import hero

print("THIS IS NOT A COMMAND")
hero.moveRight()
hero.moveLeft()
