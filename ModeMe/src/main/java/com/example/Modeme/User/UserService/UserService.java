package com.example.Modeme.User.UserService;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Modeme.User.UserDTO.UserDTO;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;

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
    	// 📌 (변경됨) 중복 확인을 한 번의 쿼리로 처리
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());

        if (existingUser.isPresent()) { // ✅ 중복 검사 최적화
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        
        if (!userDTO.getPhone().matches("\\d{10,11}")) { // ✅ 숫자만 허용 (10~11자리)
            throw new IllegalArgumentException("유효한 전화번호 형식이 아닙니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // User 엔티티로 변환 후 저장
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(encodedPassword);
        user.setName(userDTO.getName());
        // 이메일이 입력되지 않았다면 null 저장
        user.setEmail(userDTO.getEmail() != null && !userDTO.getEmail().isEmpty() ? userDTO.getEmail() : null);
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

    @Transactional
    public void updateUser(String username, UserDTO userDTO) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("❌ 사용자를 찾을 수 없습니다: " + username));

            System.out.println("🔹 기존 사용자 정보: " + user.toString()); // 기존 정보 확인
            System.out.println("🔹 요청된 수정 정보: " + userDTO.toString()); // 수정할 정보 확인
            
            // 📌 (변경됨) 전화번호 검증 추가
            if (!userDTO.getPhone().matches("\\d{10,11}")) {
                throw new IllegalArgumentException("유효한 전화번호 형식이 아닙니다.");
            }

            // 📌 (변경됨) 비밀번호 변경 시만 암호화 적용
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            
            if (userDTO.getEmail() != null) {
                user.setEmail(userDTO.getEmail());
            }

            // ✅ 수정할 정보 업데이트
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setBirthdate(userDTO.getBirthdate());
            user.setGender(userDTO.getGender());
            user.setPostcode(userDTO.getPostcode());
            user.setAddress(userDTO.getAddress());
            user.setAddressDetail(userDTO.getAddressDetail());

            userRepository.save(user); // 변경된 정보 저장
            System.out.println("✅ 회원정보 수정 성공: " + username); // 디버깅용 로그

        } catch (Exception e) {
            System.out.println("❌ 회원정보 수정 실패: " + e.getMessage()); // 예외 메시지 출력
            throw new RuntimeException("회원정보 수정 중 오류 발생: " + e.getMessage());
        }
    }
    
    public String findUsernameByNameAndContact(String name, String email, String phone) {
        if (email != null && !email.isEmpty()) {
            return findUsernameByNameAndEmail(name, email);
        } else if (phone != null && !phone.isEmpty()) {
            return findUsernameByNameAndPhone(name, phone);
        } else {
            return null; // 이메일과 전화번호 모두 입력되지 않은 경우
        }
    }

    // 아이디 마스킹 (앞 4자리만 노출)
    private String maskUsername(String username) {
        if (username.length() <= 4) {
            return "XXXX"; // 4자리 이하인 경우 모두 X로 처리
        }
        return username.substring(0, 4) + "X".repeat(username.length() - 4);
    }

    public String findUsernameByNameAndEmail(String name, String email) {
        Optional<User> userOptional = userRepository.findByNameAndEmail(name, email);
        return userOptional.map(user -> maskUsername(user.getUsername())).orElse(null);
    }

    public String findUsernameByNameAndPhone(String name, String phone) {
        Optional<User> userOptional = userRepository.findByNameAndPhone(name, phone);
        return userOptional.map(user -> maskUsername(user.getUsername())).orElse(null);
    }

}