package SubProject.EShop.service;

import SubProject.EShop.config.JwtUtil;
import SubProject.EShop.domain.User;
import SubProject.EShop.dto.UserSignupRequestDto;
import SubProject.EShop.exception.BusinessException;
import SubProject.EShop.exception.ErrorCode;
import SubProject.EShop.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder; // import 추가

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock // 1. PasswordEncoder에 대한 Mock 객체 추가
    private PasswordEncoder passwordEncoder;

    @Mock // (미래를 위해) JwtUtil에 대한 Mock 객체도 추가
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signup_success() {
        // given
        UserSignupRequestDto requestDto = new UserSignupRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password123");
        requestDto.setUsername("tester");

        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());

        // 2. passwordEncoder.encode가 호출되면 "encoded_password"라는 가짜 암호를 반환하도록 설정
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encoded_password");

        User savedUser = new User(requestDto.getEmail(), "encoded_password", requestDto.getUsername());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        userService.signup(requestDto);

        // then
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일 중복")
    void signup_fail_when_email_is_duplicated() {
        // given
        UserSignupRequestDto requestDto = new UserSignupRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password123");
        requestDto.setUsername("tester");

        User existingUser = new User("test@example.com", "anypassword", "anyname");
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(existingUser));

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.signup(requestDto);
        });

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_DUPLICATION);
        verify(userRepository, never()).save(any(User.class));
    }
}