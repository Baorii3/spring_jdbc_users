package com.ra2.users.spring_jdbc_users.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class customLogging {
    private final String logDirectory = "logs/";

    public customLogging() {
    }

    public void afegeixLog(String level, String className, String method, String description) throws IOException {
        if (!level.equals("ERROR") && !level.equals("INFO")) return;
        if (className == null) className = "null";
        if (method == null) method = "null";
        if (description == null) description = "null";
        StringBuilder sb = new StringBuilder();
        sb.append(level).append(" - ").append(className).append(" - ").append(method).append(" - ").append(description);
        escriureLogs(sb.toString());
    }

    public void error(String className, String method, String description) throws IOException {
        afegeixLog("ERROR", className, method, description);
        
    }

    public void info(String className, String method, String description) throws IOException {
        afegeixLog("INFO", className, method, description);
    }

    public void createDirectory() throws IOException {
        Path path = Paths.get(logDirectory);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
    public void escriureLogs(String message) throws IOException {
        createDirectory();
        LocalDateTime avui = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaString = avui.format(format);
        String logFileName = "aplicacio_" + avui.toLocalDate().toString() + ".log";
        Path path = Paths.get(logDirectory + logFileName);
        try (var writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write("["+ fechaString + "] ");
            writer.write(message+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
