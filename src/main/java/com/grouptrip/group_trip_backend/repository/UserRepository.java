package com.grouptrip.group_trip_backend.repository;

import com.grouptrip.group_trip_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}