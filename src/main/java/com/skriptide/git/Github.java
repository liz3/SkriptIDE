package com.skriptide.git;

/**
 * Copyright © 2016 Matthew E Development - All Rights Reserved
 * You may NOT use, distribute and modify this code.
 * <p>
 * Created by Matthew E on 10/23/2016 at 8:29 PM.
 */
public class Github extends Git {

    private String email;
    private String username;

    public Github(String email, String username) {
        this.email = email;
        this.username = username;
    }

    @Override
    public void push(String message) {
        executeCommand("git commit -m \"" + message + "\"");
        executeCommand("git push master");
    }

    @Override
    public void config() {
        executeCommand("git config --global user.email " + email);
        executeCommand("git config --global user.name " + username);
    }

    @Override
    public void pull() {
        executeCommand("git pull");
    }
}
