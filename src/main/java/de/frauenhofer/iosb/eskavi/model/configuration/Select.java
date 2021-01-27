package de.frauenhofer.iosb.eskavi.model.configuration;

import java.util.Map;

public class Select extends SingleValueField {

    private Map<String, String> content;

    public Select(String name, boolean allowMultiple, KeyExpression expression, Map<String, String> content) {
        super(name, allowMultiple, expression);
        this.content = content;
    }

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    @Override
    public void setValue(String value) throws IllegalArgumentException{
        if (content.containsKey(value)) {
            super.setValue(content.get(value));
        } else {
            throw new IllegalArgumentException("value must be a possible value from content");
        }
    }

    @Override
    public Select clone() {
        KeyExpression copy = new KeyExpression(getKeyExpression().getExpressionStart(), getKeyExpression().getExpressionEnd());
        return new Select(getName(), allowsMultiple(), copy, getContent());
    }
}
