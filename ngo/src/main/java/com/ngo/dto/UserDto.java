package com.ngo.dto;

import com.ngo.model.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto
{
    private final Long userId;
    private final String userName;
    private final String email;
    private final String level;

    @Builder(access = AccessLevel.PRIVATE)
    private UserDto(Long userId, String userName, String email, String level)
    {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.level = level;
    }

    public static UserDto build(User user)
    {
        return UserDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .level(user.getLevel())
                .build();
    }


}