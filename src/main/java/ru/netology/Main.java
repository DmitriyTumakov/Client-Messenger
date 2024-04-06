package ru.netology;

import ru.netology.Logger.FileLogger;
import ru.netology.Logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final int STANDART_PORT = 8988;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Logger logger = new FileLogger();
        File settings = new File("settings.txt");

        String host = "netology.homework";
        int port = createSettings(settings);

        Account account = auth();

        Client client = new Client(account, logger, host, port);
        client.start();
    }

    protected static Account auth() {
        System.out.print("Пожалуйста, введите имя: ");
        String name = scanner.next();
        return new Account(name);
    }

    protected static int createSettings(File settings) {
        if (!settings.exists()) {
            try (FileOutputStream fos = new FileOutputStream("settings.txt")) {
                if (settings.createNewFile()) {
                    System.out.println("Файл настроек создан успешно создан.");
                }
                fos.write(String.valueOf(STANDART_PORT).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return STANDART_PORT;
        } else {
            StringBuilder settingsBuilder = new StringBuilder();
            try (FileInputStream fis = new FileInputStream("settings.txt")) {
                int i;
                while ((i = fis.read()) != -1) {
                    settingsBuilder.append((char) i);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (!(settingsBuilder.length() > 5 || settingsBuilder.length() < 4)) {
                return Integer.parseInt(settingsBuilder.toString());
            } else {
                System.out.println("Порт введён не верно");
            }
        }
        return 0;
    }
}
