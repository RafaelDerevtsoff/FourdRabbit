package com.fourd.rabbit.document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
@JsonSerialize
@JsonDeserialize
public class Lesson {
    String title;
    String description;
    Date date;

    public Lesson(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }
}
