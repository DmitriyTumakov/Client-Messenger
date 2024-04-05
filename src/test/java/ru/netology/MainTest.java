package ru.netology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.Logger.Logger;
import ru.netology.Logger.TextLogger;

import java.beans.PropertyEditorSupport;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import static ru.netology.Main.*;

public class MainTest {
    public static ServerSocket serverSocketMock;
    public static Socket clientSocketMock;

    @BeforeAll
    public static void setup() {
        serverSocketMock = Mockito.mock(ServerSocket.class);
        try {
            Mockito.when(serverSocketMock.accept()).thenReturn(clientSocketMock);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clientSocketMock = Mockito.mock(Socket.class);
        try {
            PipedOutputStream output = new PipedOutputStream();
            Mockito.when(clientSocketMock.getOutputStream()).thenReturn(output);
            PipedInputStream input = new PipedInputStream(output);
            Mockito.when(clientSocketMock.getInputStream()).thenReturn(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createSettingsTest() {
        File settings = new File("settings.txt");

        int port = createSettings(settings);

        Assertions.assertEquals(port, 8988);
    }

    @Test
    public void createSettingsTestFalse() {
        File settings = new File("settings.txt");

        int port = createSettings(settings);

        Assertions.assertNotEquals(port, 8888);
    }

    @Test
    public void getMessageTest() throws IOException {
        PrintWriter out = new PrintWriter(clientSocketMock.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocketMock.getInputStream()));
        Logger logger = new TextLogger();
        Account account = new Account("Dmitriy");
        account.setInChat(true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        Thread getMessageTest = new Thread(() -> {
            out.println("Hello");
        });
        getMessageTest.start();
        System.setOut(ps);
        getMessage(account, in, logger);
        String message = baos.toString();
        String[] result = message.split("\n");

        Assertions.assertEquals("Hello\r", result[0]);
    }

    @Test
    public void sendMessageTest() throws IOException {
        PrintWriter out = new PrintWriter(clientSocketMock.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocketMock.getInputStream()));
        Logger logger = new TextLogger();
        Account account = new Account("Dmitriy");
        account.setInChat(true);
        String message = "Hello";

        sendMessage(account, out, logger, message);

        String name = in.readLine();
        String result = in.readLine();

        Assertions.assertEquals("[Dmitriy] |> Hello", result);
    }

    @Test
    public void sendMessageExitTest() throws IOException {
        PrintWriter out = new PrintWriter(clientSocketMock.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocketMock.getInputStream()));
        Logger logger = new TextLogger();
        Account account = new Account("Dmitriy");
        account.setInChat(true);
        String message = "/exit";

        sendMessage(account, out, logger, message);

        String name = in.readLine();
        String result = in.readLine();

        Assertions.assertEquals("Пользователь Dmitriy вышел из чата.", result);
    }
}