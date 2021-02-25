package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.ModuleInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        impSelect.setInstance(new ModuleInstance(new ConfigurationImplementationStub(1, new GenericStub("generic"), instanceConfig)));
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
        impSelect.setInstance(new ModuleInstance(new ConfigurationImplementationStub(2, new GenericStub("generic"), instanceConfig)));
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
        impSelect.setInstance(new ModuleInstance(new ConfigurationImplementationStub(2, new GenericStub("generic"), instanceConfig)));
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
    void removeChild() {
        TextField textFieldMultiple = new TextField("unique", true,
                new KeyExpression("<multiple>", "<multiple>"), DataType.TEXT);
        textFieldMultiple.setValue("multipleSecondVal");
        testObject.addChild(textFieldMultiple);
        assertTrue(testObject.getChildren().contains(textFieldMultiple));
        testObject.removeChild("unique");
        assertFalse(testObject.getChildren().contains(textFieldMultiple));
    }

    @Test
    void getDependentModuleImps() {
        HashSet<ImmutableModuleImp> expected = new HashSet<>();
        ImplementationSelect impSelect = new ImplementationSelect("diffSelect", false,
                new KeyExpression("<diffSelect>", "<diffSelect>"), generics, ImpType.ENDPOINT);
        testObject.addChild(impSelect);
        TextField instanceConfig = new TextField("instanceConfig", false, new KeyExpression("<instanceConfig>", "<instanceConfig>"), DataType.TEXT);
        instanceConfig.setValue("value");
        ImmutableModuleImp imp = new ConfigurationImplementationStub(2, new GenericStub("generic"), instanceConfig);
        impSelect.setInstance(new ModuleInstance(imp));
        expected.add(this.impSelect.getModuleImp());
        expected.add(impSelect.getModuleImp());
        System.out.println(testObject.resolveKeyExpression());
        assertEquals(expected, testObject.getDependentModuleImps());
    }

    @Test
    void toStringTest() {
        ConfigurationAggregate ca = new ConfigurationAggregate("name", true,
                new KeyExpression("String name = ", ";"),
                new LinkedList<>(), true);
        System.out.println(ca.toString());
        assertEquals("ConfigurationAggregate{name='name', allowMultiple=true, keyExpression=KeyExpression{expr" +
                        "essionStart='String name = ', expressionEnd=';'}, enforceCompatibility=true, children=[]}",
                ca.toString());
    }

    @Test
    void equalsTest() {
        ConfigurationAggregate ca0 = new ConfigurationAggregate("name", true,
                new KeyExpression("String name = ", ";"),
                new LinkedList<>(), true);
        ConfigurationAggregate ca1 = new ConfigurationAggregate("name", true,
                new KeyExpression("String name = ", ";"),
                new LinkedList<>(), true);
        ca0.addChild(testObject);
        ca1.addChild(testObject);
        assertTrue(ca0.equals(ca1));
    }

    @Test
    void testIsValid() {
        assertEquals(true, testObject.isValid());
    }

    @Test
    void testValidDiffGenericNoEnforce() {
        HashSet<ImmutableGenericImp> newGeneric = new HashSet<>();
        newGeneric.add(new GenericStub("other"));
        ImplementationSelect impSelect = new ImplementationSelect("diffSelect", false,
                new KeyExpression("<diffSelect>", "<diffSelect>"), generics, ImpType.ENDPOINT);
        testObject.addChild(impSelect);

        assertEquals(true, testObject.isValid());
    }

    @Test
    void testIsValidDiffGenericEnforce() {
        testObject.setEnforceCompatibility(true);
        HashSet<ImmutableGenericImp> newGeneric = new HashSet<>();
        newGeneric.add(new GenericStub("other"));
        ImplementationSelect impSelect = new ImplementationSelect("diffSelect", false,
                new KeyExpression("<diffSelect>", "<diffSelect>"), newGeneric, ImpType.ENDPOINT);
        testObject.addChild(impSelect);

        assertEquals(false, testObject.isValid());
    }
}