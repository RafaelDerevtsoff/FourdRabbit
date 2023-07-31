package com.fourd.rabbit.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fourd.rabbit.document.Lesson;

import java.util.HashMap;
import java.util.List;

@JsonSerialize
@JsonDeserialize
public class CreateLessonsRequest {
    @JsonProperty("teacher")
    private String teacher;
    @JsonProperty("lessons")
    private HashMap<String,Lesson> lessons;

    public CreateLessonsRequest(String teacher, HashMap<String, Lesson> lessons) {
        this.teacher = teacher;
        this.lessons = lessons;
    }

    public CreateLessonsRequest() {
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public HashMap<String, Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(HashMap<String, Lesson> lessons) {
        this.lessons = lessons;
    }
}
