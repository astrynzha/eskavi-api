package eskavi.model.configuration;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eskavi.model.implementation.ImpType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * This enumeration lists all the available types of configuration fields. It's used to provide the callers of the api
 * with templates of the expected json format.
 * @version 1.0.0
 * @author David Kaufmann
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ConfigurationType {
    CONFIGURATION_AGGREGATE(new ConfigurationAggregate("", false, new KeyExpression("",""), new ArrayList<>(), false)),
    TEXT_FIELD(new TextField("", false, new KeyExpression("",""), DataType.TEXT)),
    IMPLEMENTATION_SELECT(new ImplementationSelect("", false, new KeyExpression("", ""), new HashSet<>(), ImpType.SERIALIZER)),
    SELECT(new Select("", false, new KeyExpression("", ""), new HashMap<>())),
    FILE_FIELD(new FileField("", false, new KeyExpression("",""))),
    SWITCH(new Switch("", false, new KeyExpression("",""), "", ""));

    @JsonProperty
    private Configuration template;

    private ConfigurationType(Configuration template) {
        this.template = template;
    }
}
