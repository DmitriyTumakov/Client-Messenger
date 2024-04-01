package ru.netology;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;

import static ru.netology.Main.createSettings;

public class MainTest {
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
}