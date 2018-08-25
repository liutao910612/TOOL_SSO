package com.liutao.model;

import java.io.Serializable;

/**
 * @author: LIUTAO
 * @Date: Created in 2018/8/25  15:03
 * @Modified By:
 */
public class UserModel implements Serializable {
    private String username;
    private String password;

    public UserModel() {
    }

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
