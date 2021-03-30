package eskavi.model.configuration;

import eskavi.model.implementation.ImmutableGenericImp;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.ModuleInstance;
import eskavi.model.implementation.moduleimp.AssetConnection;
import eskavi.model.implementation.moduleimp.Endpoint;
import eskavi.model.implementation.moduleimp.Environment;
import eskavi.model.implementation.moduleimp.InteractionStarter;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.service.aasconfigurationservice.AASConstructionSession;
import org.apache.commons.codec.digest.Md5Crypt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

public class InstanceSelectTest {
    private ModuleInstance instance1;
    private ModuleInstance instance2;
    private ModuleInstance instance3;
    private InstanceSelect select1;
    private InstanceSelect select2;
    private InstanceSelect select3;
    private ConfigurationAggregate root;
    private AASConstructionSession session;
    private AssetConnection assetConnection;
    private InteractionStarter interactionStarter;
    private User userA;

    @BeforeEach
    void setUp() {
        ImmutableGenericImp dummyGeneric = new GenericStub("stub");
        Set<ImmutableGenericImp> dummys = new HashSet<>();
        dummys.add(dummyGeneric);
        userA = new User("test@web.de", "jassdja", UserLevel.ADMINISTRATOR, SecurityQuestion.MAIDEN_NAME, "julia");

        session = new AASConstructionSession(0, userA);

        select1 = new InstanceSelect("select", false, new KeyExpression(".instance1(",")"), new HashSet<>(), ImpType.ENVIRONMENT);
        select2 = new InstanceSelect("select", false, new KeyExpression(".instance2(",")"), new HashSet<>(), ImpType.INTERACTION_STARTER);
        select3 = new InstanceSelect("select", false, new KeyExpression(".instance3(",")"), new HashSet<>(), ImpType.ASSET_CONNECTION);

        root = new ConfigurationAggregate("root", false, new KeyExpression("", ""), new ArrayList<>(), false);

        assetConnection = new AssetConnection(0, userA, "assetConnection", ImplementationScope.PRIVATE, select1);
        Environment environment = new Environment(1, userA, "environment", ImplementationScope.PRIVATE, select2);
        interactionStarter = new InteractionStarter(2, userA, "interactionStarter", ImplementationScope.PRIVATE, root);

        this.instance1 = new ModuleInstance(assetConnection);
        this.instance2 = new ModuleInstance(environment);
        this.instance3 = new ModuleInstance(interactionStarter);

        root = new ConfigurationAggregate("root", false, new KeyExpression("", ""), new ArrayList<>(), false);
        select1.setImplementation(environment);
        root.setChildren(Arrays.asList(select1));
        instance1.setInstanceConfiguration(root);

        root = new ConfigurationAggregate("root", false, new KeyExpression("", ""), new ArrayList<>(), false);

        select2.setImplementation(interactionStarter);
        root.setChildren(Arrays.asList(select2));
        instance2.setInstanceConfiguration(root);

        root = new ConfigurationAggregate("root", false, new KeyExpression("", ""), new ArrayList<>(), false);

        instance3.setInstanceConfiguration(root);

        session.addModuleInstance(instance1);
        session.addModuleInstance(instance2);
        session.addModuleInstance(instance3);
    }

    @Test
    void testCircularWithout() {
        assertEquals(false, session.hasCircularRequirements(instance1, instance1.getInstanceConfiguration()));
    }

    @Test
    void testWithCircular() {
        root = new ConfigurationAggregate("root", false, new KeyExpression("", ""), new ArrayList<>(), false);
        interactionStarter = new InteractionStarter(2, userA, "interactionStarter", ImplementationScope.PRIVATE, select3);
        instance3 = new ModuleInstance(interactionStarter);

        select3.setImplementation(assetConnection);
        root.setChildren(Arrays.asList(select3));
        instance3.setInstanceConfiguration(root);

        session.addModuleInstance(instance3);

        assertEquals(true, session.hasCircularRequirements(instance1, instance1.getInstanceConfiguration()));
    }

    @Test
    void testUpdate() {
        root = new ConfigurationAggregate("root", false, new KeyExpression("", ""), new ArrayList<>(), false);
        interactionStarter = new InteractionStarter(2, userA, "interactionStarter", ImplementationScope.PRIVATE, select3);
        instance3 = new ModuleInstance(interactionStarter);

        select3.setImplementation(assetConnection);
        root.setChildren(Arrays.asList(select3));

        try {
            session.updateInstanceConfiguration(2, root);
            assertTrue(false);
        } catch (IllegalAccessException | IllegalStateException e) {
            System.out.println(e.getMessage());
            assertTrue(true);
        }
    }
}
