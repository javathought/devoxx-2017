package io.github.javathought.devoxx.dao;

import io.github.javathought.devoxx.model.Credentials;
import io.github.javathought.devoxx.model.User;
import io.github.javathought.devoxx.security.PasswordStorage;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.javathought.devoxx.dao.Connexion.UUIDToBytes;
import static io.github.javathought.devoxx.db.Tables.USERS;

/**
 *
 */
public class UsersDao {

    private static final Logger LOG = LoggerFactory.getLogger(UsersDao.class);
    private static Connection conn = Connexion.getInstance().getDbConnection();

    public static List<User> getAll() {
        return DSL.using(conn).
                fetch(USERS)
                .map(record -> mapUser(record));
    }

    public static Optional<User> getById(long id) {
        return DSL.using(conn).selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptional()
                .map(UsersDao::mapUser);
    }

    public static Optional<User> getByName(String name) {
        return DSL.using(conn).selectFrom(USERS)
                .where(USERS.NAME.eq(name))
                .fetchOptional()
                .map(UsersDao::mapUser);
    }

    public static void create(User user) {
        long id = DSL.using(conn).insertInto(USERS,
                USERS.UUID,
                USERS.NAME,
                USERS.PASSWORD
        )
                .values (
                        UUIDToBytes(UUID.randomUUID()),
                        user.getName(),
                        user.getKey()
                )
                .returning(USERS.ID).fetchOne().getId().intValue();

        user.setId(id);
    }

    public static void update(User user) {
        if (user.getKey().isEmpty()) {
            DSL.using(conn).update(USERS)
                    .set(USERS.NAME, user.getName())
                    .where(USERS.ID.eq(user.getId()))
                    .execute();
        } else {
            DSL.using(conn).update(USERS)
                    .set(USERS.NAME, user.getName())
                    .set(USERS.PASSWORD, user.getKey())
                    .where(USERS.ID.eq(user.getId()))
                    .execute();
        }
    }

    public static void delete(long id) {
        DSL.using(conn).delete(USERS)
                .where(USERS.ID.eq(id)).execute();
    }

    public static boolean authenticate(Credentials credentials) {
        boolean authenticated = false;
        Optional<String> hashOpt = DSL.using(conn).select(USERS.PASSWORD).from(USERS)
                .where(USERS.NAME.eq(credentials.getUsername()))
                .fetchOptional().map(r -> r.get(USERS.PASSWORD));

        if (hashOpt.isPresent()) {
            try {
                authenticated = PasswordStorage.verifyPassword(credentials.getPassword(), hashOpt.get());
            } catch (PasswordStorage.InvalidHashException | PasswordStorage.CannotPerformOperationException e) {
                LOG.error("Erreur to auth {}");
                authenticated = false;
            }
        }

        return authenticated;
    }

    public static User mapUser(Record record) {
        User user = new User(record.get(USERS.ID),
                UUID.nameUUIDFromBytes(record.get(USERS.UUID)),
                record.get(USERS.NAME));
        LOG.trace("user returned : {}", user);
        return user ;
    }

}
