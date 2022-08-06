package com.vv.personal.diurnal.interaction.data.dao;

import com.vv.personal.diurnal.interaction.data.repository.UserMappingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Vivek
 * @since 06/08/22
 */
@Slf4j
@AllArgsConstructor
public class UserMappingDao {

    private final UserMappingRepository userMappingRepository;

    public boolean isUserExistent(String email) {
        return userMappingRepository.checkIfEmailExists(email) == 1;
    }
}