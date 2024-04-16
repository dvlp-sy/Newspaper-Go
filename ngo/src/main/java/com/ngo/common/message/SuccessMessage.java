package com.ngo.common.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessMessage
{
    /**
     * Login
     */

    /**
     * User
     */
    GET_USER_SUCCESS(OK, "유저 정보 불러오기에 성공했습니다")

    /**
     * News
     */

    /**
     * Scrap
     */

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