package Model;

public class Person {
    static Integer staticId = 0;
    private Integer id = ++staticId;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    private String username;
    private String email;
    private String password;

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "( " + id + " \n email : " + email + " \n username: " + username + "\n password: " + password + "\n )";
    }
}

