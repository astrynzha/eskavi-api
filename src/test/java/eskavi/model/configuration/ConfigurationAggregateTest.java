package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.ModuleInstance;
import eskavi.util.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ConfigurationAggregateTest {
    private ConfigurationAggregate testObject;
    private List<Configuration> children;
    private HashSet<ImmutableGenericImp> generics;
    private ImplementationSelect impSelect;

    @BeforeEach
    void setUp() {
        setUpChildren();
        testObject = new ConfigurationAggregate("aggregate", true, new KeyExpression("<aggregate>", "<aggregate>"),
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
    void equalsTest0() {
        ConfigurationAggregate ca0 = new ConfigurationAggregate("name", true,
                new KeyExpression("String name = ", ";"),
                new LinkedList<>(), true);
        ConfigurationAggregate ca1 = new ConfigurationAggregate("name", true,
                new KeyExpression("String name = ", ";"),
                new LinkedList<>(), true);
        ca0.addChild(testObject);
        ca1.addChild(testObject);
        assertEquals(ca0, ca1);
    }

    @Test
    void equalsTest1() {
        // TODO test if child is also an aggregate
        Configuration testObject1 = testObject.clone();
        TextField textFieldMultiple = new TextField("multiple", true,
                new KeyExpression("<multiple>", "<multiple>"), DataType.TEXT);
        try {
            testObject1.addChild(textFieldMultiple);
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
        assertEquals(4, testObject1.getChildren().size());
        // (TextField, ImpSelect, TextFieldMultiple) equals (TextField, ImpSelect, TextFieldMultiple, TextFieldMultiple)
        assertEquals(testObject, testObject1);
        try {
            testObject1.removeChild("multiple");
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
        // (TextField, ImpSelect, TextFieldMultiple) !equals (TextField, ImpSelect)
        assertFalse(testObject1.equals(testObject));
        assertFalse(testObject.equals(testObject1));
    }

    @Test
    void equalsTest2() {
        // with aggregate
        Configuration testObject1 = testObject.clone();
        Configuration childAggregate = testObject.clone();
        try {
            testObject1.addChild(childAggregate);
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
        // (TextField, ImpSelect, TextFieldMultiple) !equals (TextField, ImpSelect, TextFieldMultiple, childAggregate))
        assertFalse(testObject1.equals(testObject));
        testObject.addChild(childAggregate);
        testObject.addChild(childAggregate);
        // (TextField, ImpSelect, TextFieldMultiple, childAggregate, childAggregate) !equals
        // (TextField, ImpSelect, TextFieldMultiple, childAggregate))
        assertTrue(testObject.equals(testObject1));
    }

    @Test
    void setChildrenTest() {
        List<Configuration> children = new LinkedList<>();
        TextField textFieldMultiple = new TextField("multiple", true,
                new KeyExpression("<multiple>", "<multiple>"), DataType.TEXT);
        TextField textField = new TextField("single", false,
                new KeyExpression("<single>", "<single>"), DataType.TEXT);
        children.add(textFieldMultiple);
        children.add(textFieldMultiple);
        children.add(textField);
        testObject.setChildren(children); // throws no exception
        children.add(textField);
        assertThrows(IllegalArgumentException.class, () -> testObject.setChildren(children));
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