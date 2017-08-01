package com.android.common.ui.password;

/**
 * Created by fengyulong on 2017/5/20.
 */

public class PasswordEvent {

    private String password;
    private int event;

    public PasswordEvent(int event, String password) {
        this.event = event;
        this.password = password;
    }

    public PasswordEvent(int event) {
        this.event = event;
    }

    public String getPassword() {
        return password;
    }

    public int getEvent() {
        return event;
    }

    public interface Event {
        int INPUT_COMPLATE = 1;
        int CLOSE = 2;
    }
}
