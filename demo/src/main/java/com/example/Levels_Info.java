package com.example;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Levels_Info {

  private Level_1_Mechanics[] levels;//arraylist of levels , added later on?
  private int totalStars;
  private int map;
  private ArrayList<String> name;
  private int level;

  
      public Levels_Info() {
          
          this.totalStars = 0 ;
          this.map = 1;
          this.name = new ArrayList<>();
          this.level=0;
      }

        public int getLevels() {
            return level;
        }
        public void setLevels(int level) {
            this.level = level;
        }
        public ArrayList<String> getName() {
            return name;
        }
        public void setName(ArrayList<String> name) {
            this.name = name;
        }
        public int getTotalStars() {
            return totalStars;
        }
        public void setTotalStars(int totalStars) {
            this.totalStars = totalStars;
        }
        public int getMap() {
            return map;
        }
        public void setMap(int map) {
            this.map = map;
        }
        public int getLevelStars(int levelIndex) {
            if (levels != null && levelIndex >= 0 && levelIndex < levels.length) {
                return levels[levelIndex].getStars();
            }
            return 0;
        }
        public void setLevelStars(int levelIndex, int stars) {
            if (levels != null && levelIndex >= 0 && levelIndex < levels.length) {
                levels[levelIndex].setStars(stars);
            }
        }
public void loadGame(String filename) {
        try {
            FileReader reader = new FileReader(filename);
            StringBuilder content = new StringBuilder();
            int character;
            
            while ((character = reader.read()) != -1) {
                content.append((char) character);
            }
            reader.close();
            
            // Parse the content
            String[] lines = content.toString().split("\n");
            for (String line : lines) {
                if (line.startsWith("totalStars:")) {
                    totalStars = Integer.parseInt(line.substring(11));
                } else if (line.startsWith("map:")) {
                    map = Integer.parseInt(line.substring(4));
                } else if (line.startsWith("level:")) {
                    level = Integer.parseInt(line.substring(6));
                } else if (line.startsWith("names:")) {
                    String[] namesArray = line.substring(6).split(",");
                    name = new ArrayList<>();
                    for (String n : namesArray) {
                        name.add(n);
                    }
                }
            }
            
            System.out.println("Game loaded successfully from: " + filename);
            
        } catch (IOException e) {
            System.out.println("Error loading game: " + e.getMessage());
        }
    }


}
