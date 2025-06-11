package com.example.assetbasedriskassessmentplatformbackend.repository;

import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Equivalent to findWholeTable() - provided by JpaRepository as findAll()

    // Equivalent to findById() - provided by JpaRepository

    // Find by username
    Optional<User> findByAssetUserName(String assetUserName);

    // Find by email
    Optional<User> findByAssetUserEmail(String assetUserEmail);

    // Insert and update operations are handled by JpaRepository's save() method

    // Delete operation is provided by JpaRepository
}