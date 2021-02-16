package eskavi.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import eskavi.model.user.ImmutableUser;
import eskavi.repository.UserRepository;
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

    public ImmutableUser getUser(String token) {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm)
                .build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);
        String userId = jwt.getHeaderClaim("email").asString();
        return userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    public String generateToken(String userId) {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        String token = JWT.create()
                .withClaim("email", userId)
                .withExpiresAt(new Date(new Date().getTime() + 2 * 3600 * 1000))
                .sign(algorithm);
        return token;
    }
}
