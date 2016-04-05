package com.lethalminds.udonate.client.utilities;

/**
 * Created by Nishok on 4/3/2016.
 */
public class User {

    public String dob, email, password, username, address, userJSON;

    public User(String username, String email, String password, String dob, String address, String uJSON) {
        this.dob = dob;
        this.username = username;
        this.email = email;
        this.address = address;
        this.password = password;
        this.userJSON = uJSON;
    }

    public User(String username, String password) {
        this(username, "", password, "","", null);
    }

}
