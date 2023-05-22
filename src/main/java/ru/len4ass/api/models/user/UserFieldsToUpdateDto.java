package ru.len4ass.api.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserFieldsToUpdateDto {
    @JsonProperty("user_role")
    private UserRole userRole;

    public UserFieldsToUpdateDto() {
    }

    public UserFieldsToUpdateDto(UserRole userRole) {
        this.userRole = userRole;
    }

    public UserRole getUserRole() {
        return userRole;
    }
}
