package eskavi.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import eskavi.model.implementation.Scope;

import java.io.IOException;

public class ScopeDeserializer extends StdDeserializer<Scope> {
    public ScopeDeserializer() {
        this(null);
    }

    public ScopeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Scope deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        long id = node.findValue("id").asLong();
        return null;//TODO: database access
    }
}
