package ru.netology;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    private static final int STANDART_PORT = 8988;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Logger logger = new Logger();
        File settings = new File("settings.txt");

        String host = "netology.homework";
        int port = createSettings(settings);

        Account account = auth();

        if (port != 0) {
            System.out.printf("Выбранный порт сервера: %d\n", port);

            boolean isRunning = true;
            try (Socket clientSocket = new Socket(host, port)) {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out.println(account.getName());
                menu(account);

                new Thread(() -> {
                    while (true) {
                        if (account.isInChat()) {
                            String name = account.getName();
                            String nickName = "[" + account.getName() + "] |> ";
                            String message = scanner.nextLine();
                            if (message.equals("/exit")) {
                                account.setInChat(false);
                                menu(account);
                                out.println(name);
                                out.printf("Пользователь %s вышел из чата.", name);
                            }
                            if (!(message.isEmpty())) {
                                out.println(name);
                                out.println(nickName + message);
                                logger.log("[Я] -> " + message, LoggerEnum.MESSAGE);
                            }
                        }
                    }
                }).start();

                while (isRunning) {
                    if (account.isInChat()) {
                        try {
                            String message = in.readLine();
                            System.out.println(message);
                            logger.log(message, LoggerEnum.MESSAGE);
                        } catch (IOException e) {
                            e.printStackTrace();
                            isRunning = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected static void menu(Account account) {
        boolean isInMenu = true;

        while (isInMenu) {
            System.out.println("Чтобы войти в чат введите /join");
            String command = scanner.next();
            switch (command) {
                case "/join":
                    System.out.println("Вы вошли в чат!");
                    account.setInChat(true);
                    isInMenu = false;
                    break;
                default:
                    System.out.println("Извините, введённая команда не найдена!");
                    break;
            }
        }
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