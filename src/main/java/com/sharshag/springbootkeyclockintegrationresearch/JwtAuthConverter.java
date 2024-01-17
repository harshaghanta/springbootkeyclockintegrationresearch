package com.sharshag.springbootkeyclockintegrationresearch;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;
    
    @Value("${jwt.auth.converter.principal-attribute}")
    private String principalAttribute;
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Stream<GrantedAuthority> grantedAuthorities = grantedAuthoritiesConverter.convert(jwt).stream();

        Stream<? extends GrantedAuthority> resourceRoles = extractResourceRoles(jwt).stream();

        Stream<GrantedAuthority> authorities = Stream.concat(grantedAuthorities, resourceRoles);

        String principalClaimName = getPrincipalClaimName(jwt);
        return new JwtAuthenticationToken(jwt, authorities.collect(Collectors.toSet()), principalClaimName);
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if(principalAttribute != null) {
            claimName = principalAttribute;
        }
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {

        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Collection<String> resourceRoles;

        if(jwt.getClaim("resource_access") == null)
            return Set.of();

        resourceAccess = jwt.getClaim("resource_access");

        if(resourceAccess.get(resourceId) == null)
            return Set.of();

        resource = (Map<String, Object>) resourceAccess.get(resourceId);

        resourceRoles = (Collection<String>) resource.get("roles");

        return resourceRoles
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toSet());
    }

}
