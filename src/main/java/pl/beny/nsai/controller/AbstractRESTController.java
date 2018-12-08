package pl.beny.nsai.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.beny.nsai.dto.ExceptionResponse;
import pl.beny.nsai.model.UserContext;
import pl.beny.nsai.util.ContextHolder;
import pl.beny.nsai.util.GamesException;

public abstract class AbstractRESTController {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @ExceptionHandler(GamesException.class)
    public ResponseEntity<?> amaException(GamesException ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(ex.getHttpCode()).body(new ExceptionResponse(ex));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validException(MethodArgumentNotValidException ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(ex));
    }

    protected UserContext getUserContext() {
        return ContextHolder.getUserContext();
    }

    protected ResponseEntity<?> ok() {
        return ResponseEntity.ok().build();
    }

    protected <T> ResponseEntity<T> ok(T t) {
        return ResponseEntity.ok(t);
    }

}
