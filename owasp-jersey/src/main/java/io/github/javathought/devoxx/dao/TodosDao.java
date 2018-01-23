package io.github.javathought.devoxx.dao;

import io.github.javathought.devoxx.model.Todo;
import io.github.javathought.devoxx.model.User;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.github.javathought.devoxx.dao.Connexion.uuidToBytes;
import static io.github.javathought.devoxx.dao.Connexion.asUuid;
import static io.github.javathought.devoxx.db.Tables.TODOS;
import static org.jooq.tools.StringUtils.defaultIfNull;

/**
 *
 */
public class TodosDao {

    private static final Logger LOG = LoggerFactory.getLogger(TodosDao.class);
    private static Connection conn = Connexion.getInstance().getDbConnection();

    public static List<Todo> getAll() {
        return DSL.using(conn).
                fetch(TODOS)
                .map(record -> mapTodo(record));
    }

    public static Optional<Todo> getById(UUID id) {
        return DSL.using(conn).selectFrom(TODOS)
                .where(TODOS.UUID.eq(uuidToBytes(id)))
                .fetchOptional()
                .map(TodosDao::mapTodo);
    }

    public static Optional<Todo> getByUuidAndUser(UUID id, User userPrincipal) {
        return DSL.using(conn).selectFrom(TODOS)
                .where(TODOS.UUID.eq(uuidToBytes(id)))
                .and(TODOS.USER_ID.eq(userPrincipal.getId()))
                .fetchOptional()
                .map(TodosDao::mapTodo);
    }


    public static void create(Todo todo) {
        long id = DSL.using(conn).insertInto(TODOS,
                TODOS.UUID,
                TODOS.USER_ID,
                TODOS.SUMMARY,
                TODOS.DESCRIPTION
        )
                .values (
                        uuidToBytes(UUID.randomUUID()),
                        todo.getUserId(),
                        todo.getSummary(),
                        todo.getDescription()
                )
                .returning(TODOS.ID).fetchOne().getId().intValue();

        todo.setId(id);
    }

    public static void update(Todo todo) {
            DSL.using(conn).update(TODOS)
                    .set(TODOS.SUMMARY, todo.getSummary())
                    .set(TODOS.DESCRIPTION, todo.getDescription())
                    .where(TODOS.UUID.eq(uuidToBytes(todo.getUuid())))
                    .execute();
    }

    public static void delete(UUID id) {
        DSL.using(conn).delete(TODOS)
                .where(TODOS.UUID.eq(uuidToBytes(id)))
                .execute();
    }

    public static List<Todo> getByOwner(long userId) {
        return DSL.using(conn).selectFrom(TODOS)
                .where(TODOS.USER_ID.eq(userId))
                .fetch()
                .map(TodosDao::mapTodo);
    }

    public static Todo mapTodo(Record record) {
        Todo todo = new Todo(
                record.get(TODOS.ID),
                asUuid(record.get(TODOS.UUID)),
                defaultIfNull(record.get(TODOS.USER_ID),0L),
                record.get(TODOS.SUMMARY),
                record.get(TODOS.DESCRIPTION));
        LOG.trace("todo returned : {}", todo);
        return todo ;
    }

}
