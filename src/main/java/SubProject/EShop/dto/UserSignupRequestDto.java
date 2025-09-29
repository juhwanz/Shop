package SubProject.EShop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@Getter
@Setter // DTO는 데이터를 받고 설정해야 하므로 Setter도 추가해야함.
public class UserSignupRequestDto {
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8글자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "사용자 이름은 필수 입력입니다.")
    private String username;
}
