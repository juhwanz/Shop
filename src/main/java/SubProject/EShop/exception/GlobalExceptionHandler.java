package SubProject.EShop.exception;

import SubProject.EShop.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler{

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    // @Valid 유효성 검사 실패 시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.warn("handleMethodArgumentNotValidException", e); //Warn 레벨로 로그 기록
        final ErrorResponse response = new ErrorResponse(
                ErrorCode.INVALID_INPUT_VALUE.getStatus().value(),
                ErrorCode.INVALID_INPUT_VALUE.getCode(),
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        );
        return new ResponseEntity<>(response, ErrorCode.INVALID_INPUT_VALUE.getStatus());
    }

    // 우리가 직접 정의한 비즈니스 예외 처리
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e){
        log.warn("handleBusinessException", e); // WARN 레벨로 로그 기록
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = new ErrorResponse(
                errorCode.getStatus().value(),
                errorCode.getCode(),
                errorCode.getMessage()
        );
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    // 나머지 모든 예외 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e){
        log.error("handleEntityNotFoundException", e); // ERROR 레벨로 가장 심각한 예외 기록
        final ErrorResponse response = new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value(),
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                e.getMessage()
        );
        return new ResponseEntity<>(response, ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }
}




/* 예외 처리 및 에러 응답 통일 전
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SoldOutException.class)
    public ResponseEntity<String> handleSoldOutException(SoldOutException ex) {
        // "재고 없음" 예외가 발생하면, HTTP 409 Conflict 상태 코드와 함께 에러 메시지를 반환
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // 409 conflict
    }

    // Valid 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex){
        // 검증 실패 결과에서 메세지만 모아서 하나의 문자열로 합침
        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST); // 400
    }
}*/
