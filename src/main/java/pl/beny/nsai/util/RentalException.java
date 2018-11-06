package pl.beny.nsai.util;

public class RentalException extends Exception {

    public enum RentalErrors {
        NOT_AUTHORIZED(1, "Not authorized", "error.authorized", null),
        CAPTCHA_ERROR(2, "Captcha Error", "error.captcha", null),
        USER_EXISTS(3, "The e-mail address is already in use", "error.user.exists", null),
        ITEM_NOT_EXISTS(4, "The item does not exist in database", "error.item.not.exists", null),
        USER_NOT_EXISTS(5, "The e-mail does not exist in database", "error.user.not.exists", null),
        EMAIL_NOT_EXISTS(6, "The e-mail does not exist in database", "error.user.not.exists", "/register/resend"),
        TOKEN_NOT_EXISTS(7, "The token does not exist in database", "error.token.not.exists", "/register/resend"),
        ROLE_NOT_EXISTS(8, "Role does not exist!", "error.role.not.exists", null),
        USER_ALREADY_ACTIVE(9, "User connected with this email is already active", "error.user.active", "/login");

        private int code;
        private String message;
        private String source;
        private String url;

        RentalErrors(int code, String message, String source, String url) {
            this.code = code;
            this.message = message;
            this.source = source;
            this.url = url;
        }
    }

    private final RentalErrors error;

    public RentalException(RentalErrors error) {
        super(error.message);
        this.error = error;
    }

    public int getErrorCode() {
        return error.code;
    }

    public String getMessageSource() {
        return error.source;
    }

    public String getUrl() {
        return error.url;
    }

    @Override
    public String toString() {
        return error.code + ": " + error.message;
    }

}