package ru.netology;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {
    public void log(String message, LoggerEnum messageType) {
        File log = new File("info.log");
        if (log.exists()) {
            try (FileOutputStream fos = new FileOutputStream(log.getName(), true)) {
                fos.write(("[" + LocalDateTime.now() + "] " + messageType + " -> " + message + ".\n").getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                if (log.createNewFile()) {}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}