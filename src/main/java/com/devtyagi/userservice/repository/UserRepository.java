package com.devtyagi.userservice.repository;

import com.devtyagi.userservice.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findUserByEmailAddress(String emailAddress);

}
