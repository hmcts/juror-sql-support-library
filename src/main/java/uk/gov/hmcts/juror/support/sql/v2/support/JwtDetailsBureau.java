package uk.gov.hmcts.juror.support.sql.v2.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class JwtDetailsBureau implements JwtDetails {

    private final String jwtSecret;

    public JwtDetailsBureau() {
        this.jwtSecret = DataCreator.jwtSecret;
    }


    public JwtDetailsBureau(DataCreator.User user) {
        this();
        this.updateOwnerAndLoc(user);
        this.login = user.getUsername();
        this.roles = new HashSet<>(user.getRoles());
        this.userType = user.getUserType();
    }

    private String owner = "415";
    private String locCode = "415";
    private String login = "ben";
    private UserType userType = UserType.COURT;
    private Collection<Role> roles = new HashSet<>(Set.of(Role.values()));

    private String userLevel = "1";
    private Map<String, Object> staff = new HashMap<>(Map.of(
        "name", login,
        "rank", 9,
        "active", 1,
        "courts", new String[]{"415"}
    ));


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
        claims.put("owner", owner);
        claims.put("locCode", locCode);
        claims.put("login", login);
        claims.put("userType", userType);
        claims.put("roles", roles);
        claims.put("userLevel", userLevel);
        claims.put("staff", staff);
        return claims;
    }

    public JwtDetailsBureau updateOwnerAndLoc(String locCode) {
        this.owner = locCode;
        this.locCode = locCode;
        this.staff.put("courts", new String[]{locCode});
        return this;
    }

    public JwtDetailsBureau updateOwnerAndLoc(DataCreator.User user) {
        this.owner = user.getOwner();
        this.locCode = user.getOwner();
        this.staff.put("courts", user.getCourts());
        return this;
    }

    public JwtDetailsBureau addRole(Role role) {
        this.roles.add(role);
        return this;
    }
}
