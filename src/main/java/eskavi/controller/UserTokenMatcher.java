package eskavi.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import eskavi.model.user.ImmutableUser;
import eskavi.repository.UserRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Component
public class UserTokenMatcher {

    final UserRepository userRepository;

    public UserTokenMatcher(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO return userId instead of ImmutableUser
    public ImmutableUser getUser(String token) {
        token = token.replaceFirst("^Bearer ", "");
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm)
                .build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);
        String userId = jwt.getClaim("email").asString();
        return userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    public String generateToken(String userId) {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        return JWT.create()
                .withClaim("email", userId)
                .withExpiresAt(DateUtils.addHours(new Date(), 1))
                .sign(algorithm);
    }
}
