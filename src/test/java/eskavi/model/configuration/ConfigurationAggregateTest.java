package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.ModuleInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.synth.SynthTabbedPaneUI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigurationAggregateTest {
    private ConfigurationAggregate testObject;
    private List<Configuration> children;
    private HashSet<ImmutableGenericImp> generics;
    private ImplementationSelect impSelect;

    @BeforeEach
    void setUp() {
        setUpChildren();
        testObject = new ConfigurationAggregate("aggregate", false, new KeyExpression("<aggregate>", "<aggregate>"),
                children, false);
    }

    private void setUpChildren() {
        children = new ArrayList<>();
        TextField textField = new TextField("text", false, new KeyExpression("<text>", "<text>"), DataType.TEXT);
        textField.setValue("textValue");
        children.add(textField);
        generics = new HashSet<>();
        generics.add(new GenericStub("generic"));
        impSelect = new ImplementationSelect("impSelect", false,
                new KeyExpression("<impSelect>", "<impSelect>"), generics, ImpType.ENDPOINT);
        children.add(impSelect);
        TextField instanceConfig = new TextField("instanceConfig", false, new KeyExpression("<instanceConfig>", "<instanceConfig>"), DataType.TEXT);
        instanceConfig.setValue("value");
        impSelect.setInstance(new ModuleInstance(new ImplementationStub(1), instanceConfig));
        TextField textFieldMultiple = new TextField("multiple", true, new KeyExpression("<multiple>", "<multiple>"), DataType.TEXT);
        textFieldMultiple.setValue("multipleFirstVal");
        children.add(textFieldMultiple);
    }

    @Test
    void resolveKeyExpression() {
        assertEquals("<aggregate><text>textValue<text><impSelect><instanceConfig>value<instanceConfig><impSelect><multiple>multipleFirstVal<multiple><aggregate>",
                testObject.resolveKeyExpression());
    }

    @Test
    void checkCompatible() {
        assertEquals(true, testObject.checkCompatible());
    }

    @Test
    void checkCompatibleWithDifferentCaseButNoEnforce() {
        ImplementationSelect impSelect = new ImplementationSelect("diffSelect", false,
                new KeyExpression("<diffSelect>", "<diffSelect>"), generics, ImpType.ENDPOINT);
        testObject.addChild(impSelect);
        TextField instanceConfig = new TextField("instanceConfig", false, new KeyExpression("<instanceConfig>", "<instanceConfig>"), DataType.TEXT);
        instanceConfig.setValue("value");
        impSelect.setInstance(new ModuleInstance(new ImplementationStub(2), instanceConfig));
        assertEquals(true, testObject.checkCompatible());
    }

    @Test
    void checkCompatibleWithDifferentCaseAndEnforce() {
        testObject = new ConfigurationAggregate("aggregate", false, new KeyExpression("<aggregate>", "<aggregate>"),
                children, true);
        ImplementationSelect impSelect = new ImplementationSelect("diffSelect", false,
                new KeyExpression("<diffSelect>", "<diffSelect>"), generics, ImpType.ENDPOINT);
        testObject.addChild(impSelect);
        TextField instanceConfig = new TextField("instanceConfig", false, new KeyExpression("<instanceConfig>", "<instanceConfig>"), DataType.TEXT);
        instanceConfig.setValue("value");
        impSelect.setInstance(new ModuleInstance(new ImplementationStub(2), instanceConfig));
        assertEquals(false, testObject.checkCompatible());
    }

    @Test
    void addChildWhichIsAlreadyPart() {
        TextField textField = new TextField("text", false, new KeyExpression("<text>", "<text>"), DataType.TEXT);
        textField.setValue("textValue");
        try {
            testObject.addChild(textField);
            assertEquals(true, false);
        } catch (IllegalArgumentException e) {
            assertEquals(true, true);
        }
    }

    @Test
    void addChildWhichIsAlreadyPartAllowsMultiple() {
        TextField textFieldMultiple = new TextField("multiple", true,
                new KeyExpression("<multiple>", "<multiple>"), DataType.TEXT);
        textFieldMultiple.setValue("multipleSecondVal");
        testObject.addChild(textFieldMultiple);
        children.add(textFieldMultiple);
        assertEquals(children, testObject.getChildren());
    }

    @Test
    void getDependentModuleImps() {
        HashSet<ImmutableModuleImp> expected = new HashSet<>();
        ImplementationSelect impSelect = new ImplementationSelect("diffSelect", false,
                new KeyExpression("<diffSelect>", "<diffSelect>"), generics, ImpType.ENDPOINT);
        testObject.addChild(impSelect);
        TextField instanceConfig = new TextField("instanceConfig", false, new KeyExpression("<instanceConfig>", "<instanceConfig>"), DataType.TEXT);
        instanceConfig.setValue("value");
        ImmutableModuleImp imp = new ImplementationStub(2);
        impSelect.setInstance(new ModuleInstance(imp, instanceConfig));
        expected.add(this.impSelect.getModuleImp());
        expected.add(impSelect.getModuleImp());
        System.out.println(testObject.resolveKeyExpression());
        assertEquals(expected, testObject.getDependentModuleImps());
    }
}