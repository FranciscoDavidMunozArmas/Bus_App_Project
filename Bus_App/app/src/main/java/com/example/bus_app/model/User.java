package com.example.bus_app.model;

public class User {

    private String id;
    private String name;
    private String password;
    private boolean hand;
    private float money;

    public User() {
        this("", "");
    }

    public User(String name, String password) {
        this(name, password, true);
    }

    public User(String name, String password, boolean hand) {
        this("", name, password, hand);
    }

    public User(String id, String name, String password, boolean hand) {
        this("", name, password, hand, 0);
    }

    public User(String id, String name, String password, boolean hand, float money) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.hand = hand;
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHand() {
        return hand;
    }

    public void setHand(boolean hand) {
        this.hand = hand;
    }
}
