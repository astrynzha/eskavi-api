package eskavi.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.service.ImpService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ImplementationByIdDeserializer extends StdDeserializer<ImmutableModuleImp> {
    @Autowired
    private ImpService impService;

    protected ImplementationByIdDeserializer() {
        this(null);
    }

    protected ImplementationByIdDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ImmutableModuleImp deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return (ImmutableModuleImp) impService.getImp(node.asLong());
    }
}
