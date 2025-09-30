package SubProject.EShop.service;

import SubProject.EShop.domain.User;
import SubProject.EShop.dto.UserLoginRequestDto;
import SubProject.EShop.dto.UserSignupRequestDto;
import SubProject.EShop.exception.BusinessException;
import SubProject.EShop.exception.ErrorCode;
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
        // 이메일 중복 확인 로직
        if(userRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }

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

    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    public User login(UserLoginRequestDto requestDto){
        // 1. email로 사용자 조회
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 2. 비밀번호 확인 ( 현재는 단순 문자열 비교)
        if(!user.getPassword().equals(requestDto.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 향후 암호화된 비밀번호를 비교하는 로직으로 변경 예정

        return user;
    }
}
