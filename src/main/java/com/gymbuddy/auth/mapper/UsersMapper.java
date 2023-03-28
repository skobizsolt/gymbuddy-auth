package com.gymbuddy.auth.mapper;

import com.gymbuddy.auth.dto.AuthRoles;
import com.gymbuddy.auth.dto.user.CreateUserDto;
import com.gymbuddy.auth.dto.user.MinimalUserDto;
import com.gymbuddy.auth.dto.user.UpdateUserDto;
import com.gymbuddy.auth.dto.user.UserDto;
import com.gymbuddy.auth.persistence.domain.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring", imports = LocalDate.class)
public interface UsersMapper {
    @Named("userDtoMapper")
    UserDto toUserDto(User user);

    @Named("minimalUserDtoMapper")
    MinimalUserDto toMinimalUserDto(User user);

    @IterableMapping(qualifiedByName = "minimalUserDtoMapper")
    List<MinimalUserDto> toMinimalUserDtoList(List<User> users);

    @Mapping(target = "registrationDate", expression = "java(LocalDate.now())")
    User toUser(CreateUserDto dto);

    void modifyUser(UpdateUserDto updateUserDto, @MappingTarget User user);

    @AfterMapping
    default void setRole(@MappingTarget User user) {
        user.setRole(AuthRoles.USER);
    }
}
