package com.example.Modeme.User.UserService;

import com.example.Modeme.User.UserDTO.UserDTO;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder passwordEncoder;

    public void registerUser(UserDTO userDTO) {
        // 아이디 중복 체크
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 이메일 중복 체크
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
//        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // User 엔티티로 변환 후 저장
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
//        user.setPassword(encodedPassword);
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setBirthdate(userDTO.getBirthdate() != null ? userDTO.getBirthdate() : null);
        user.setGender(userDTO.getGender());
        user.setPostcode(userDTO.getPostcode());
        user.setAddress(userDTO.getAddress());
        user.setAddressDetail(userDTO.getAddressDetail());

        userRepository.save(user);
    }
}
