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


public class Python_Reader {

    private static final int MAX_ERROR_OUTPUT_LENGTH = 16_384;

    public static List<Level_Commands> runPython(String code)
        throws IOException, InterruptedException, PythonExecutionException {
        long totalStartedAt = System.nanoTime();
        long stageStartedAt = totalStartedAt;
        // Execute in a temporary directory, with a narrow filesystem/process sandbox.
        String sandyExecutable = resolveSandyExecutable();
        logTiming("Resolve Sandy", stageStartedAt);

        stageStartedAt = System.nanoTime();
        Path pythonExecutable = resolvePythonExecutable();
        logTiming("Locate Python", stageStartedAt);
        Path pythonDirectory = pythonExecutable.getParent();
        if (pythonDirectory == null) {
            throw new PythonExecutionException(
                "Could not determine the Python installation directory."
            );
        }

        Path workingDirectory = Files.createTempDirectory("coders-haven-python-");
        Path userScript = workingDirectory.resolve("script.py");
        Path runnerScript = workingDirectory.resolve("run_level.py");
        Path gameApi = workingDirectory.resolve("game_api.py");

        try {
            stageStartedAt = System.nanoTime();
            Files.write(userScript, code.getBytes(StandardCharsets.UTF_8));
            copyPythonResource("/com/example/run_level.py", runnerScript);
            copyPythonResource("/com/example/game_api.py", gameApi);

            String sandboxConfig = createSandboxConfig(
                pythonDirectory,
                workingDirectory
            );
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
            logTiming("Prepare scripts and sandbox", stageStartedAt);

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(workingDirectory.toFile());
            processBuilder.redirectErrorStream(true);

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

            List<Level_Commands> commands = new ArrayList<>();
            StringBuilder errorOutput = new StringBuilder();
            stageStartedAt = System.nanoTime();
            // Lines matching enum names become commands; other output is retained as error text.
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
                        continue;
                    }

                    try {
                        commands.add(Level_Commands.valueOf(line.trim()));
                    } catch (IllegalArgumentException e) {
                        if (errorOutput.length() < MAX_ERROR_OUTPUT_LENGTH) {
                            errorOutput.append(line)
                                .append(System.lineSeparator());
                        }
                    }
                }
            }

            int exitCode = process.waitFor();
            logTiming("Wait for sandbox and read output", stageStartedAt);
            if (exitCode != 0) {
                String message = errorOutput.toString().trim();
                if (message.isEmpty()) {
                    message = "Sandboxed Python exited with code " + exitCode + ".";
                }
                throw new PythonExecutionException(message);
            }

            return commands;
        } finally {
            stageStartedAt = System.nanoTime();
            Files.deleteIfExists(userScript);
            Files.deleteIfExists(runnerScript);
            Files.deleteIfExists(gameApi);
            Files.deleteIfExists(workingDirectory);
            logTiming("Cleanup", stageStartedAt);
            logTiming("Total Python run", totalStartedAt);
        }
    }

    private static void logTiming(String stage, long startedAt) {
        long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(
            System.nanoTime() - startedAt
        );
        System.out.println("[Python timing] " + stage + ": " + elapsedMillis + " ms");
    }

    private static String resolveSandyExecutable()
        throws PythonExecutionException {
        String configuredPath = System.getenv("SANDY_EXE");
        if (configuredPath != null && !configuredPath.trim().isEmpty()) {
            Path executable = Path.of(configuredPath).toAbsolutePath().normalize();
            if (!Files.isRegularFile(executable)) {
                throw new PythonExecutionException(
                    "SANDY_EXE does not point to a file: " + executable
                );
            }
            return executable.toString();
        }

        Path projectLocalExecutable = Path.of("tools", "sandy.exe")
            .toAbsolutePath()
            .normalize();
        if (Files.isRegularFile(projectLocalExecutable)) {
            return projectLocalExecutable.toString();
        }

        Path parentWorkspaceExecutable = Path.of(
            "coding_game",
            "demo",
            "tools",
            "sandy.exe"
        ).toAbsolutePath().normalize();
        if (Files.isRegularFile(parentWorkspaceExecutable)) {
            return parentWorkspaceExecutable.toString();
        }

        return "sandy.exe";
    }

    private static Path resolvePythonExecutable()
        throws IOException, InterruptedException, PythonExecutionException {
        String configuredPython = System.getenv("PYTHON_EXE");
        if (configuredPython != null && !configuredPython.trim().isEmpty()) {
            Path executable = Path.of(configuredPython).toAbsolutePath().normalize();
            if (!Files.isRegularFile(executable)) {
                throw new PythonExecutionException(
                    "PYTHON_EXE does not point to a file: " + executable
                );
            }
            return executable;
        }

        Path resolverOutput = Files.createTempFile(
            "coders-haven-python-resolver-",
            ".txt"
        );
        try {
            Process resolver;
            try {
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

            if (!resolver.waitFor(10, TimeUnit.SECONDS)) {
                resolver.destroyForcibly();
                throw new PythonExecutionException(
                    "Timed out while locating Python. Set PYTHON_EXE to python.exe."
                );
            }
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

            Path executable = Path.of(output).toAbsolutePath().normalize();
            if (!Files.isRegularFile(executable)) {
                throw new PythonExecutionException(
                    "The Python launcher returned an invalid executable path: "
                        + output
                );
            }
            return executable;
        } finally {
            Files.deleteIfExists(resolverOutput);
        }
    }

    private static void copyPythonResource(String resourcePath, Path destination)
        throws IOException, PythonExecutionException {
        try (java.io.InputStream resource =
            Python_Reader.class.getResourceAsStream(resourcePath)) {
            if (resource == null) {
                throw new PythonExecutionException(
                    "Could not find required Python support file: " + resourcePath
                );
            }
            Files.copy(resource, destination);
        }
    }

    private static String createSandboxConfig(
        Path pythonDirectory,
        Path workingDirectory
    ) throws PythonExecutionException {
        // Allow Python to run and read its temporary files, but deny child processes.
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

    private static String tomlString(String value)
        throws PythonExecutionException {
        if (value.contains("'''")) {
            throw new PythonExecutionException(
                "Sandbox paths cannot contain three consecutive apostrophes."
            );
        }
        return "'''" + value + "'''";
    }

    public static class PythonExecutionException extends Exception {
        private static final long serialVersionUID = 1L;

        public PythonExecutionException(String output) {
            super(output);
        }
    }
}
