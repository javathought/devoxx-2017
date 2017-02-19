package io.github.javathought.devoxx.dao;

import io.github.javathought.devoxx.model.Credentials;
import io.github.javathought.devoxx.model.User;
import io.github.javathought.devoxx.security.PasswordStorage;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
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
    private static final int BEARER_BYTE_SIZE = 32;
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
        if (StringUtils.isEmpty(user.getKey())) {
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

    public static byte[] getBearer(Credentials credentials) {
        SecureRandom random = new SecureRandom();
        byte[] bearer = new byte[BEARER_BYTE_SIZE];
        random.nextBytes(bearer);

        DSL.using(conn).update(USERS)
                .set(USERS.BEARER, bearer)
                .where(USERS.NAME.eq(credentials.getUsername()))
                .execute();


        return bearer;
    }

    public static Optional<User> getByBearer(byte[] bearer) {
        return DSL.using(conn).selectFrom(USERS)
                .where(USERS.BEARER.eq(bearer))
                .fetchOptional()
                .map(UsersDao::mapUser);
    }
}
