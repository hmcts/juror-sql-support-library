package uk.gov.hmcts.juror.support.sql.v2.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import uk.gov.hmcts.juror.support.sql.v1.entity.Juror;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JwtDetailsPublic implements JwtDetails {

    private final String jwtSecret;

    public JwtDetailsPublic() {
        this.jwtSecret = DataCreator.publicJwtSecret;
    }


    public JwtDetailsPublic(Juror juror) {
        this();
        this.id = "123";
        this.jurorNumber = juror.getJurorNumber();
        this.surname = juror.getLastName();
        this.postcode = juror.getPostcode();
        this.roles = new String[]{"juror"};
    }

    private String id;
    private String jurorNumber;
    private String surname;
    private String postcode;
    private String[] roles;


    @JsonIgnore
    public String getJwt() {
        Date issuedAtDate = new Date();
        return Jwts.builder()
            .claims(getClaims())
            .issuedAt(issuedAtDate)
            .expiration(new Date(issuedAtDate.getTime() + 300000L))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtSecret)))
            .compact();
    }

    private Map<String, Object> getClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("jurorNumber", jurorNumber);
        claims.put("surname", surname);
        claims.put("postcode", postcode);
        claims.put("roles", roles);
        return Map.of("data",claims);
    }


}
