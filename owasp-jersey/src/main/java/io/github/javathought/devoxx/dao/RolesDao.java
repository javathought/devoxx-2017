package io.github.javathought.devoxx.dao;

import io.github.javathought.devoxx.model.Role;
import io.github.javathought.devoxx.model.User;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

import static io.github.javathought.devoxx.db.Tables.ROLES;
import static io.github.javathought.devoxx.db.Tables.USERS;
import static io.github.javathought.devoxx.db.Tables.USER_ROLES;

/**
 *
 */
public class RolesDao {

    private static final Logger LOG = LoggerFactory.getLogger(UsersDao.class);
    private static Connection conn = Connexion.getInstance().getDbConnection();

    public static List<Role> getUserRoles(User user) {
        return DSL.using(conn).
                select().from(USER_ROLES)
                .join(ROLES).on(ROLES.ID.eq(USER_ROLES.ROLE_ID))
                .join(USERS).on(USERS.ID.eq(USER_ROLES.USER_ID)
                    .and(USERS.ID.eq(user.getId())))
                .fetch()
                .map(record -> new Role(record.get(ROLES.NAME)));
    }

}
