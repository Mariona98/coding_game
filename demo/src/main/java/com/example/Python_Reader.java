package com.example;

import java.io.*;
import java.util.*;

public class Python_Reader {

   public static List<Level_1_Commands> runPython(String code)
       throws IOException, InterruptedException, PythonExecutionException {

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
pb.redirectErrorStream(true);

    Process process = pb.start();

    BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream())
    );

    List<Level_1_Commands> commands = new ArrayList<>();
    StringBuilder errorOutput = new StringBuilder();

    String line;

    while ((line = reader.readLine()) != null) {
        System.out.println("PYTHON OUTPUT: " + line);
        if (line.trim().isEmpty()) {
            continue;
        }

        try {
            commands.add(Level_1_Commands.valueOf(line.trim()));
        } catch (IllegalArgumentException e) {
            errorOutput.append(line).append(System.lineSeparator());
        }
    }

    int exitCode = process.waitFor();
    if (exitCode != 0 || errorOutput.length() > 0) {
        throw new PythonExecutionException(errorOutput.toString().trim());
    }

    return commands;
}

   public static class PythonExecutionException extends Exception {
       private static final long serialVersionUID = 1L;

       public PythonExecutionException(String output) {
           super(output);
       }
   }

}