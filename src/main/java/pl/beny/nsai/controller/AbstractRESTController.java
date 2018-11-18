package pl.beny.nsai.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.beny.nsai.dto.ExceptionResponse;
import pl.beny.nsai.util.GamesException;

public abstract class AbstractRESTController {

    private Logger logger = LogManager.getLogger(this.getClass());

    @ExceptionHandler(GamesException.class)
    public ResponseEntity<?> gamesException(GamesException ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(ex.getHttpCode()).body(new ExceptionResponse(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception ex) {
        logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(ex));
    }

    protected ResponseEntity<?> ok() {
        return ResponseEntity.ok().build();
    }

}
