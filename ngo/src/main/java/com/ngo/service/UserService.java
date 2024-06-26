package com.ngo.service;

import com.ngo.common.ApiResponse;
import com.ngo.common.exception.ConflictException;
import com.ngo.common.exception.NotFoundException;
import com.ngo.common.message.ErrorMessage;
import com.ngo.common.message.SuccessMessage;
import com.ngo.dto.requestDto.LoginPwDto;
import com.ngo.dto.requestDto.RegisterDto;
import com.ngo.dto.requestDto.UserLevelDto;
import com.ngo.dto.responseDto.AttDto;
import com.ngo.dto.responseDto.AttListDto;
import com.ngo.dto.responseDto.UserDto;
import com.ngo.model.Attendance;
import com.ngo.model.User;
import com.ngo.repository.AttendanceRepository;
import com.ngo.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * UserService offers user management and attendance features.
 * <pre>{@code
 * // Join and Withdraw
 * public ApiResponse<Void> registerUser(RegisterDto registerDto);
 * public ApiResponse<Void> withdrawalUser(Long userId);
 * // User
 * public ApiResponse<UserDto> getUser(Long userId);
 * public ApiResponse<UserLevelDto> patchUserLevel(Long userId, UserLevelDto userLevelDto);
 * public ApiResponse<Void> patchUserPw(Long userId, String newPw);
 * // Attendance
 * public ApiResponse<AttListDto> getUserAttendance(Long userId);
 * public ApiResponse<AttListDto> getRecentAttendance(Long userId);
 * public ApiResponse<AttDto> postUserAttendance(Long userId);
 * }</pre>
 * @package : com.ngo.service
 * @name : UserService.java
 * @date : 2024. 04. 16.
 * @author : siyunsmacbook
*/

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    public UserService(UserRepository userRepository, AttendanceRepository attendanceRepository)
    {
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
    }

    /**
     * 회원가입 및 회원탈퇴
     */

    public ApiResponse<Void> registerUser(RegisterDto registerDto)
    {
        User user = User.build(registerDto);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            return ApiResponse.error(ErrorMessage.REGISTER_NOT_ALLOW);
        }
        return ApiResponse.success(SuccessMessage.REGISTER_USER_SUCCESS);
    }

    public ApiResponse<Void> withdrawalUser(Long userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        userRepository.delete(user);
        return ApiResponse.success(SuccessMessage.WITHDRAWAL_USER_SUCCESS);
    }

    /**
     * 유저 정보 관리
     */

    public ApiResponse<UserDto> getUser(Long userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
        return ApiResponse.success(SuccessMessage.GET_USER_SUCCESS, UserDto.build(user));
    }

    public ApiResponse<UserLevelDto> patchUserLevel(Long userId, UserLevelDto userLevelDto)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        user.setLevel(userLevelDto.getLevel());
        userRepository.save(user);

        return ApiResponse.success(SuccessMessage.PATCH_USER_LEVEL_SUCCESS, userLevelDto);
    }

    public ApiResponse<Void> patchUserPw(Long userId, LoginPwDto loginPwDto)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        user.setLoginPw(loginPwDto.getLoginPw());
        userRepository.save(user);

        return ApiResponse.success(SuccessMessage.PATCH_USER_PW_SUCCESS);
    }

    /**
     * 출석 정보 관리
     */

    public ApiResponse<AttListDto> getUserAttendance(Long userId)
    {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        List<AttDto> attDtoList = attendanceRepository.findByUser_UserId(userId).stream()
                .map(AttDto::build)
                .toList();

        return ApiResponse.success(SuccessMessage.GET_USER_ATTENDANCE_SUCCESS, new AttListDto(userId, attDtoList));
    }

    public ApiResponse<AttListDto> getRecentAttendance(Long userId)
    {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        /* today's date */
        LocalDate date = LocalDate.now();
        List<LocalDate> recentDateList = new ArrayList<>();

        /* recent week date */
        for (int day=0; day<7; day++)
        {
            recentDateList.add(date);
            date = date.minusDays(1);
        }

        List<AttDto> attDtoList = attendanceRepository.findByUser_UserId(userId).stream()
                .filter(attendance -> {
                    LocalDate attendanceDate = attendance.getDate();
                    for (LocalDate recentDate : recentDateList)
                    {
                        if (attendanceDate.equals(recentDate))
                            return true;
                    }
                    return false;
                })
                .map(AttDto::build)
                .toList();

        return ApiResponse.success(SuccessMessage.GET_USER_ATTENDANCE_SUCCESS, new AttListDto(userId, attDtoList));
    }

    public ApiResponse<AttDto> postUserAttendance(Long userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        LocalDate date = LocalDate.now();

        List<Attendance> userAttList = attendanceRepository.findByUser_UserId(userId).stream()
                .filter(userAtt -> userAtt.getDate().equals(date))
                .toList();

        if (!userAttList.isEmpty())
            throw new ConflictException(ErrorMessage.ATTENDANCE_ALREADY_EXIST);

        Attendance attendance = new Attendance(date, user);
        attendanceRepository.save(attendance);
        return ApiResponse.success(SuccessMessage.POST_USER_ATTENDANCE_SUCCESS, AttDto.build(attendance));

    }


}
