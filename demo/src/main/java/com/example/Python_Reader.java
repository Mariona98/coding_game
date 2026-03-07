package com.example;

import java.io.*;
import java.util.*;

public class Python_Reader {

   public static List<Level_1_Commands> runPython(String code) throws Exception {

    // create temporary script file
    File script = new File("script.py");

    try (FileWriter writer = new FileWriter(script)) {
        writer.write(code);
    }

    System.out.println("Script saved to: " + script.getAbsolutePath());

 File workingDir = new File("C:\\Users\\pittm\\OneDrive\\Desktop\\Paper");

ProcessBuilder pb = new ProcessBuilder(
        "py",
        "coding_game\\demo\\src\\main\\java\\com\\example\\run_level.py",
        "script.py"
);

pb.directory(workingDir);

    Process process = pb.start();

    BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream())
    );

    List<Level_1_Commands> commands = new ArrayList<>();

    String line;

    while ((line = reader.readLine()) != null) {
        System.out.println("PYTHON OUTPUT: " + line);
        commands.add(Level_1_Commands.valueOf(line));
    }
    BufferedReader errorReader =
        new BufferedReader(new InputStreamReader(process.getErrorStream()));

String errorLine;
while ((errorLine = errorReader.readLine()) != null) {
    System.out.println("PYTHON ERROR: " + errorLine);
}

    process.waitFor();

    return commands;
}

}