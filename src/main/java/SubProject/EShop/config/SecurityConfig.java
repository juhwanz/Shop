package SubProject.EShop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 이 파일은 문제 진단을 위한 최소한의 설정입니다.
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // private final JwtAuthenticationFilter jwtAuthenticationFilter; // 👈 일단 JWT 필터를 주석 처리하여 완전히 비활성화합니다.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // 모든 복잡한 설정을 제거하고, 오직 GET 요청만 허용하는 규칙을 적용합니다.
                .authorizeHttpRequests(authz -> authz
                        // GET 요청으로 "/", "/index.html", "/app.js" 주소에만 접근을 허용합니다.
                        .requestMatchers(HttpMethod.GET, "/", "/index.html", "/app.js").permitAll()
                        // 그 외 나머지 모든 요청은 일단 거부하여 원인을 명확히 합니다.
                        .anyRequest().denyAll()
                );
        // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 👈 필터 적용을 비활성화합니다.

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // PasswordEncoder는 다른 곳에서 필요할 수 있으므로 그대로 둡니다.
        return new BCryptPasswordEncoder();
    }
}

