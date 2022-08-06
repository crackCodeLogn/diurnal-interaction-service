package com.vv.personal.diurnal.interaction.data.repository;

import com.vv.personal.diurnal.interaction.data.entity.UserMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Vivek
 * @since 24/10/21
 */
@Repository
public interface UserMappingRepository extends JpaRepository<UserMappingEntity, Integer> {

    @Query(value = "select count(emailHash) from UserMappingEntity where email = :email")
    Long checkIfEmailExists(@Param("email") String email);
}