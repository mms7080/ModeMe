package com.example.Modeme.User.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Modeme.User.UserDTO.UserDTO;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리
     */
    public void registerUser(UserDTO userDTO) {
        // 아이디 중복 체크
        if (isUsernameTaken(userDTO.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 이메일 중복 체크
        if (isEmailTaken(userDTO.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // User 엔티티로 변환 후 저장
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(encodedPassword);
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setBirthdate(userDTO.getBirthdate());
        user.setGender(userDTO.getGender());
        user.setPostcode(userDTO.getPostcode());
        user.setAddress(userDTO.getAddress());
        user.setAddressDetail(userDTO.getAddressDetail());
        user.setRole("user");

        userRepository.save(user);
    }

    /**
     * 아이디 중복 여부 확인
     * @param username 사용자 아이디
     * @return 중복 여부 (true: 중복, false: 사용 가능)
     */
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * 이메일 중복 여부 확인
     * @param email 사용자 이메일
     * @return 중복 여부 (true: 중복, false: 사용 가능)
     */
    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * 사용자 아이디로 사용자 정보 조회
     * @param username 사용자 아이디
     * @return 사용자 엔티티
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));
    }

    /**
     * 사용자 권한 업데이트
     * @param username 사용자 아이디
     * @param role 새로운 권한
     */
    public void updateRole(String username, String role) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));
        user.setRole(role);
        userRepository.save(user);
    }
}
