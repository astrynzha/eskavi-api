package eskavi.model.configuration;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;

@Entity
public class FileField extends SingleValueField {
    @Value("${file.defaultpath}")
    private static String defaultPath;

    public FileField(String name, boolean allowMultiple, KeyExpression expression) {
        super(name, allowMultiple, expression);
    }

    protected FileField() {
    }

    @Override
    public String resolveKeyExpression() {
        if (getValue() != null) {
            return getKeyExpression().getExpressionStart() + FileField.defaultPath + getValue() + getKeyExpression().getExpressionEnd();
        } else {
            throw new IllegalStateException("A value has to be assigned to Configuration: " + getName());
        }
    }

    @Override
    public String toString() {
        return "FileField{" +
                "name='" + getName() + '\'' +
                ", allowMultiple=" + allowsMultiple() +
                ", keyExpression=" + getKeyExpression().toString() +
                ", filename='" + getValue() + "'}";
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Configuration clone() {
        return null;
    }
}
