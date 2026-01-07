package com.example.StudyPost.user.service;

import com.example.StudyPost.global.exception.CustomException;
import com.example.StudyPost.global.exception.ErrorCode;
import com.example.StudyPost.user.domain.User;
import com.example.StudyPost.user.domain.UserRole;
import com.example.StudyPost.user.dto.UserCreateRequestDto;
import com.example.StudyPost.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Getter
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Long signup(UserCreateRequestDto dto) {

        if (userRepository.existsByUsername(dto.username()))
        {throw new CustomException(ErrorCode.CONFLICT_USERNAME);}

        String encodedPassword = passwordEncoder.encode(dto.password());

        User user = User.builder()
                .username(dto.username())
                .password(encodedPassword)
                .nickname(dto.nickname())
                .role(UserRole.USER)
                .build();

        return userRepository.save(user).getId();
    }
}
