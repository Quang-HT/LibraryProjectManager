package com.library.service;

import lombok.Getter;

public class SessionID {
    @Getter
    private static int currentAccountId = 0;

    public static void setSession(int id) {
        currentAccountId = id;
    }

}

