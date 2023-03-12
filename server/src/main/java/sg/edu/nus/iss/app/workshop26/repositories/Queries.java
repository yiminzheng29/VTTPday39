package sg.edu.nus.iss.app.workshop26.repositories;

public class Queries {
    
    public static final String SQL_FIND_USER_BY_EMAIL = """
            select * from employees where email_address = ?
            """;
}
