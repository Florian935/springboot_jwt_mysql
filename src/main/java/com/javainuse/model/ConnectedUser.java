package com.javainuse.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class ConnectedUser extends User {

    private final String lastname;

    public ConnectedUser(DAOUser user, List<GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.lastname = user.getLastname();
    }

    public String getLastname() {
        return lastname;
    }
}
