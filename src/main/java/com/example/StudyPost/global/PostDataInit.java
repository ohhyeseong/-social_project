package com.example.StudyPost.global;

import com.example.StudyPost.post.dto.PostCreateRequestDto;
import com.example.StudyPost.post.service.PostService;
import com.example.StudyPost.user.domain.User;
import com.example.StudyPost.user.domain.UserRole;
import com.example.StudyPost.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class PostDataInit {

    private final PostService postService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // 1. 테스트용 유저 생성 (이미 존재하면 건너뜀)
            if (userRepository.findByUsername("testuser").isPresent()) {
                return;
            }

            User user = User.builder()
                    .username("testuser")
                    .password(passwordEncoder.encode("password"))
                    .nickname("테스트유저")
                    .role(UserRole.USER)
                    .build();
            
            userRepository.save(user);

            // 2. 테스트용 게시글 20개 생성
            for (int i = 1; i <= 20; i++) {
                PostCreateRequestDto dto = new PostCreateRequestDto(
                        "테스트 제목 " + i,
                        "테스트 내용 " + i
                );
                try {
                    // 변경된 create 메서드 호출 (이미지는 null로 전달)
                    postService.create(dto, null, user);
                } catch (IOException e) {
                    // 초기 데이터 생성 시에는 파일 I/O 예외가 발생할 일이 거의 없으므로 간단히 처리
                    e.printStackTrace();
                }
            }
        };
    }
}
