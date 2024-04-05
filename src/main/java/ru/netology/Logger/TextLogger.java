package ru.netology.Logger;

import java.time.LocalDateTime;

public class TextLogger implements Logger {
    @Override
    public void log(String message, LoggerEnum messageType) {
        System.out.println("[" + LocalDateTime.now() + "] " + messageType + " -> " + message + ".\n");
    }
}
