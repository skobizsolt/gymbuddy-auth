package com.gymbuddy.auth.controller;

import com.gymbuddy.auth.dto.user.MinimalUserDto;
import com.gymbuddy.auth.dto.user.UpdateUserDto;
import com.gymbuddy.auth.dto.user.UserDto;
import com.gymbuddy.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Implementation of User controller.
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    @Autowired
    private final UserService userService;

    /**
     * List all users email and username.
     *
     * @return 200 OK.
     */
    @GetMapping
    @Operation(summary = "List all users data", security = {@SecurityRequirement(name = "token")})
    public ResponseEntity<List<MinimalUserDto>> getUsers() {
        log.info("Endpoint invoked.");
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    /**
     * List all user specific data without critical information.
     *
     * @param userId refers to the user's identity.
     * @return with UserDto, containing User, Profile and Address data.
     */
    @GetMapping(value = "/{userId}/profile", name = "getUser")
    @Operation(summary = "List all user specific data to profile page", security = {@SecurityRequirement(name = "token")})
    public ResponseEntity<UserDto> getAllUserData(@PathVariable final Long userId) {
        log.info("Endpoint invoked. userId = {}", userId);
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    /**
     * Updates user profile with the given data.
     *
     * @param userId for selecting what user should be deleted.
     * @return 200 OK.
     */
    @PutMapping(value = "/{userId}/profile", name = "updateUser")
    @Operation(summary = "Update user profile and address", security = {@SecurityRequirement(name = "token")})
    public ResponseEntity<UserDto> updateProfile(@PathVariable final Long userId,
                                                 @RequestBody final UpdateUserDto updateUserDto) {
        log.info("Endpoint invoked. userId = {}, updateUserDto = {}", userId, updateUserDto);
        return ResponseEntity.ok().body(userService.updateUser(userId, updateUserDto));
    }

    /**
     * Deletes user from repository.
     *
     * @param userId for selecting what user should be deleted.
     * @return 200 OK.
     */
    @DeleteMapping(value = "/{userId}")
    @Operation(summary = "Delete user", security = {@SecurityRequirement(name = "token")})
    public ResponseEntity<Void> deleteUser(@PathVariable final Long userId) {
        log.info("Endpoint invoked. userId = {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
