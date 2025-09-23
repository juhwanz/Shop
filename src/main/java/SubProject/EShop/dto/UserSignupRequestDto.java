package SubProject.EShop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // DTO는 데이터를 받고 설정해야 하므로 Setter도 추가해야함.
public class UserSignupRequestDto {
    private String email;
    private String password;
    private String username;
}
