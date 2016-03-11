package cpp.scottl.com.handleit;
/**
 * Created by rosie_000 on 2/19/2016.
 */
public class User {

    private String email;
    private String username;
    private String userId;

    public User() {
    }

    public User(String email, String username, String userId) {
        this.username = username;
        this.userId = userId;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
