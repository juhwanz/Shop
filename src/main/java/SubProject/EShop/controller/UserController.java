package SubProject.EShop.controller;

import SubProject.EShop.domain.User;
import SubProject.EShop.dto.UserLoginRequestDto;
import SubProject.EShop.dto.UserSignupRequestDto;
import SubProject.EShop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        Long userId = userService.signup(requestDto);
        return ResponseEntity.ok("회원가입 성공. 사용자 ID: " + userId);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto requestDto){
        String token = userService.login(requestDto);
        return ResponseEntity.ok("로그인 성공. 사용자 이름: " + token);
    }
}
