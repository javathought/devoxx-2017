package io.github.javathought.devoxx.filters;

import io.github.javathought.devoxx.dao.RolesDao;
import io.github.javathought.devoxx.dao.UsersDao;
import io.github.javathought.devoxx.model.User;
import io.github.javathought.devoxx.resources.AuthenticateResource;
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
    public static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Bearer";
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

        LOG.trace("URI : {}", requestContext.getUriInfo().getPath());

        //If no authorization information present; block access
        if( !(authorization == null) && ! requestContext.getUriInfo().getPath().equalsIgnoreCase(AuthenticateResource.PATH))
        {

            //Get encoded username and password
            final String bearerCookie = authorization.getValue().replaceFirst(AUTHENTICATION_SCHEME + " ", "");

            //Decode username and password
            byte[] bearer = Base64.decode(bearerCookie.getBytes());

            User user = UsersDao.getByBearer(bearer).orElseThrow(() -> new WebApplicationException(Response.Status.UNAUTHORIZED));

            user.setRoles(RolesDao.getUserRoles(user));

            LOG.trace("User accessing resource : {}", user);

            requestContext.setSecurityContext(new ApiSecurityContext(user));
        }


    }
}
