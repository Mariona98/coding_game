# coding_game
A coding game to learn python

## Adding level-specific help

Level 1 and Level 2 both load `GameScene.fxml`; `GameScreen` applies each
level's map, positions, background, dialogue, and command-reference sections
from its `GameLevelData`. To customize the scroll-icon command pane for a
level, add or edit `GameLevelData.HelpSection` entries in that level's
`Level_*_Data` class. The book-icon syntax reference remains shared Python
guidance.

The current shared scene has a fixed 12-by-12 grid. New levels must use those
dimensions unless the scene layout and its dimension check are updated.
