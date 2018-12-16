package pl.beny.nsai.dto;

//required to proper working of AI listener
public class ResponseWrapper {

    private Long userId;        //userId
    private Object response;    //AI result

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
