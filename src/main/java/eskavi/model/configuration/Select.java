package eskavi.model.configuration;

import java.util.Map;
import java.util.Objects;

/**
 * Class extends {@link SingleValueField} and represents a Configuration, in which the value has to be contained in a predefined list
 */
public class Select extends SingleValueField {

    private Map<String, String> content;

    /**
     * Constructs a new Select object
     *
     * @param name          the name of this Select
     * @param allowMultiple boolean, whether this Select can be added multiple times
     * @param expression    the {@link KeyExpression} of this Select
     * @param content
     */
    public Select(String name, boolean allowMultiple, KeyExpression expression, Map<String, String> content) {
        super(name, allowMultiple, expression);
        this.content = content;
    }

    protected Select() {
    }

    /**
     * Returns the Content stored in a Map. The key represents the value to show the User, while the value is inserted
     * into the {@link KeyExpression} when it is resolved
     *
     * @return a map of the possible Content
     */
    public Map<String, String> getContent() {
        return content;
    }

    /**
     * Sets the content to the given map. Should only be invoked inside constructors
     *
     * @param content the new content map
     */
    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    @Override
    public void setValue(String value) throws IllegalArgumentException {
        if (content.containsKey(value)) {
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
        result.setValue(getValue());
        return result;
    }
}
