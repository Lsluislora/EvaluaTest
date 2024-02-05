package com.example.myexampleevaluatest.data;

public class UserData {
    private String username;
    private String email;

    // Constructor
    public UserData(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Métodos getter y setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
