package com.javainuse.service;

import com.javainuse.model.DAOUser;
import com.javainuse.model.UserDTO;

public interface UserService {
    DAOUser save(UserDTO user);
}
