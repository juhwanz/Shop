package SubProject.EShop.service;

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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 기능을 사용하겠다는 선언
public class UserServiceTest {

    @InjectMocks
    private UserService userService; // 테스트 대상, @Mock으로 만든 가짜 객체들이 여기에 주입

    @Mock
    private UserRepository userRepository; // 가짜 UserRepository( 스턴트 대역)

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signup_success(){
        // given(주어진 상황)
        // 회원가입에 필요한 DTO 생성
        UserSignupRequestDto requestDto = new UserSignupRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password123");
        requestDto.setUsername("tester");

        // 가짜 userRepository가 findByEmail을 호출받음 -> '아직 가입된 이메일 없는 상황'연기하도록 시나리오 설정=빈 Optional 반환
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());

        // 가짜 userRepository가 save 호출시 '저장이 완료된 객체'반환 하도록 시나리오 설정.
        User savedUser = new User(requestDto.getEmail(), requestDto.getPassword(), requestDto.getUsername());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        //when (무엇을 할때)
        // 실제로 테스트하고 싶은 userService.signup 메소드 호출
        userService.signup(requestDto);

        //then (결과 확인)
        // userRepo의 save메소드가 정확히 1번 호출?
        verify(userRepository).save(any(User.class));

    }

    // 실패 케이스
    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일 중복")
    void signup_fail_when_email_is_duplicated(){
        //given (주어진 상황)
        //회원가입 DTO 만듬
        UserSignupRequestDto requestDto = new UserSignupRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password123");
        requestDto.setUsername("tester");

        // 가짜 UserRepo가 findbyEmail호출 시 '이미 가입된 USer가 없는 사오항' 연기
        User existingUser = new User("test@example.com", "anypassword", "anyname");
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(existingUser));

        //when &then(실행과 동시에 예외 검증)
        // userService.signup실행시, BusinessException이 발생 기대, 실제로 발생시 그 예외 객체를 'exception' 변수에 담기.
        BusinessException exception = assertThrows(BusinessException.class, () ->{
            userService.signup(requestDto);
        });

        //then (추가 검증)
        // 1. 발생한 예외의 ErrorCode가 EMAIL_DUPLICATION이 맞는지 확인합니다.
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_DUPLICATION);
        // 2. 이메일이 중복되었으므로, save 메소드는 '절대 호출되지 않았어야' 하는지 확인합니다.
        verify(userRepository, never()).save(any(User.class));
    }
}
