package pl.beny.nsai.dto;

import org.springframework.http.HttpStatus;
import pl.beny.nsai.util.GamesException;

public class ExceptionResponse {

    private String message;
    private int code = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public ExceptionResponse() {
    }

    public ExceptionResponse(GamesException ex) {
        this.message = ex.getMessage();
        this.code = ex.getHttpCode();
    }

    public ExceptionResponse(Exception ex) {
        this.message = ex.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
