package io.github.javathought.devoxx.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 */
public class Role {
    public static final String ADMIN = "super";
    public static final String USER = "user";

    private String role;

    public Role() {
        // Empty to marshalling
    }

    public Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("role", role)
                .toString();
    }
}
