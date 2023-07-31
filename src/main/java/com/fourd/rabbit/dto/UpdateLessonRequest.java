package com.fourd.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fourd.rabbit.document.Lesson;

import java.util.Map;

@JsonDeserialize
@JsonSerialize
public class UpdateLessonRequest {
    @JsonProperty("teacher")
    private String teacher;
    @JsonProperty("lessons")
    private Map<String, Lesson> updatedLessons;

    public UpdateLessonRequest() {
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Map<String, Lesson> getUpdatedLessons() {
        return updatedLessons;
    }

    public void setUpdatedLessons(Map<String, Lesson> updatedLessons) {
        this.updatedLessons = updatedLessons;
    }

    public UpdateLessonRequest(String teacher, Map<String, Lesson> updatedLessons) {
        this.teacher = teacher;
        this.updatedLessons = updatedLessons;
    }
}
