package cl.gfmn.authserver.exception;

import cl.gfmn.authserver.model.ErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    private final Gson gson = new Gson();

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ErrorResponse> handle400Exceptions(RuntimeException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now().toString(), ex.getMessage(), request.getRequestURI());

        logger.error("an error 400 has occurred = {}", gson.toJson(errorResponse));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now().toString(), ex.getMessage(), request.getRequestURI());

        logger.error("an error 500 has occurred = {}", gson.toJson(errorResponse));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
