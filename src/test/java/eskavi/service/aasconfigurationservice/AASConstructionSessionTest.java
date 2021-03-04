package eskavi.service.aasconfigurationservice;

import eskavi.model.configuration.DataType;
import eskavi.model.configuration.KeyExpression;
import eskavi.model.configuration.TextField;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.implementation.MessageType;
import eskavi.model.implementation.ProtocolType;
import eskavi.model.implementation.moduleimp.*;
import eskavi.model.user.SecurityQuestion;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.util.ImpCreatorUtil;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AASConstructionSessionTest {
    private ImpCreatorUtil imps;
    private AASConstructionSession testObject;

    @BeforeEach
    void setUp() {
        this.imps = new ImpCreatorUtil();
        this.testObject = new AASConstructionSession(0, imps.getUserA());
        testObject.addModuleInstance(ModuleInstanceFactory.build(imps.getAssetConnection()));
        testObject.addModuleInstance(ModuleInstanceFactory.build(imps.getInteractionStarter()));
        testObject.addModuleInstance(ModuleInstanceFactory.build(imps.getPersistenceManager()));
        testObject.addModuleInstance(ModuleInstanceFactory.build(imps.getEndpoint()));
    }

    @Test
    void testGenerateJavaClassValid() throws FileNotFoundException {
        String data = "";
        Scanner myReader = new Scanner(testObject.generateJavaClass());
        while (myReader.hasNextLine()) {
            data = data + "\n" + myReader.nextLine();
        }
        // To remove first newline
        data = data.substring(1);
        assertEquals("class App {\n" +
                "  public static void main(String[] args) throws IOException {\n" +
                "    Assetconnection assetconnection = Builder().dummy(\"dummy\").build();\n" +
                "    InteractionStarter interactionstarter = Builder().dummy(\"dummy\").build();\n" +
                "    PersistanceManager persistancemanager = Builder().dummy(\"dummy\").build();\n" +
                "    Endpoint endpoint =\n" +
                "        Builder()\n" +
                "            .mapping(\n" +
                "                Serializer.builder().dummy(\"dummy\").build(),\n" +
                "                Deserializer.builder().dummy(\"dummy\").build(),\n" +
                "                Dispatcher.builder().handler(Handler.builder().dummy(\"dummy\").build()).build())\n" +
                "            .port(8080)\n" +
                "            .build();\n" +
                "    AasService service =\n" +
                "        AasService.builder()\n" +
                "            .assetconnection(assetconnection)\n" +
                "            .interactionstarter(interactionstarter)\n" +
                "            .persistencemanager(persistancemanager)\n" +
                "            .endpoint(endpoint)\n" +
                "            .build();\n" +
                "    AasServiceManager.Instance.setAasService(service);\n" +
                "    service.start();\n" +
                "  }\n" +
                "}", data);
    }

    @Test
    void testGenerateJavaClassFailure() {

        Endpoint endpoint = imps.getEndpoint();
        endpoint.setProtocolType(new ProtocolType());
        testObject.addModuleInstance(ModuleInstanceFactory.build(endpoint));
        try {
            testObject.generateJavaClass();
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }


}
