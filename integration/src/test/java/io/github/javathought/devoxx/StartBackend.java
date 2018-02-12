package io.github.javathought.devoxx;

//import io.github.javathought.devoxx.dao.Connexion;
//import io.github.javathought.devoxx.filters.CrossDomainFilter;
//import io.github.javathought.devoxx.filters.SecurityFilter;
//import io.swagger.jaxrs.config.BeanConfig;
//import io.swagger.jaxrs.listing.ApiListingResource;
//import io.swagger.jaxrs.listing.SwaggerSerializers;
//import org.glassfish.grizzly.http.server.HttpServer;
//import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
//import org.glassfish.jersey.jackson.JacksonFeature;
//import org.glassfish.jersey.message.filtering.SelectableEntityFilteringFeature;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.server.filter.CsrfProtectionFilter;
//import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
//import org.jruby.ir.instructions.MatchInstr;
//import org.slf4j.bridge.SLF4JBridgeHandler;

import io.github.javathought.devoxx.dao.Connexion;
import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

//import static io.github.javathought.devoxx.BDDRunnerTest.mysql;

/**
 * ApiServer class.
 *
 */
public class StartBackend {
    // Base URI the Grizzly HTTP server will listen on

    public static String jdbcUrl = "jdbc:tc:mysql:5.7://hostname/databasename?" +
            "TC_INITFUNCTION=io.github.javathought.devoxx.StartBackend::initDB";
    public static String jdbcUrlInitiated = "jdbc:tc:mysql:5.7://hostname/databasename";


    public static void initDB(Connection connection) throws SQLException {
        // e.g. run schema setup or Flyway/liquibase/etc DB migrations here...

        // Create the Flyway instance
        Flyway flyway = new Flyway();

        // Point it to the database


        flyway.setDataSource(connection.getMetaData().getURL(), "root", "test");

        // Start the migration
        flyway.migrate();
    }

//    public static start() {
//        io.github.javathought.devoxx.
//    }

}

