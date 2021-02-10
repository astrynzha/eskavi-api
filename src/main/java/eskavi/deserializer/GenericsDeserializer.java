package eskavi.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.MessageType;
import eskavi.model.implementation.ProtocolType;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.service.ImpService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashSet;

public class GenericsDeserializer extends StdDeserializer<HashSet<ImmutableGenericImp>> {
    @Autowired
    private ImpService service;

    public GenericsDeserializer() {
        this(null);
    }

    protected GenericsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public HashSet<ImmutableGenericImp> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        HashSet<ImmutableGenericImp> result = new HashSet<>();
        //TODO delete when ready
        User userA = new User("a@gmail.com", "dfjask;fj",
                UserLevel.PUBLISHING_USER, SecurityQuestion.MAIDEN_NAME, "Julia");
        result.add(new ProtocolType(0, userA, "protocolType_0", ImplementationScope.SHARED));
        result.add(new MessageType(3, userA, "messageType_3", ImplementationScope.SHARED));
        /*
        if (node.isArray()) {
            ArrayNode nodes = (ArrayNode) node;
            for (JsonNode element : nodes) {
                result.add((ImmutableGenericImp) service.getImp(element.asLong()));
            }
        }
        TODO get tests for services to work to test this section properly
         */
        return result;
    }
}
