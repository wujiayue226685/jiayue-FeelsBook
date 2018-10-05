package com.jiayue.feelsbook.bean;

import java.io.Serializable;

/**
 * emotions function(entries)
 */
public class Emotion implements Serializable {

    //emotions: love ...
    private String str_mood;

    //text
    private String text;

    //time
    private String time;

    public String get_mood() {
        return str_mood;
    }

    public void set_mood(String str_mood) {
        this.str_mood = str_mood;
    }

    public String get_text() {
        return text;
    }

    public void set_text(String text) {
        this.text = text;
    }

    public String get_time() {
        return time;
    }

    public void set_time(String time) {
        this.time = time;
    }


}
