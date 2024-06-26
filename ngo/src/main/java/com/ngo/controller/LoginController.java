package com.ngo.controller;

import com.ngo.common.ApiResponse;
import com.ngo.dto.requestDto.LoginFormDto;
import com.ngo.dto.responseDto.UserDto;
import com.ngo.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

/**
 * ==== LoginController for Login and Logout ====
 * @package : com.ngo.controller
 * @name : LoginController.java
 * @date : 2024. 05. 09.
 * @author : siyunsmacbook
*/

@RestController
@RequestMapping("/api")
public class LoginController
{
    private final LoginService loginService;

    public LoginController(LoginService loginService)
    {
        this.loginService = loginService;
    }

    @PostMapping("/users/login")
    public ApiResponse<UserDto> loginUser(@RequestBody LoginFormDto loginFormDto, HttpServletRequest request, HttpServletResponse response)
    {
        return loginService.loginUser(loginFormDto, request, response);
    }

    @PostMapping("/users/logout")
    public ApiResponse<Void> logoutUser(HttpServletRequest request, HttpServletResponse response)
    {
        return loginService.logoutUser(request, response);
    }
}
