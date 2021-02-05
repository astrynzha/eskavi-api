package eskavi.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import eskavi.model.implementation.ImmutableImplementation;

import java.io.IOException;

public class ImplementationByIdDeserializer extends StdDeserializer<ImmutableImplementation> {
    public ImplementationByIdDeserializer() {
        this(null);
    }

    protected ImplementationByIdDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ImmutableImplementation deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        long id = node.findValue("id").asLong();
        return null;
    }
}
