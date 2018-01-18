package com.example.ishitaroychowdhury.socialapp;

import java.io.Serializable;

/**
 * Created by ishitaroychowdhury on 11/21/17.
 */

public class UserRequests implements Serializable {
    User u;
    String type;

    public UserRequests(User u, String type) {
        this.u = u;
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserRequests{" +
                "u=" + u +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRequests that = (UserRequests) o;

        if (!u.equals(that.u)) return false;
        return type.equals(that.type);

    }

    @Override
    public int hashCode() {
        int result = u.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}