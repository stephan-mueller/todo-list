package de.openknowledge.projects.todolist.rest.infrastructure.rest.filter;

import org.wildfly.swarm.keycloak.deployment.KeycloakSecurityContextAssociation;

import java.io.IOException;
import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 * JAX-RS filter that rewrites the principal name in the {@link SecurityContext}.
 *
 * Keycloak sets a principal name to the access token subject which is a unique (UUID/etc) identifier.
 * The custom security context sets the principal name to the preferred user name instead.
 */
@Provider
public class KeycloakSecurityContextFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        SecurityContext securityContext = requestContext.getSecurityContext();
        Principal principal = () -> KeycloakSecurityContextAssociation.get().getToken().getPreferredUsername();
        
        requestContext.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {
                return principal;
            }

            @Override
            public boolean isUserInRole(String role) {
                return securityContext.isUserInRole(role);
            }

            @Override
            public boolean isSecure() {
                return securityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return securityContext.getAuthenticationScheme();
            }            
        });
    }
}
