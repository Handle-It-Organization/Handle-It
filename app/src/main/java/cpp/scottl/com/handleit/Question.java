package cpp.scottl.com.handleit;

/**
 * Created by Scott on 2/26/2016.
 */
public class Question {
    private String title;
    private String description;
    private String category;
    private String username;
    private String userId;
    private String userEmail;
    private String complete;
    private String date;
    private String photo;
    private String comments;

    public Question(){
    }

    public Question(String title, String description, String category, String username,
                    String userId, String userEmail, String complete, String date, String photo){
        this.title = title;
        this.description = description;
        this.category = category;
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;
        this.complete = complete;
        this.date = date;
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getPhoto() { return photo; }

    public void setPhoto(String photo) {this.photo = photo; }


}
