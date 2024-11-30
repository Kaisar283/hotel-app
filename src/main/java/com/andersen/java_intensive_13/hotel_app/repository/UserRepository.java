package com.andersen.java_intensive_13.hotel_app.repository;

import com.andersen.java_intensive_13.hotel_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
