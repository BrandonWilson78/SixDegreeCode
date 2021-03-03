package app.sixdegree.model;

public class User {

    private String email;
    private String password;
    private String token;
    public User(String email, String password, String token) {
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}