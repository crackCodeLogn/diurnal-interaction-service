package com.vv.personal.diurnal.interaction.service.impl;

import com.vv.personal.diurnal.interaction.data.dao.UserMappingDao;
import com.vv.personal.diurnal.interaction.service.UserMappingService;
import lombok.AllArgsConstructor;

/**
 * @author Vivek
 * @since 06/08/22
 */
@AllArgsConstructor
public class UserMappingServiceImpl implements UserMappingService {
    private final UserMappingDao userMappingDao;

    @Override
    public boolean isUserExistent(String email) {
        return userMappingDao.isUserExistent(email);
    }
}