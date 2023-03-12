package sg.edu.nus.iss.app.workshop26.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public class User {
    private int userId;
    private String name;
    private String email;

    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public static User create (SqlRowSet rs) {
        User user = new User();
        user.setUserId(rs.getInt("id"));
        user.setName("%s %s".formatted(rs.getString("first_name"), rs.getString("last_name")));
        user.setEmail(rs.getString("email_address"));
        return user;
    }

    
}
