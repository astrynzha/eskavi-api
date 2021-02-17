package eskavi.model.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class extends {@link Select} and only allows two values. If the Switch is set the true value will be used otherwise the
 * false value. Because of that Switch always has a value.
 */
@Entity
public class Switch extends Select {
    public static int TRUE_INDEX = 0;
    public static int FALSE_INDEX = 1;

    /**
     * Constructs a new Switch
     *
     * @param name          the name of this Switch
     * @param allowMultiple boolean, whether this Switch can be added multiple times
     * @param expression    the {@link KeyExpression} of this Switch
     * @param trueValue     the value used if the Switch is toggled
     * @param falseValue    the value used if the Switch is not toggled
     */
    public Switch(String name, boolean allowMultiple, KeyExpression expression, String trueValue, String falseValue) {
        super(name, allowMultiple, expression, null);
        List<String> content = new ArrayList<>();
        content.add(trueValue);
        content.add(falseValue);
        this.setContent(content);
        //setting the default value
        this.setValue(falseValue);
    }

    @JsonCreator
    protected Switch(@JsonProperty("name") String name, @JsonProperty("allowMultiple") boolean allowMultiple,
                     @JsonProperty("keyExpression") KeyExpression expression, @JsonProperty("content") List<String> content) {
        super(name, allowMultiple, expression, content);
    }

    protected Switch() {
    }

    @Override
    public String toString() {
        return "Switch{" +
                "name='" + getName() + "'" +
                ", allowMultiple=" + allowsMultiple() +
                ", keyExpression=" + getKeyExpression().toString() +
                ", value='" + getValue() + "'" +
                ", content=" + getContent() +
                "}";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public Switch clone() {
        KeyExpression copy = new KeyExpression(getKeyExpression().getExpressionStart(), getKeyExpression().getExpressionEnd());
        Switch result = new Switch(getName(), allowsMultiple(), copy, getContent().get(TRUE_INDEX), getContent().get(FALSE_INDEX));
        // needed to be able to set value to previously set value
        result.setValue(getValue());
        return result;
    }
}
