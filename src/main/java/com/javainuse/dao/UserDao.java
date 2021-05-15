package com.javainuse.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.javainuse.model.DAOUser;

import java.util.Optional;

@Repository
public interface UserDao extends CrudRepository<DAOUser, Integer> {

    Optional<DAOUser> findByUsername(String username);

}