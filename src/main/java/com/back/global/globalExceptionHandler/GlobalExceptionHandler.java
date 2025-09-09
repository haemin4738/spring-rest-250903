package com.back.global.globalExceptionHandler;

import com.back.global.rsData.RsData;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<RsData<Void>> handle(NoSuchElementException e) {
        return new ResponseEntity<>(
                new RsData<>(
                        "404-1",
                        "존재하지 않는 데이터에 접근했습니다."
                ),
                NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RsData<Void>> handle(MethodArgumentNotValidException e) {
// 예외 객체 e 에서 BindingResult 꺼냄 (검증 에러 결과)
        String message = e.getBindingResult()
                // 모든 에러 객체 가져오기 (ObjectError / FieldError 포함)
                .getAllErrors()
                .stream()
                // FieldError 타입만 필터링 (필드 검증 에러만 추출)
                .filter(error -> error instanceof FieldError)
                // ObjectError → FieldError 로 캐스팅
                .map(error -> (FieldError) error)
                // 에러를 "필드명-코드-기본메시지" 형태의 문자열로 변환
                // 예: "title-NotBlank-공백일 수 없습니다"
                .map(error -> error.getField() + "-" + error.getCode() + "-" + error.getDefaultMessage())
                // 정렬 (필드명/코드/메시지 기준 문자열 오름차순)
                .sorted(Comparator.comparing(String::toString))
                // 여러 문자열을 개행(\n)으로 이어붙임
                .collect(Collectors.joining("\n"));

        return new ResponseEntity<>(
                new RsData<>(
                        "400-1",
                        message
                ),
                BAD_REQUEST
        );
    }
}