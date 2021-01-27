package de.frauenhofer.iosb.eskavi.model.configuration;

import java.util.HashMap;
import java.util.Map;

public class Switch extends Select {
    private static String TRUE = "trueValue";
    private static String FALSE = "falseValue";

    public Switch(String name, boolean allowMultiple, KeyExpression expression, String trueValue, String falseValue) {
        super(name, allowMultiple, expression, null);
        Map<String, String> content = new HashMap<>();
        content.put(TRUE, trueValue);
        content.put(FALSE, falseValue);
        this.setContent(content);
        //setting the default value
        this.setValue(FALSE);
    }

    @Override
    public Switch clone() {
        KeyExpression copy = new KeyExpression(getKeyExpression().getExpressionStart(), getKeyExpression().getExpressionEnd());
        return new Switch(getName(), allowsMultiple(), copy, getContent().get(TRUE), getContent().get(FALSE));
    }
}
