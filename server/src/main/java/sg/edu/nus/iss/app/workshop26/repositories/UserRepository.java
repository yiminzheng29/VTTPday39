package sg.edu.nus.iss.app.workshop26.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.app.workshop26.models.User;
import static sg.edu.nus.iss.app.workshop26.repositories.Queries.*;


@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate template;

    public Optional<User> findUserByEmail(String email) {
        SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_BY_EMAIL, email);

        if (!rs.next())
            return Optional.empty();
        
        return Optional.of(User.create(rs));
    }
}
