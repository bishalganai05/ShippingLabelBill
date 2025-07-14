package com.bishal.slb.ShippingLabelBill.scheduler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GitAutoCommitScheduler {

    private static final String PROJECT_SRC_PATH = "src/main/java";

    @Scheduled(fixedRate = 60*2*1000) // 6 hours
    public void autoCommitFiles() {
        File srcFolder = new File(PROJECT_SRC_PATH);
        if (!srcFolder.exists()) {
            System.out.println("Source folder not found: " + srcFolder.getAbsolutePath());
            return;
        }

        List<File> packages = Arrays.stream(Objects.requireNonNull(srcFolder.listFiles(File::isDirectory)))
                .toList();

        for (File pkg : packages) {
            Optional<File> selectedFile = pickRandomJavaFile(pkg);
            selectedFile.ifPresent(this::commitToGit);
        }
    }

    private Optional<File> pickRandomJavaFile(File packageDir) {
        File[] javaFiles = packageDir.listFiles((dir, name) -> name.endsWith(".java"));
        if (javaFiles != null && javaFiles.length > 0) {
            return Optional.of(javaFiles[new Random().nextInt(javaFiles.length)]);
        }
        return Optional.empty();
    }

    private void commitToGit(File file) {
        try {
            String commitMessage = "Auto-committing file: " + file.getName();

            ProcessBuilder gitAdd = new ProcessBuilder("git", "add", file.getPath());
            ProcessBuilder gitCommit = new ProcessBuilder("git", "commit", "-m", commitMessage);
            ProcessBuilder gitPush = new ProcessBuilder("git", "push");

            File projectRoot = new File(System.getProperty("user.dir"));
            gitAdd.directory(projectRoot);
            gitCommit.directory(projectRoot);
            gitPush.directory(projectRoot);

            runProcess(gitAdd);
            runProcess(gitCommit);
            runProcess(gitPush);

            System.out.println("Committed: " + file.getName());
        } catch (Exception e) {
            System.err.println("Git commit failed for file " + file.getName() + ": " + e.getMessage());
        }
    }

    private void runProcess(ProcessBuilder builder) throws IOException, InterruptedException {
        Process process = builder.inheritIO().start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command failed with exit code " + exitCode);
        }
    }
}
