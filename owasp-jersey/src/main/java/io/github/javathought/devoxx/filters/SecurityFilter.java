package io.github.javathought.devoxx.filters;

import io.github.javathought.devoxx.dao.RolesDao;
import io.github.javathought.devoxx.dao.UsersDao;
import io.github.javathought.devoxx.model.User;
import io.github.javathought.devoxx.security.ApiSecurityContext;
import org.glassfish.jersey.internal.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {


    private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
            .entity("You cannot access this resource").build();

    public static final String USER_ID = "userid";
    public static final String ROLES = "roles";

//    @PostConstruct
    public void init() {
        LOG.info("Initializing Custom Authorization Filter...");
    }

    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        //Get request headers
        final Map<String, Cookie> cookies = requestContext.getCookies();

        //Fetch authorization header
        final Cookie authorization = cookies.get(AUTHORIZATION_PROPERTY);

        //If no authorization information present; block access
        if(! (authorization == null))
        {

            //Get encoded username and password
            final String encodedUserPassword = authorization.getValue().replaceFirst(AUTHENTICATION_SCHEME + " ", "");

            //Decode username and password
            String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;

            //Split username and password tokens
            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            final String login = tokenizer.nextToken();
            final String password = tokenizer.nextToken();

            User user = UsersDao.getByName(login).orElseThrow(() -> new WebApplicationException(Response.Status.UNAUTHORIZED));

            user.setRoles(RolesDao.getUserRoles(login));

            LOG.trace("User accessing resource : {}", user);

            requestContext.setSecurityContext(new ApiSecurityContext(user));
        }


    }
}
