package eskavi.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.Scope;
import eskavi.repository.ScopeRepository;
import org.hibernate.tool.hbm2ddl.ImportScriptException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ScopeDeserializer extends StdDeserializer<Scope> {

    @Autowired
    ScopeRepository repository;

    public ScopeDeserializer() {
        this(null);
    }

    public ScopeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Scope deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        long id = node.findValue("scopeId").asLong();
        String type = node.findValue("impScope").asText();
        ImplementationScope impScope = ImplementationScope.valueOf(type);

        Scope data = repository.findById(id).orElse(new Scope(impScope));
        Scope result = new Scope(impScope, data.getGrantedUsers());

        return result;
    }
}
