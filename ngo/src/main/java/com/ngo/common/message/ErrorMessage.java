package com.ngo.common.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage
{
    /**
     * Login
     */

    /**
     * User
     */
    USER_NOT_FOUND(NOT_FOUND, "유저 정보를 불러올 수 없습니다"),
    ATTENDANCE_NOT_FOUND(NOT_FOUND, "출석 정보를 불러올 수 없습니다"),
    ATTENDANCE_ALREADY_EXIST(CONFLICT, "출석 정보가 이미 존재합니다"),

    /**
     * News
     */

    /**
     * Scrap
     */
    SCRAP_NOT_FOUND(NOT_FOUND, "스크랩이 존재하지 않습니다")

    /**
     * Dictionary
     */

    /**
     * Rank
     */

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode()
    {
        return httpStatus.value();
    }
}
