package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.calendar.entity.Users;
import com.example.calendar.repository.UserRepository;

@Service  // 스프링 빈으로 등록
@Transactional  // 데이터베이스 트랜잭션 적용
public class UserService {

    private final UserRepository userRepository;

    // ✅ 생성자 주입 방식 (Spring Boot 4.3+에서는 @Autowired 필요 없음)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ 사용자 저장 (불변성을 위해 final 사용)
    public Users saveUser(final Users user) {
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보가 null일 수 없습니다.");
        }
        return userRepository.save(user);
    }

    // ✅ 사용자 ID로 찾기 (예외 처리 추가)
    public Users findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다: " + userId));
    }
}
