package SubProject.EShop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SoldOutException.class)
    public ResponseEntity<String> handleSoldOutException(SoldOutException ex) {
        // "재고 없음" 예외가 발생하면, HTTP 409 Conflict 상태 코드와 함께 에러 메시지를 반환
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // 409 conflict
    }

    // Valid 예외 처리
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex){
        // 검증 실패 결과에서 메세지만 모아서 하나의 문자열로 합침
        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST); // 400
    }
}
