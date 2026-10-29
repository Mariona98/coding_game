package com.example;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Levels_Info {

    private int map;
    private int levels;
    private ArrayList<Integer> passedLevels;
    private int steps;
    private final Map<Integer, Integer> bestStepsByLevel;
    private final Map<Integer, Integer> bestLinesByLevel;
    private final ArrayList<Integer> goldLevels;

    public Levels_Info() {
        this.map = 1;
        this.levels = 0;
        this.passedLevels = new ArrayList<>();
        this.steps = 0;
        this.bestStepsByLevel = new HashMap<>();
        this.bestLinesByLevel = new HashMap<>();
        this.goldLevels = new ArrayList<>();
    }

  
    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }


    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }


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

    public void awardGold(int level) {
        if (!goldLevels.contains(level)) {
            goldLevels.add(level);
        }
    }

    public boolean hasGold(int level) {
        return goldLevels.contains(level);
    }

    public Integer getBestSteps(int level) {
        return bestStepsByLevel.get(level);
    }

    public boolean recordBestSteps(int level, int stepCount) {
        if (stepCount < 0) {
            throw new IllegalArgumentException("Step count cannot be negative.");
        }
        Integer currentBest = bestStepsByLevel.get(level);
        if (currentBest == null || stepCount < currentBest) {
            bestStepsByLevel.put(level, stepCount);
            return true;
        }
        return false;
    }

    public Integer getBestLines(int level) {
        return bestLinesByLevel.get(level);
    }

    public boolean recordBestLines(int level, int lineCount) {
        if (lineCount < 0) {
            throw new IllegalArgumentException("Line count cannot be negative.");
        }
        Integer currentBest = bestLinesByLevel.get(level);
        if (currentBest == null || lineCount < currentBest) {
            bestLinesByLevel.put(level, lineCount);
            return true;
        }
        return false;
    }


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


    public void saveGame(String filename) {

        try (FileWriter writer = new FileWriter(filename)) {

            // Each field is stored on a separate line for simple manual inspection.
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
            writer.write("goldLevels:");
            for (int i = 0; i < goldLevels.size(); i++) {
                if (i > 0) {
                    writer.write(",");
                }
                writer.write(String.valueOf(goldLevels.get(i)));
            }
            writer.write("\n");
            writer.write("steps:" + steps + "\n");
            writer.write("bestSteps:");
            boolean firstBest = true;
            for (Map.Entry<Integer, Integer> entry : bestStepsByLevel.entrySet()) {
                if (!firstBest) {
                    writer.write(",");
                }
                writer.write(entry.getKey() + "=" + entry.getValue());
                firstBest = false;
            }
            writer.write("\n");
            writer.write("bestLines:");
            boolean firstBestLineCount = true;
            for (Map.Entry<Integer, Integer> entry : bestLinesByLevel.entrySet()) {
                if (!firstBestLineCount) {
                    writer.write(",");
                }
                writer.write(entry.getKey() + "=" + entry.getValue());
                firstBestLineCount = false;
            }
            writer.write("\n");

            System.out.println(
                "Game saved successfully to: " + filename
            );

        } catch (IOException e) {

            System.out.println(
                "Error saving game: " + e.getMessage()
            );
        }
    }


    public void loadGame(String filename) {

        try (FileReader reader = new FileReader(filename)) {

            StringBuilder content = new StringBuilder();
            int character;

            while ((character = reader.read()) != -1) {
                content.append((char) character);
            }

            String[] lines = content.toString().split("\n");

            // Parse known keys; unrecognized lines are ignored for basic compatibility.
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

                } else if (line.startsWith("goldLevels:")) {
                    goldLevels.clear();
                    String data = line.substring("goldLevels:".length());
                    if (!data.isEmpty()) {
                        for (String level : data.split(",")) {
                            goldLevels.add(Integer.parseInt(level.trim()));
                        }
                    }

                } else if (line.startsWith("steps:")) {

                    steps = Integer.parseInt(
                        line.substring(6)
                    );

                } else if (line.startsWith("bestSteps:")) {
                    bestStepsByLevel.clear();
                    String data = line.substring("bestSteps:".length());
                    if (!data.isEmpty()) {
                        for (String record : data.split(",")) {
                            String[] fields = record.split("=", 2);
                            if (fields.length == 2) {
                                bestStepsByLevel.put(
                                    Integer.parseInt(fields[0].trim()),
                                    Integer.parseInt(fields[1].trim())
                                );
                            }
                        }
                    }
                } else if (line.startsWith("bestLines:")) {
                    bestLinesByLevel.clear();
                    String data = line.substring("bestLines:".length());
                    if (!data.isEmpty()) {
                        for (String record : data.split(",")) {
                            String[] fields = record.split("=", 2);
                            if (fields.length == 2) {
                                bestLinesByLevel.put(
                                    Integer.parseInt(fields[0].trim()),
                                    Integer.parseInt(fields[1].trim())
                                );
                            }
                        }
                    }
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