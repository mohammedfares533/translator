package com.cerebra.translator.security;

import com.cerebra.translator.dto.CustomUserDetails;
import com.cerebra.translator.exception.ApiException;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
@Log4j2
public class JwtTokenProvider {

    private static String SECRET_KEY;

    private final SecurityConfigProps props;

    public JwtTokenProvider(@Autowired SecurityConfigProps props) {
        this.props = props;
    }


    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(props.getSecretKey().getBytes());
    }

    public String createToken(String username, String userId, String roles) {
        LocalDateTime validity = LocalDateTime.now().plusMinutes(60);

        Date date = Date.from(validity.atZone(ZoneId.systemDefault()).toInstant());

        return createToken(username, userId, roles, date);
    }

    private String createToken(String username, String userId, String role, Date validity) {

        Claims payloadClaims = Jwts.claims().setSubject(username);
        payloadClaims.put(props.getRulesParameterName(), role);
        payloadClaims.put(props.getUserId(), userId);

        return Jwts.builder()
                .setClaims(payloadClaims)
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }


    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {

        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException | SignatureException e) {
            log.error(e);
            throw new ApiException(HttpStatus.UNAUTHORIZED, "TOKEN.EXPIRED.MESSAGE");

        }
        return claims;
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public UserDetails getUserDetails(String token) {
        String userName = getUsername(token);
        List<String> roleList = getRoleList(token);
        return new CustomUserDetails(userName, roleList.toArray(new String[0]));
    }

    public List<String> getRoleList(String token) {

        List<String> roles = null;
        try {
            roles = (List<String>) Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get(props.getRulesParameterName());
        } catch (ExpiredJwtException | SignatureException e) {
            log.error(" Token expired ");
            log.error(e);
        }

        return roles;
    }

    public String getUsername(String token) {

        String userName = null;
        try {
            userName = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();

        } catch (ExpiredJwtException | SignatureException e) {
            log.error(" Token expired");
            log.error(e);
        } catch (Exception e) {
            log.error(" Some other exception in JWT parsing userName");
        }

        return userName;
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

@Getter
@Setter
@Configuration
@ConfigurationProperties("custom.security")
class SecurityConfigProps {

    private String secretKey;

    private String authorizationParamName;

    private String rulesParameterName;

    private String userId;

    private long validityInMilliseconds;

}
