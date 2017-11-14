package dto;

public class LoginSuccessDTO {

    private String message;
    private String token;
    private String valid_till;

    public LoginSuccessDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getValid_till() {
        return valid_till;
    }

    public void setValid_till(String valid_till) {
        this.valid_till = valid_till;
    }

    @Override
    public String toString() {
        return "LoginSuccessDTO{" +
                "message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", valid_till='" + valid_till + '\'' +
                '}';
    }
}
