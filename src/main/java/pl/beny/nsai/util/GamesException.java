package pl.beny.nsai.util;

import org.springframework.http.HttpStatus;

public class GamesException extends Exception {

    public enum GamesErrors {
        NOT_AUTHORIZED(1, "Unauthorized", HttpStatus.UNAUTHORIZED),
        CAPTCHA_ERROR(2, "Captcha Error", HttpStatus.FORBIDDEN),
        USER_EXISTS(3, "The e-mail address is already in use", HttpStatus.CONFLICT),
        ITEM_NOT_EXISTS(4, "The item does not exist in database", HttpStatus.NOT_FOUND),
        USER_NOT_EXISTS(5, "The e-mail does not exist in database", HttpStatus.NOT_FOUND),
        EMAIL_NOT_EXISTS(6, "The e-mail does not exist in database", HttpStatus.NOT_FOUND),
        TOKEN_NOT_EXISTS(7, "The token does not exist in database", HttpStatus.NOT_FOUND),
        ROLE_NOT_EXISTS(8, "Role does not exist!", HttpStatus.NOT_FOUND),
        USER_ALREADY_ACTIVE(9, "User connected with this email is already active", HttpStatus.CONFLICT);

        private int code;
        private String message;
        private HttpStatus httpStatus;

        GamesErrors(int code, String message, HttpStatus httpStatus) {
            this.code = code;
            this.message = message;
            this.httpStatus = httpStatus;
        }
    }

    private final GamesErrors error;

    public GamesException(GamesErrors error) {
        super(error.message);
        this.error = error;
    }

    public int getErrorCode() {
        return error.code;
    }

    public int getHttpCode() {
        return error.httpStatus.value();
    }

    @Override
    public String toString() {
        return error.code + ": " + error.message;
    }

}