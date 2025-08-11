package com.services.ServiceProvider.security;

import com.services.ServiceProvider.constant.Constants;
import com.services.ServiceProvider.entity.Role;
import com.services.ServiceProvider.entity.User;
import com.services.ServiceProvider.exception.TokenException;
import com.services.ServiceProvider.exception.TokenNotCreatedException;
import com.services.ServiceProvider.exception.UserNotFoundException;
import com.services.ServiceProvider.repository.UserRepository;
import com.services.ServiceProvider.service.OtpService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtHelper {

    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);


    public String getUserNameFromToken(String token){
        return getClaimFRomToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFRomToken(String token){
        return getClaimFRomToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFRomToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException ex) {
            logger.error("JWT token is expired: {}", ex.getMessage());
            throw new TokenException(440,Constants.RESPONSE.get(440));

        } catch (UnsupportedJwtException ex) {
            logger.error("JWT token is unsupported: {}", ex.getMessage());
            throw new TokenException(441,Constants.RESPONSE.get(441));

        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
            throw new TokenException(442,Constants.RESPONSE.get(442));

        } catch (SignatureException ex) {
            logger.error("JWT signature does not match: {}", ex.getMessage());
            throw new TokenException(443,Constants.RESPONSE.get(443));

        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty: {}", ex.getMessage());
            throw new TokenException(444,Constants.RESPONSE.get(444));

        } catch (Exception ex) {
            logger.error("Unexpected error while parsing token: {}", ex.getMessage(), ex);
            throw new TokenException(445,Constants.RESPONSE.get(445));
        }
    }

    public Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFRomToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims =new HashMap<>();
        return doGenerateToken(claims, userDetails);
    }

    private String doGenerateToken(Map<String, Object> claims, UserDetails userDetails) {
        try {
            if (!(userDetails instanceof CustomUserDetails)) {
                throw new IllegalArgumentException("UserDetails must be instance of CustomUserDetails");
            }

            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            Set<Role> roles = customUserDetails.getUser().getRoles();

            if (roles == null || roles.isEmpty()) {
                logger.warn("User {} has no roles assigned", userDetails.getUsername());
            }

            claims.put("roles", roles.stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.toList()));

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_TOKEN_VALIDITY * 1000))
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();

        } catch (TokenNotCreatedException e) {
            logger.error("Failed to generate JWT token for user {}: {}", userDetails.getUsername(), e.getMessage(), e);
            throw new TokenNotCreatedException(433, Constants.RESPONSE.get(433));
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String userName = getUserNameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean doAuthenticateOtp(String email, Long otp){
        boolean isValid = otpService.validateOtp(email,otp);
        return isValid;
    }


    public boolean doAuthenticatePassword(String username, String rawPassword) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException(410,username,Constants.RESPONSE.get(410)));

        String encryptedPassword = user.getPassword();
        return encoder.matches(rawPassword, encryptedPassword);
    }

}