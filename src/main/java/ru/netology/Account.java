package ru.netology;

public class Account {
    private String name;
    private boolean isInChat;

    public Account(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setInChat(boolean inChat) {
        this.isInChat = inChat;
    }

    public boolean isInChat() {
        return isInChat;
    }
}