package com.library.service;

import lombok.Getter;
import lombok.Setter;

public class SessionID {
    @Getter
    private static int currentAccountId = 0;

    public static void setSession(int id) {
        currentAccountId = id;
    }

}

