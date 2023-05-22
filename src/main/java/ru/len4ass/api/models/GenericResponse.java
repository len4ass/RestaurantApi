package ru.len4ass.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericResponse {
    @JsonProperty("info")
    private String infoMessage;

    public GenericResponse(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    public String getInfoMessage() {
        return infoMessage;
    }
}