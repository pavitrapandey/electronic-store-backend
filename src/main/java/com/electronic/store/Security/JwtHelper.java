package com.electronic.store.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {

    //requirement: 1.Valitate the token
    public final long JWT_TOKEN_VALIDITY = 5 * 60 * 60*1000; //5 hours

    //2. Secret key
    public static final String SECRET_kEY = "a1b2c3d4e5f6g7h8i9j0A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0a1b2c3d4e5f6g7h8i9j0A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0";


    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return this.getClaimFromToken(token,Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        //Uses in 0's version
        return Jwts.parser().setSigningKey(SECRET_kEY).build().parseClaimsJws(token).getPayload();
        //Use in 1.0 version
//         SignatureAlgorithm hs512=SignatureAlgorithm.HS512;
//        SecretKeySpec secretKeySpec=new SecretKeySpec(SECRET_kEY.getBytes(),hs512.getJcaName());
//        return Jwts.parser().verifyWith(secretKeySpec).build().parseClaimsJws(token).getPayload();
    }

    //check if the token has expired
   public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

   public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token,Claims::getExpiration);
    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String,Object> claims=new HashMap<>();
        return doGenerateToken(claims,userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().
                setClaims(claims).
                setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512,SECRET_kEY).compact();

    }


}
