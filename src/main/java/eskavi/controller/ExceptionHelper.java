package eskavi.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.googlejavaformat.java.FormatterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ExceptionHelper {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);


    @ExceptionHandler(value = {FormatterException.class})
    public ResponseStatusException handleFormatterException(FormatterException ex) {
        logger.error("Invalid Java syntax exception: {}", ex.getMessage());
        //return new ResponseEntity<Object>("Invalid Java syntax exception", HttpStatus.BAD_REQUEST);
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Java syntax exception");
    }

    @ExceptionHandler(value = {JWTVerificationException.class})
    public ResponseStatusException handleJWTVerificationException(JWTVerificationException ex) {
        logger.error("Invalid token: {}", ex.getMessage());
        //return new ResponseEntity<Object>("Invalid token", HttpStatus.UNAUTHORIZED);
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
    }
}
