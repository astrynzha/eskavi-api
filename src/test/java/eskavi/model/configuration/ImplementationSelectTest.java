package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImmutableModuleImp;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.ModuleInstance;
import eskavi.model.user.ImmutableUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImplementationSelectTest {
    private ImplementationSelect testObject;
    private ModuleInstance instance;
    private ImmutableUser testUser;
    private ImmutableModuleImp implementation;
    private HashSet<ImmutableGenericImp> generics;
    private SingleValueField instanceConfig;

    @BeforeEach
    void setUpHelpObjects() {
        testUser = new UserStub();
        generics = new HashSet<>();
        generics.add(new GenericStub("generic"));
        implementation = new ImplementationStub(0, new GenericStub("generic"));
        instanceConfig = new TextField("text", false, new KeyExpression("text:", "."), DataType.TEXT);
        instanceConfig.setValue("value");
        instance = new ModuleInstance(implementation, instanceConfig);
        testObject = new ImplementationSelect("impSelect", false, new KeyExpression("impSelect:", "."), generics, ImpType.ENDPOINT);
    }

    @Test
    void getGeneric() {
        assertEquals(generics, testObject.getGeneric());
    }

    @Test
    void getType() {
        assertEquals(ImpType.ENDPOINT, testObject.getType());
    }

    @Test
    void getImplementationWithValueSet() {
        testObject.setInstance(instance);
        assertEquals(instance.getModuleImp(), testObject.getModuleImp());
    }

    @Test
    void getImplementationWithoutValueSet() {
        assertEquals(null, testObject.getModuleImp());
    }

    @Test
    void resolveKeyExpressionWithoutInstance() {
        try {
            testObject.resolveKeyExpression();
            assertEquals(true, false);
        } catch (IllegalStateException e) {
            assertEquals(true, true);
        }
    }

    @Test
    void resolveKeyExpressionWithInstance() {
        testObject.setInstance(instance);
        try {
            assertEquals("impSelect:text:value..", testObject.resolveKeyExpression());
        } catch (IllegalStateException e) {
            assertEquals(true, false);
        }
    }

    @Test
    void checkCompatibleWithoutInstanceSet() {
        assertEquals(false, testObject.checkCompatible());
    }

    @Test
    void checkCompatibleWithInstanceSet() {
        testObject.setInstance(instance);
        assertEquals(true, testObject.checkCompatible());
    }

    @Test
    void getChildrenWithoutInstanceSet() {
        assertEquals(0, testObject.getChildren().size());
    }

    @Test
    void getChildrenWithInstanceSet() {
        testObject.setInstance(instance);
        List<Configuration> expected = new ArrayList<>();
        expected.add(instanceConfig);
        assertEquals(expected, testObject.getChildren());
    }

    @Test
    void testGetDependentModuleImpsWithoutChild() {
        testObject.setInstance(instance);
        HashSet<ImmutableModuleImp> expected = new HashSet<>();
        expected.add(this.implementation);
        assertEquals(expected, testObject.getDependentModuleImps());
    }

    @Test
    void testGetDependentModuleImpsWithChild() {
        ImplementationSelect config = new ImplementationSelect("child", false, new KeyExpression("child:", "."),
                generics, ImpType.ENDPOINT);
        ImmutableModuleImp newImp = new ImplementationStub(20, new GenericStub("generic"));
        config.setInstance(new ModuleInstance(newImp, instanceConfig));
        testObject.setInstance(new ModuleInstance(implementation, config));
        HashSet<ImmutableModuleImp> expected = new HashSet<>();
        expected.add(this.implementation);
        expected.add(newImp);
        assertEquals(expected, testObject.getDependentModuleImps());
    }
}