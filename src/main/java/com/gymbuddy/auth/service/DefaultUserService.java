package com.gymbuddy.auth.service;

import com.gymbuddy.auth.dto.user.MinimalUserDto;
import com.gymbuddy.auth.dto.user.UpdateUserDto;
import com.gymbuddy.auth.dto.user.UserDto;
import com.gymbuddy.auth.exception.ServiceExpection;
import com.gymbuddy.auth.mapper.UsersMapper;
import com.gymbuddy.auth.persistence.domain.User;
import com.gymbuddy.auth.persistence.query.UserEntityMapper;
import com.gymbuddy.auth.persistence.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gymbuddy.auth.exception.Errors.USER_NOT_FOUND;

/**
 * Default implementation of User service.
 */
@Service
@AllArgsConstructor
public class DefaultUserService implements UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserEntityMapper userEntityMapper;
    @Autowired
    private final UsersMapper usersMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MinimalUserDto> getAllUsers() {
        return usersMapper.toMinimalUserDtoList(userRepository.findAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto getUserById(final Long userId) {
        final User user = getUserEntityById(userId);
        return usersMapper.toUserDto(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUser(final Long userId) {
        final User user = getUserEntityById(userId);
        // delete all user transactions
        // delete user
        userRepository.delete(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto updateUser(final Long userId, final UpdateUserDto updateUserDto) {
        final User user = getUserEntityById(userId);
        //updating user
        usersMapper.modifyUser(updateUserDto, user);
        userRepository.save(user);
        return usersMapper.toUserDto(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserEntityById(final Long userId) {
        return userEntityMapper.getUsersByUserId(userId)
                .orElseThrow(() -> {
                    throw new ServiceExpection(USER_NOT_FOUND);
                });
    }
}
