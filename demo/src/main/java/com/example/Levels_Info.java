package com.example;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Levels_Info {

    private int map;
    private int levels;
    private ArrayList<Integer> passedLevels;
    private int steps;

    public Levels_Info() {
        this.map = 1;
        this.levels = 0;
        this.passedLevels = new ArrayList<>();
        this.steps = 0;
    }

    // =========================
    // MAP
    // =========================

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    // =========================
    // NUMBER OF LEVELS
    // =========================

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    // =========================
    // PASSED LEVELS
    // =========================

    public ArrayList<Integer> getPassedLevels() {
        return passedLevels;
    }

    public void setPassedLevels(ArrayList<Integer> passedLevels) {
        this.passedLevels = passedLevels;
    }

    public void passLevel(int level) {
        if (!passedLevels.contains(level)) {
            passedLevels.add(level);
        }
    }

    public boolean isLevelPassed(int level) {
        return passedLevels.contains(level);
    }

    // =========================
    // STEPS
    // =========================

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void addStep() {
        steps++;
    }

    public void addSteps(int amount) {
        steps += amount;
    }

    // =========================
    // SAVE GAME
    // =========================

    public void saveGame(String filename) {

        try (FileWriter writer = new FileWriter(filename)) {

            writer.write("map:" + map + "\n");
            writer.write("levels:" + levels + "\n");

            writer.write("passedLevels:");

            for (int i = 0; i < passedLevels.size(); i++) {

                writer.write(
                    String.valueOf(passedLevels.get(i))
                );

                if (i < passedLevels.size() - 1) {
                    writer.write(",");
                }
            }

            writer.write("\n");
            writer.write("steps:" + steps + "\n");

            System.out.println(
                "Game saved successfully to: " + filename
            );

        } catch (IOException e) {

            System.out.println(
                "Error saving game: " + e.getMessage()
            );
        }
    }

    // =========================
    // LOAD GAME
    // =========================

    public void loadGame(String filename) {

        try (FileReader reader = new FileReader(filename)) {

            StringBuilder content = new StringBuilder();
            int character;

            while ((character = reader.read()) != -1) {
                content.append((char) character);
            }

            String[] lines = content.toString().split("\n");

            for (String line : lines) {

                line = line.trim();

                if (line.startsWith("map:")) {

                    map = Integer.parseInt(
                        line.substring(4)
                    );

                } else if (line.startsWith("levels:")) {

                    levels = Integer.parseInt(
                        line.substring(7)
                    );

                } else if (line.startsWith("passedLevels:")) {

                    passedLevels.clear();

                    String data = line.substring(13);

                    if (!data.isEmpty()) {

                        String[] levelArray = data.split(",");

                        for (String level : levelArray) {

                            passedLevels.add(
                                Integer.parseInt(level.trim())
                            );
                        }
                    }

                } else if (line.startsWith("steps:")) {

                    steps = Integer.parseInt(
                        line.substring(6)
                    );
                }
            }

            System.out.println(
                "Game loaded successfully from: " + filename
            );

        } catch (IOException e) {

            System.out.println(
                "Error loading game: " + e.getMessage()
            );
        }
    }
}