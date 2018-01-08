package lamngo.financialtool.UserAccount;

/**
 * Created by lamngo on 9.1.2018.
 */

public class User {
    private int userId;
    private String userName;
    private String email;
    private String userRole;

    public User() {}

    public User(int userId, String userName, String email, String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.userRole = userRole;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", userRole='" + userRole + '\'' +
                '}';
    }
}
