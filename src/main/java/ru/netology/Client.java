package ru.netology;

import ru.netology.Logger.Logger;
import ru.netology.Logger.LoggerEnum;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Client {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isRunning = true;
    private final Account account;
    private final Logger logger;
    private final String host;
    private final int port;

    public Client(Account account, Logger logger, String host, int port) {
        this.account = account;
        this.logger = logger;
        this.host = host;
        this.port = port;
    }

    public void stop() {

    }

    public void start() {
        if (port != 0) {
            System.out.printf("Выбранный порт сервера: %d\n", port);

            try (Socket clientSocket = new Socket(host, port)) {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out.println(account.getName());
                menu(account);

                new Thread(() -> {
                    while (true) {
                        String message = scanner.nextLine();
                        sendMessage(account, out, logger, message, LocalDateTime.now());
                    }
                }).start();

                while (isRunning) {
                    getMessage(account, in, logger);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected static void sendMessage(Account account, PrintWriter out, Logger logger, String message, LocalDateTime localDateTime) {
        if (account.isInChat()) {
            String name = account.getName();
            String messageInfo = "[" + account.getName() + "] {" + localDateTime + "} |> ";
            if (message.equals("/exit")) {
                account.setInChat(false);
                out.println(name);
                out.println("Пользователь " + name + " вышел из чата.");
            }
            if (!(message.isEmpty())) {
                out.println(name);
                out.println(messageInfo + message);
                logger.log("[Я] -> " + message, LoggerEnum.MESSAGE);
            }
        }
    }

    protected static void getMessage(Account account, BufferedReader in, Logger logger) {
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
}