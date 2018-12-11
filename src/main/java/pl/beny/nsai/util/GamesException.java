package pl.beny.nsai.util;

import org.springframework.http.HttpStatus;

public class GamesException extends RuntimeException {

    public enum GamesErrors {
        FORBIDDEN(0, "Forbidden", HttpStatus.FORBIDDEN),
        UNAUTHORIZED(1, "Unauthorized", HttpStatus.UNAUTHORIZED),
        CAPTCHA_ERROR(2, "Captcha Error", HttpStatus.FORBIDDEN),
        USER_EXISTS(3, "The e-mail address is already in use", HttpStatus.CONFLICT),
        ITEM_NOT_EXISTS(4, "The item does not exist in database", HttpStatus.NOT_FOUND),
        EMAIL_NOT_EXISTS(5, "The e-mail does not exist in database", HttpStatus.NOT_FOUND),
        TOKEN_NOT_EXISTS(6, "The token does not exist in database", HttpStatus.NOT_FOUND),
        USER_ALREADY_ACTIVE(7, "User connected with this email is already active", HttpStatus.CONFLICT),

        DIFFICULTY_INCORRECT(99, "Incorrect difficulty level", HttpStatus.BAD_REQUEST),
        GAME_NOT_FOUND(100, "Game not found", HttpStatus.NOT_FOUND),

        BATTLESHIP_WRONG_SIZE(101, "The ship of that size does not exist", HttpStatus.BAD_REQUEST),
        BATTLESHIP_SIZE_NOT_AVAILABLE(102, "Too many ships of that size", HttpStatus.BAD_REQUEST),
        BATTLESHIP_OFF_THE_BOARD(103, "Action off the board", HttpStatus.BAD_REQUEST),
        BATTLESHIP_WRONG_PLACEMENT(104, "The ship can not be placed here", HttpStatus.BAD_REQUEST),
        BATTLESHIP_PLACE_FIRED(105, "This place was already fired", HttpStatus.BAD_REQUEST),
        BATTLESHIP_NOT_PREPARING(106, "You can not place ship outside PREPARING phase", HttpStatus.BAD_REQUEST),
        BATTLESHIP_NOT_BATTLE(107, "You can not fire outside BATTLE phase", HttpStatus.BAD_REQUEST),
        BATTLESHIP_DIAGONAL(108, "You can not place ships diagonally", HttpStatus.BAD_REQUEST),

        CHECKERS_NO_MAN(151, "There is no man at this position", HttpStatus.BAD_REQUEST),
        CHECKERS_OPPOSITE_MAN(152, "This is no player's man", HttpStatus.BAD_REQUEST),
        CHECKERS_POSITION_TAKEN(153, "This position is already taken", HttpStatus.BAD_REQUEST),
        CHECKERS_POSITION_PROHIBITED(154, "This position is prohibited", HttpStatus.BAD_REQUEST),
        CHECKERS_NOT_ALLOWED(155, "This move is not allowed", HttpStatus.BAD_REQUEST),
        CHECKERS_CAPTURE_FORCED_STATE(156, "This man is forced to capture", HttpStatus.BAD_REQUEST),
        CHECKERS_CAPTURE_FORCED(157, "Forced to capture state", HttpStatus.BAD_REQUEST),
        CHECKERS_COMPUTER_TURN(158, "Computer turn", HttpStatus.BAD_REQUEST),
        CHECKERS_PLAYER_TURN(159, "Player turn", HttpStatus.BAD_REQUEST),
        CHECKERS_ERROR(160, "Internal OLO error", HttpStatus.INTERNAL_SERVER_ERROR),

        AI_ERROR(99, "Sorry, our OLO AI is dumb as ...", HttpStatus.INTERNAL_SERVER_ERROR),

        INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

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