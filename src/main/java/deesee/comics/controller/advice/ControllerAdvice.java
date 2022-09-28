package deesee.comics.controller.advice;

import deesee.comics.controller.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDto> handleUnexpectedException(Exception e) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorDto> handleUnexpectedException(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String validationErrorMessage = "";
        try {
            validationErrorMessage = ex.getAllErrors().stream()
                    .filter(objectError -> objectError.getArguments() != null && objectError.getDefaultMessage() != null)
                    .map(objectError -> {
                        StringBuilder result = new StringBuilder();
                        var argument = (DefaultMessageSourceResolvable) objectError.getArguments()[0];
                        result.append(argument.getDefaultMessage());
                        result.append(" ");
                        result.append(objectError.getDefaultMessage());
                        return result;
                    })
                    .collect(Collectors.joining(", "));
        } catch (Exception e) {
            log.debug("An error occurred while generating 'validationErrorMessage'", e);
        }
        log.info("An error occurred while validation arguments! Error: {}", validationErrorMessage);

        return ResponseEntity.badRequest().body(new ErrorDto(validationErrorMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.info("An error occurred while reading HTTP message! Error: {}", ex.getLocalizedMessage());

        return ResponseEntity.badRequest().body(new ErrorDto(ex.getLocalizedMessage()));
    }
}