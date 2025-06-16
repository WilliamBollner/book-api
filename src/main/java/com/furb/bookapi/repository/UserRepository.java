package com.furb.bookapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.furb.bookapi.model.user.User;

@Repository
public interface UserRepository  extends JpaRepository<User, String> {
    UserDetails findByUsername(String username);
}
