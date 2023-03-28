package com.gymbuddy.auth.service;

import com.gymbuddy.auth.dto.user.MinimalUserDto;
import com.gymbuddy.auth.dto.user.UpdateUserDto;
import com.gymbuddy.auth.dto.user.UserDto;
import com.gymbuddy.auth.persistence.domain.User;

import java.util.List;

/**
 * Interface for UserService
 */
public interface UserService {
    /**
     * Method for listing all users basic data.
     *
     * @return all users.
     */
    List<MinimalUserDto> getAllUsers();

    /**
     * Method for listing data of the given user.
     *
     * @param userId id of the user we need
     * @return UserDto object.
     */
    UserDto getUserById(final Long userId);

    /**
     * Method for removing an existing user.
     *
     * @param userId id of the user we want to delete.
     */
    void deleteUser(final Long userId);

    /**
     * Method for updating an existing user's profile and address.
     *
     * @param userId        id of the user we want to update.
     * @param updateUserDto data of the user we want to update.
     * @return the updated user's data.
     */
    UserDto updateUser(final Long userId, final UpdateUserDto updateUserDto);

    /**
     * Method for getting the selected entity from repo.
     *
     * @param userId id of the user
     * @return the selected User entity.
     */
    User getUserEntityById(final Long userId);
}
