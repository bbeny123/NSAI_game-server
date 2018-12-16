package pl.beny.nsai.dto.user;

public class CaptchaResponse {

    private boolean success;    //indicates if captcha request was successful

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
