package eskavi.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import eskavi.model.user.User;
import eskavi.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class UserByIdDeserializer extends StdDeserializer<User> {
    @Autowired
    UserManagementService service;

    public UserByIdDeserializer() {
        this(null);
    }

    public UserByIdDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String email = node.asText();
        //TODO is this correct?
        if (email.isEmpty()) {
            return null;
        }
        return (User) service.getUser(email);
    }
}
