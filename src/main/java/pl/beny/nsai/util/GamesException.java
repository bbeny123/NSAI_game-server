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
        USER_ALREADY_ACTIVE(9, "User connected with this email is already active", HttpStatus.CONFLICT),

        BATTLESHIP_WRONG_SIZE(10, "The ship of that size does not exist", HttpStatus.BAD_REQUEST),
        BATTLESHIP_SIZE_NOT_AVAILABLE(11, "Too many ships of that size", HttpStatus.BAD_REQUEST),
        BATTLESHIP_OFF_THE_BOARD(12, "Action off the board", HttpStatus.BAD_REQUEST),
        BATTLESHIP_WRONG_PLACEMENT(13, "The ship can not be placed here", HttpStatus.BAD_REQUEST),
        BATTLESHIP_PLACE_FIRED(14, "This place was already fired", HttpStatus.BAD_REQUEST),
        BATTLESHIP_NOT_PREPARING(15, "You can not place ship outside PREPARING phase", HttpStatus.BAD_REQUEST),
        BATTLESHIP_NOT_BATTLE(16, "You can not fire outside BATTLE phase", HttpStatus.BAD_REQUEST),
        BATTLESHIP_DIAGONAL(17, "You can not place ships diagonally", HttpStatus.BAD_REQUEST),

        CHECKERS_NO_MAN(18, "There is no man at this position", HttpStatus.BAD_REQUEST),
        CHECKERS_OPPOSITE_MAN(19, "This is no player's man", HttpStatus.BAD_REQUEST),
        CHECKERS_POSITION_TAKEN(20, "This position is already taken", HttpStatus.BAD_REQUEST),
        CHECKERS_POSITION_PROHIBITED(21, "This position is prohibited", HttpStatus.BAD_REQUEST),
        CHECKERS_CAPTURE_FORCED(22, "This man is forced to capture", HttpStatus.BAD_REQUEST),
        CHECKERS_COMPUTER_TURN(23, "Computer turn", HttpStatus.BAD_REQUEST),
        CHECKERS_PLAYER_TURN(24, "Player turn", HttpStatus.BAD_REQUEST),
        CHECKERS_ERROR(25, "Internal OLO error", HttpStatus.INTERNAL_SERVER_ERROR),

        AI_ERROR(99, "Sorry, our OLO AI is dumb as ...", HttpStatus.INTERNAL_SERVER_ERROR);

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