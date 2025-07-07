package com.example.assetbasedriskassessmentplatformbackend.repository;

import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;
import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    // 原生SQL查询：支持首字母匹配和前缀匹配
    @Query(value = """
    SELECT u.*, 
        CASE WHEN LOWER(u.asset_user_name) LIKE LOWER(:prefixPattern) THEN 1 ELSE 0 END AS prefix_score,
        CASE WHEN (
            SELECT CONCAT(
                SUBSTRING(LOWER(SUBSTRING_INDEX(u.asset_user_name, ' ', 1)), 1, 1),
                IF(LOCATE(' ', u.asset_user_name) > 0, 
                   SUBSTRING(LOWER(SUBSTRING_INDEX(SUBSTRING_INDEX(u.asset_user_name, ' ', 2), ' ', -1)), 1, 1), 
                   ''),
                IF(LENGTH(u.asset_user_name) - LENGTH(REPLACE(u.asset_user_name, ' ', '')) > 1,
                   SUBSTRING(LOWER(SUBSTRING_INDEX(SUBSTRING_INDEX(u.asset_user_name, ' ', 3), ' ', -1)), 1, 1),
                   '')
            )
        ) = LOWER(:initials) THEN 1 ELSE 0 END AS initials_score
    FROM asset_user_table u
    WHERE 
        LOWER(u.asset_user_name) LIKE LOWER(:prefixPattern)
        OR (
            SELECT CONCAT(
                SUBSTRING(LOWER(SUBSTRING_INDEX(u.asset_user_name, ' ', 1)), 1, 1),
                IF(LOCATE(' ', u.asset_user_name) > 0, 
                   SUBSTRING(LOWER(SUBSTRING_INDEX(SUBSTRING_INDEX(u.asset_user_name, ' ', 2), ' ', -1)), 1, 1), 
                   ''),
                IF(LENGTH(u.asset_user_name) - LENGTH(REPLACE(u.asset_user_name, ' ', '')) > 1,
                   SUBSTRING(LOWER(SUBSTRING_INDEX(SUBSTRING_INDEX(u.asset_user_name, ' ', 3), ' ', -1)), 1, 1),
                   '')
            )
        ) = LOWER(:initials)
    ORDER BY 
        prefix_score DESC,
        initials_score DESC,
        asset_user_name
    LIMIT 5
    """, nativeQuery = true)
    List<User> findByInitialsOrPrefix(
            @Param("initials") String initials,
            @Param("prefixPattern") String prefixPattern);

    // 默认方法：智能生成查询参数
    default List<User> findTop5ByInitials(String input) {
        // 生成首字母正则表达式（如 zs -> z.*s.*）
        String initialsPattern = Arrays.stream(input.split(""))
                .map(c -> c + ".*")
                .collect(Collectors.joining());

        // 生成前缀匹配模式（如 ji -> ji%）
        String prefixPattern = input.toLowerCase() + "%";

        return findByInitialsOrPrefix(
                input,
//                initialsPattern,
                prefixPattern
        );
    }
    @Query("SELECT COUNT(u) FROM User u WHERE " +
            "u.assetUserLevel = :permission")
    long countWithFilters(@Param("permission") Integer permission);

    @Query("SELECT u FROM User u WHERE " +
            "u.assetUserLevel = :permission")
    List<User> findByFilter(@Param("permission") Integer permission,
            Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE " +
            "LOWER(u.assetUserName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    long countWithSearch(@Param("searchTerm") String searchTerm);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.assetUserName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> findBySearch(@Param("searchTerm") String searchTerm,Pageable pageable);


}