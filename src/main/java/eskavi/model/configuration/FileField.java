package eskavi.model.configuration;

import eskavi.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.FieldResult;

@Entity
public class FileField extends SingleValueField {
    private static String defaultpath = "./";

    public FileField(String name, boolean allowMultiple, KeyExpression expression) {
        super(name, allowMultiple, expression);
    }

    protected FileField() {
    }

    @Override
    public String resolveKeyExpression() {
        if (getValue() != null) {
            return getKeyExpression().getExpressionStart() + FileField.defaultpath + getValue() + getKeyExpression().getExpressionEnd();
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
    public FileField clone() {
        FileField result = new FileField(this.getName(), this.allowsMultiple(), this.getKeyExpression());
        result.setValue(this.getValue());
        return result;
    }
}
