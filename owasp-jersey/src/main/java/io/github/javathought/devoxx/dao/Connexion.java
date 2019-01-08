package io.github.javathought.devoxx.dao;

import io.github.javathought.devoxx.filters.CrossDomainFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

/**
 *
 */
public class Connexion {
    private static Connexion ourInstance = new Connexion();

    private static final Logger LOG = LoggerFactory.getLogger(CrossDomainFilter.class);


    private Connection dbConnection;

    public static Connexion getInstance() {
        return ourInstance;
    }

    private Connexion() {
    }

    public Connexion setConnection(String dbUrl, String user, String pwd) {
        try {
//            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/devoxx_tia",
            ourInstance.dbConnection = DriverManager.getConnection(
                    dbUrl, user, pwd);
        } catch (SQLException e) {
            LOG.error("Unable to create connection", e);
        }
        return this;
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

    public static byte[] uuidToBytes(UUID uuid) {
        return ByteBuffer.wrap(new byte[16])
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits()).array();
    }

    public static UUID asUuid(byte[] bytes){
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }


    public Connexion init() {
        try {
//            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/devoxx_tia",
            dbConnection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/devoxx_tia?useJDBCCompliantTimezoneShift=true" +
                            "&useLegacyDatetimeCode=false&serverTimezone=Europe/Paris&autoReconnect=true",
                    "devoxx", "owasp-2017;");
        } catch (SQLException e) {
            LOG.error("Unable to create connection", e);
        }

        return this;
    }
}
