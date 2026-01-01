package com.library.service;

public class SessionId {
    private static int currentAccountId = 0;

    public static void setSession(int id) {
        currentAccountId = id;
    }

    public static int getCurrentAccountId() {
        return currentAccountId;
    }
}

