package pl.beny.nsai.dto;

public class ResponseWrapper {

    private Long userId;
    private Object response;

    public ResponseWrapper(Long userId, Object response) {
        this.userId = userId;
        this.response = response;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
