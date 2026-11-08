package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Runs the player's Python program in a temporary directory and converts the
 * commands printed by the support runner into Java game commands.
 *
 * <p>The current protocol is batch-based: Python runs to completion and prints
 * commands, then this class returns those commands to the game. It does not
 * provide live communication for Python to request information from the game
 * while the Python program is running.</p>
 */
public class Python_Reader {

    /** Maximum amount of non-command process output retained for error messages. */
    private static final int MAX_ERROR_OUTPUT_LENGTH = 16_384;

    // Add a level number here when its Python scripts should be allowed to scan.
    private static final int[] SCANNABLE_LEVELS = {3};

    // Legacy placeholder for player item input; this field is not used by runPython.
    public String item;

    /**
     * Executes player code and returns each valid Level_Commands token emitted
     * by run_level.py. Python stdout is treated as the command protocol; text
     * that is not a command is collected as diagnostic output.
     *
     * @param code Python source entered by the player
     * @return commands in the order printed by the Python runner
     * @throws IOException if temporary files or process streams cannot be used
     * @throws InterruptedException if this Java thread is interrupted while waiting
     * @throws PythonExecutionException if setup, sandboxing, or Python execution fails
     */
    public static List<Level_Commands> runPython(String code)
        throws IOException, InterruptedException, PythonExecutionException {
        return runPython(code, -1, null);
    }

    /**
     * Runs player code with scan details only when the selected level allows scanning.
     *
     * @param code Python source entered by the player
     * @param levelNumber selected level number, used to check scan permission
     * @param levelData active map whose size and enemy positions are sent to Python
     * @return commands in the order printed by the Python runner
     * @throws IOException if temporary files or process streams cannot be used
     * @throws InterruptedException if this Java thread is interrupted while waiting
     * @throws PythonExecutionException if setup, sandboxing, or Python execution fails
     */
    public static List<Level_Commands> runPython(
        String code,
        int levelNumber,
        GameLevelData levelData
    ) throws IOException, InterruptedException, PythonExecutionException {
        // Use monotonic time for durations so clock adjustments do not affect timings.
        long totalStartedAt = System.nanoTime();
        long stageStartedAt = totalStartedAt;

        // Sandy applies the security restrictions to the submitted Python program.
        String sandyExecutable = resolveSandyExecutable();
        logTiming("Resolve Sandy", stageStartedAt);

        // Locate the interpreter that Sandy will run.
        stageStartedAt = System.nanoTime();
        Path pythonExecutable = resolvePythonExecutable();
        logTiming("Locate Python", stageStartedAt);

        // The sandbox configuration needs the interpreter directory for execution.
        Path pythonDirectory = pythonExecutable.getParent();
        if (pythonDirectory == null) {
            throw new PythonExecutionException(
                "Could not determine the Python installation directory."
            );
        }

        // Keep this run's scripts separate from the project and other executions.
        Path workingDirectory = Files.createTempDirectory("coders-haven-python-");
        Path userScript = workingDirectory.resolve("script.py");
        Path runnerScript = workingDirectory.resolve("run_level.py");
        Path gameApi = workingDirectory.resolve("game_api.py");

        try {
            stageStartedAt = System.nanoTime();
            // Write the player's source and copy the trusted helper scripts beside it.
            Files.write(userScript, code.getBytes(StandardCharsets.UTF_8));
            copyPythonResource("/com/example/run_level.py", runnerScript);
            copyPythonResource("/com/example/game_api.py", gameApi);

            // Create Sandy's allow-list and resource limits for this execution.
            String sandboxConfig = createSandboxConfig(
                pythonDirectory,
                workingDirectory
            );
            // Pass arguments separately so paths containing spaces stay intact.
            List<String> command = new ArrayList<>();
            command.add(sandyExecutable);
            command.add("-s");
            command.add(sandboxConfig);
            command.add("-q");
            command.add("-x");
            command.add(pythonExecutable.toString());
            command.add("-B");
            command.add(runnerScript.toString());
            command.add(userScript.toString());
            if (isScannableLevel(levelNumber)) {
                if (levelData == null) {
                    throw new PythonExecutionException(
                        "Cannot provide scan data without active level data."
                    );
                }

                // Pass the current map snapshot as one JSON argument. Coordinates
                // come from this run's level instance, including randomized enemies.
                command.add("--scan-data");
                command.add(createScanDataJson(levelData));
            }
            logTiming("Prepare scripts and sandbox", stageStartedAt);

            // Run from the temporary directory and combine stderr and stdout so all
            // process output can be read from one stream.
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(workingDirectory.toFile());
            processBuilder.redirectErrorStream(true);

            // Start the sandboxed process and report setup failures explicitly.
            final Process process;
            stageStartedAt = System.nanoTime();
            try {
                process = processBuilder.start();
                logTiming("Start Sandy process", stageStartedAt);
            } catch (IOException e) {
                if ("sandy.exe".equals(sandyExecutable)) {
                    throw new PythonExecutionException(
                        "Sandy is required to run game code. Install sandy.exe "
                            + "and add it to PATH, or set SANDY_EXE to its full path."
                    );
                }
                throw new PythonExecutionException(
                    "Could not start Sandy at " + sandyExecutable + ": "
                        + e.getMessage()
                );
            }

            // Separate recognized game commands from all other Python output.
            List<Level_Commands> commands = new ArrayList<>();
            StringBuilder errorOutput = new StringBuilder();
            stageStartedAt = System.nanoTime();
            // Continuously drain output so the child cannot block on a full output buffer.
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    process.getInputStream(),
                    StandardCharsets.UTF_8
                )
            )) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("SANDBOX OUTPUT: " + line);
                    if (line.trim().isEmpty()) {
                        // Blank lines are neither valid commands nor useful diagnostics.
                        continue;
                    }

                    try {
                        // The runner prints enum names such as MOVE_UP and ATTACK.
                        commands.add(Level_Commands.valueOf(line.trim()));
                    } catch (IllegalArgumentException e) {
                        // Player print statements and errors are not command tokens.
                        // Keep a bounded diagnostic buffer in case execution fails.
                        if (errorOutput.length() < MAX_ERROR_OUTPUT_LENGTH) {
                            errorOutput.append(line)
                                .append(System.lineSeparator());
                        }
                    }
                }
            }

            // Output has reached EOF; obtain the process exit status.
            int exitCode = process.waitFor();
            logTiming("Wait for sandbox and read output", stageStartedAt);
            if (exitCode != 0) {
                // Prefer the process's diagnostic output, with an exit-code fallback.
                String message = errorOutput.toString().trim();
                if (message.isEmpty()) {
                    message = "Sandboxed Python exited with code " + exitCode + ".";
                }
                throw new PythonExecutionException(message);
            }

            // GameScreen receives the complete command list after Python has exited.
            return commands;
        } finally {
            // Remove all files created for this invocation, including after failure.
            stageStartedAt = System.nanoTime();
            Files.deleteIfExists(userScript);
            Files.deleteIfExists(runnerScript);
            Files.deleteIfExists(gameApi);
            Files.deleteIfExists(workingDirectory);
            logTiming("Cleanup", stageStartedAt);
            logTiming("Total Python run", totalStartedAt);
        }
    }

    /** Checks whether a level number is present in the opt-in scan-level array. */
    private static boolean isScannableLevel(int levelNumber) {
        for (int scannableLevel : SCANNABLE_LEVELS) {
            if (scannableLevel == levelNumber) {
                return true;
            }
        }
        return false;
    }

    /**
     * Encodes only numeric map dimensions and enemy coordinates, so no string
     * escaping is needed and the result can safely be passed as one process argument.
     */
    private static String createScanDataJson(GameLevelData levelData) {
        StringBuilder json = new StringBuilder();
        json.append("{\"width\":")
            .append(levelData.getWidth())
            .append(",\"height\":")
            .append(levelData.getHeight())
            .append(",\"enemies\":[");

        boolean firstEnemy = true;
        for (GameLevelData.LevelObject object : levelData.getLevelObjects()) {
            if (object.getType() != GameLevelData.LevelObject.Type.ENEMY) {
                continue;
            }
            if (!firstEnemy) {
                json.append(',');
            }
            json.append('[')
                .append(object.getX())
                .append(',')
                .append(object.getY())
                .append(']');
            firstEnemy = false;
        }
        return json.append("]}").toString();
    }

    /** Prints elapsed time for one setup or execution stage. */
    private static void logTiming(String stage, long startedAt) {
        long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(
            System.nanoTime() - startedAt
        );
        System.out.println("[Python timing] " + stage + ": " + elapsedMillis + " ms");
    }

    /**
     * Finds Sandy using SANDY_EXE, known project-relative paths, then PATH.
     */
    private static String resolveSandyExecutable()
        throws PythonExecutionException {
        String configuredPath = System.getenv("SANDY_EXE");
        if (configuredPath != null && !configuredPath.trim().isEmpty()) {
            // An explicit but invalid setting is an error, not a reason to use another copy.
            Path executable = Path.of(configuredPath).toAbsolutePath().normalize();
            if (!Files.isRegularFile(executable)) {
                throw new PythonExecutionException(
                    "SANDY_EXE does not point to a file: " + executable
                );
            }
            return executable.toString();
        }

        // First check the project demo directory as the current working directory.
        Path projectLocalExecutable = Path.of("tools", "sandy.exe")
            .toAbsolutePath()
            .normalize();
        if (Files.isRegularFile(projectLocalExecutable)) {
            return projectLocalExecutable.toString();
        }

        // Also support launching while the parent workspace is the working directory.
        Path parentWorkspaceExecutable = Path.of(
            "coding_game",
            "demo",
            "tools",
            "sandy.exe"
        ).toAbsolutePath().normalize();
        if (Files.isRegularFile(parentWorkspaceExecutable)) {
            return parentWorkspaceExecutable.toString();
        }

        // Let Windows search PATH; startup reports a clear error if it is not found.
        return "sandy.exe";
    }

    /**
     * Uses PYTHON_EXE if configured; otherwise asks the Windows "py" launcher
     * for the full path of its selected Python interpreter.
     */
    private static Path resolvePythonExecutable()
        throws IOException, InterruptedException, PythonExecutionException {
        String configuredPython = System.getenv("PYTHON_EXE");
        if (configuredPython != null && !configuredPython.trim().isEmpty()) {
            // Do not silently ignore an invalid explicit interpreter setting.
            Path executable = Path.of(configuredPython).toAbsolutePath().normalize();
            if (!Files.isRegularFile(executable)) {
                throw new PythonExecutionException(
                    "PYTHON_EXE does not point to a file: " + executable
                );
            }
            return executable;
        }

        // Capture resolver output in a file so waiting for the process cannot
        // deadlock because its stdout pipe is full.
        Path resolverOutput = Files.createTempFile(
            "coders-haven-python-resolver-",
            ".txt"
        );
        try {
            Process resolver;
            try {
                // Print only the interpreter path; Python_Reader validates it below.
                resolver = new ProcessBuilder(
                    "py",
                    "-c",
                    "import sys; print(sys.executable)"
                )
                    .redirectErrorStream(true)
                    .redirectOutput(resolverOutput.toFile())
                    .start();
            } catch (IOException e) {
                throw new PythonExecutionException(
                    "Could not find Python. Install the Python launcher or set "
                        + "PYTHON_EXE to the full path of python.exe."
                );
            }

            // Avoid hanging indefinitely if the launcher stalls.
            if (!resolver.waitFor(10, TimeUnit.SECONDS)) {
                resolver.destroyForcibly();
                throw new PythonExecutionException(
                    "Timed out while locating Python. Set PYTHON_EXE to python.exe."
                );
            }
            // Read the path reported by the launcher.
            String output = new String(
                Files.readAllBytes(resolverOutput),
                StandardCharsets.UTF_8
            ).trim();
            if (resolver.exitValue() != 0) {
                throw new PythonExecutionException(
                    "Could not locate Python. Set PYTHON_EXE to the full path "
                        + "of python.exe.\n" + output
                );
            }

            // A successful launcher result must still point to a real file.
            Path executable = Path.of(output).toAbsolutePath().normalize();
            if (!Files.isRegularFile(executable)) {
                throw new PythonExecutionException(
                    "The Python launcher returned an invalid executable path: "
                        + output
                );
            }
            return executable;
        } finally {
            // The resolver's output is temporary regardless of success or failure.
            Files.deleteIfExists(resolverOutput);
        }
    }

    /** Copies a bundled Python helper into the temporary execution directory. */
    private static void copyPythonResource(String resourcePath, Path destination)
        throws IOException, PythonExecutionException {
        try (java.io.InputStream resource =
            Python_Reader.class.getResourceAsStream(resourcePath)) {
            // A missing bundled dependency must stop execution with a clear error.
            if (resource == null) {
                throw new PythonExecutionException(
                    "Could not find required Python support file: " + resourcePath
                );
            }
            // try-with-resources closes the input stream after the copy completes.
            Files.copy(resource, destination);
        }
    }

    /**
     * Builds Sandy's TOML policy: Python can read/execute from its installation
     * and use the temporary work directory, but cannot spawn child processes.
     * The policy also limits runtime, memory, and process count.
     */
    private static String createSandboxConfig(
        Path pythonDirectory,
        Path workingDirectory
    ) throws PythonExecutionException {
        // Quote both paths so spaces and other ordinary path characters are preserved.
        String writableDirectory = tomlString(workingDirectory.toString());
        String pythonHome = tomlString(pythonDirectory.toString());

        return "[sandbox]\n"
            + "token = 'appcontainer'\n"
            + "workdir = " + writableDirectory + "\n"
            + "\n"
            + "[allow.deep]\n"
            + "read = [" + pythonHome + ", " + writableDirectory + "]\n"
            + "execute = [" + pythonHome + "]\n"
            + "\n"
            + "[privileges]\n"
            + "child_processes = false\n"
            + "\n"
            + "[limit]\n"
            + "timeout = 30\n"
            + "memory = 512\n"
            + "processes = 1\n";
    }

    /** Quotes a value using TOML's literal multiline-string syntax. */
    private static String tomlString(String value)
        throws PythonExecutionException {
        // Three apostrophes would close the TOML delimiter and corrupt the policy.
        if (value.contains("'''")) {
            throw new PythonExecutionException(
                "Sandbox paths cannot contain three consecutive apostrophes."
            );
        }
        return "'''" + value + "'''";
    }

    /** Checked failure type for Python setup, sandboxing, and execution errors. */
    public static class PythonExecutionException extends Exception {
        private static final long serialVersionUID = 1L;

        /** Creates an exception carrying the diagnostic shown to the caller. */
        public PythonExecutionException(String output) {
            super(output);
        }
    }
}
