package SubProject.EShop.service;

import SubProject.EShop.domain.User;
import SubProject.EShop.dto.UserSignupRequestDto;
import SubProject.EShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long signup(UserSignupRequestDto requestDto){
        //TODO 이메일 충복확인
        /*
        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new IllegalArgumentException("이미 가입된 이메일입니다.")
            }
        */

        //TODO 비밀번호 암호화
        //String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(
            requestDto.getEmail(),
            requestDto.getPassword(),
            requestDto.getUsername()
        );

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

}
