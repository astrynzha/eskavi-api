package eskavi.model.configuration;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class extends {@link SingleValueField} and represents a Configuration, in which the value has to be contained in a predefined list
 */
@Entity
public class Select extends SingleValueField {
    @ElementCollection(targetClass = String.class)
    private Map<String, String> content;
    private String valueKey;

    /**
     * Constructs a new Select object
     *
     * @param name          the name of this Select
     * @param allowMultiple boolean, whether this Select can be added multiple times
     * @param expression    the {@link KeyExpression} of this Select
     * @param content
     */
    @JsonCreator
    public Select(@JsonProperty("name") String name, @JsonProperty("allowMultiple") boolean allowMultiple,
                  @JsonProperty("keyExpression") KeyExpression expression, @JsonProperty("content") Map<String, String> content) {
        super(name, allowMultiple, expression);
        this.content = content;
    }

    protected Select() {
    }

    /**
     * Returns the Content stored in a Map. The key represents the value to show the User, while the value is inserted
     * into the {@link KeyExpression} when it is resolved
     *
     * @return a list of the possible Content
     */
    public Map<String, String> getContent() {
        return content;
    }

    /**
     * Sets the content to the given map. Should only be invoked inside constructors
     *
     * @param content the new content list
     */
    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    @JsonGetter
    public String getValueKey() {
        return valueKey;
    }

    @JsonSetter
    protected void setValueKey(String valueKey) {
        this.setValue(valueKey);
    }


    @Override
    @JsonIgnore
    public void setValue(String value) throws IllegalArgumentException {
        if (content != null && content.containsKey(value)) {
            this.valueKey = value;
            super.setValue(content.get(value));
        } else {
            throw new IllegalArgumentException("value must be a possible value from content");
        }
    }

    @Override
    public String toString() {
        return "Select{" +
                "name='" + getName() + "'" +
                ", allowMultiple=" + allowsMultiple() +
                ", keyExpression=" + getKeyExpression().toString() +
                ", value='" + getValue() + "'" +
                ", content=" + getContent() +
                "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getContent());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Select select = (Select) o;
        return Objects.equals(getContent(), select.getContent());
    }

    @Override
    public Select clone() {
        KeyExpression copy = new KeyExpression(getKeyExpression().getExpressionStart(), getKeyExpression().getExpressionEnd());
        Select result = new Select(getName(), allowsMultiple(), copy, getContent());
        if (getValue() != null) {
            result.setValue(getValue());
        }
        return result;
    }
}
