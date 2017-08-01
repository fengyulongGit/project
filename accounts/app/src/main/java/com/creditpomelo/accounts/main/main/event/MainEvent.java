package com.creditpomelo.accounts.main.main.event;

/**
 * Created by fengyulong on 2017/5/15.
 */

public class MainEvent {
    public interface Events {
        int LIMIT_COMMIT = 1;
        int NEW_MESSAGE = 2;
        int CREDIT_CARD_COMMIT = 3;
    }

    private MainEvent() {
    }

    public MainEvent(int event) {
        this.event = event;
    }

    private int event;

    public int getEvent() {
        return event;
    }
}
