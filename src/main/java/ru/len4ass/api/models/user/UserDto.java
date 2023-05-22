package ru.len4ass.api.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {
    @JsonProperty("username")
    private final String username;

    @JsonProperty("email")
    private final String email;

    @JsonProperty("role")
    private final UserRole role;

    public UserDto(String username, String email, UserRole role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }
}
