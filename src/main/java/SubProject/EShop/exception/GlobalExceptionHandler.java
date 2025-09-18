package SubProject.EShop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SoldOutException.class)
    public ResponseEntity<String> handleSoldOutException(SoldOutException ex) {
        // "재고 없음" 예외가 발생하면, HTTP 409 Conflict 상태 코드와 함께 에러 메시지를 반환
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // 409 conflict
    }
}
