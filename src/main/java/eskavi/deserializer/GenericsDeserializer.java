package eskavi.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
        if (node.isArray()) {
            ArrayNode nodes = (ArrayNode) node;
            for (JsonNode element : nodes) {
                result.add((ImmutableGenericImp) service.getImp(element.asLong()));
            }
        }
        return result;
    }
}
