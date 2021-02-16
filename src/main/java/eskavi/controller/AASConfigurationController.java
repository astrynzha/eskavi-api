package eskavi.controller;

import com.google.common.io.Files;
import eskavi.model.configuration.Configuration;
import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;
import eskavi.service.aasconfigurationservice.AASConfigurationService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("aas")
public class AASConfigurationController {

    final AASConfigurationService aasConfigurationService;
    final UserTokenMatcher userTokenMatcher;

    public AASConfigurationController(AASConfigurationService aasConfigurationService, UserTokenMatcher userTokenMatcher) {
        this.aasConfigurationService = aasConfigurationService;
        this.userTokenMatcher = userTokenMatcher;
    }

    /**
     * @api{post}/aas Post AAS session
     * @apiName CreateSession
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} [Authorization] Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {Number} sessionId Session unique ID
     * @apiError {String} message Errormessage
     */
    @PostMapping
    public long createSession(@RequestHeader String jwtToken) {
        ImmutableUser user = userTokenMatcher.getUser(jwtToken);
        return aasConfigurationService.createAASConstructionSession((User) user);
    }

    /**
     * @api{delete}/aas Close AAS session
     * @apiName CloseSession
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} [Authorization] Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request body) {Number} sessionId Session unique ID
     * @apiError {String} message Errormessage
     */
    @DeleteMapping()
    public void closeSession(@RequestHeader String jwtToken, @ModelAttribute("sessionId") long sessionId) {
        ImmutableUser user = userTokenMatcher.getUser(jwtToken);
        //TODO not every one should be able to randomly close sessions
        aasConfigurationService.removeAASConstructionSession(sessionId);
    }

    /**
     * @api{post}/aas/imp Add ModuleImplementation to Session
     * @apiName AddModleImp
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} [Authorization] Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request body) {Number} sessionId Session unique ID
     * @apiParam (Request body) {Number} impId Implementation unique ID
     * @apiError {String} message Errormessage
     */
    @PostMapping("{/imp")
    public void addModuleImp(@ModelAttribute("impId") long moduleId, @ModelAttribute("sessionId") long sessionId) {
        //TODO order of Parameters is annoying :D
        aasConfigurationService.addModuleInstance(sessionId, moduleId);
    }

    /**
     * @api{get}/aas/imp/configuration Get Configuration from ModuleImplementation in Session
     * @apiName GetConfiguration
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} [Authorization] Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request body) {Number} sessionId Session unique ID
     * @apiParam (Request body) {Number} impId Implementation unique ID
     * @apiSuccessExample Success-Example:
     * {
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"<parent>",
     * "expressionEnd":"<parent>"
     * },
     * "enforceCompatibility":false,
     * "name":"parent",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"<mapping>",
     * "expressionEnd":"<mapping>"
     * },
     * "enforceCompatibility":true,
     * "name":"mapping",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TextField",
     * "keyExpression":{
     * "expressionStart":"<dummy>",
     * "expressionEnd":"<dummy>"
     * },
     * "value":"dummy",
     * "dataType":"TEXT",
     * "name":"dummy",
     * "allowMultiple":false
     * },
     * {
     * "jsonTypeInfo":"ImplementationSelect",
     * "keyExpression":{
     * "expressionStart":"<serializer>",
     * "expressionEnd":"<serializer>"
     * },
     * "instance":{
     * "moduleImp":8,
     * "instanceConfiguration":{
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TextField",
     * "keyExpression":{
     * "expressionStart":"<dummy>",
     * "expressionEnd":"<dummy>"
     * },
     * "value":"dummy",
     * "dataType":"TEXT",
     * "name":"dummy",
     * "allowMultiple":false
     * }
     * ]
     * }
     * },
     * "generics":[
     * 0,
     * 3
     * ],
     * "type":"SERIALIZER",
     * "name":"serializer",
     * "allowMultiple":false
     * },
     * {
     * "jsonTypeInfo":"ImplementationSelect",
     * "keyExpression":{
     * "expressionStart":"<deserializer>",
     * "expressionEnd":"<deserializer>"
     * },
     * "instance":{
     * "moduleImp":7,
     * "instanceConfiguration":{
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TextField",
     * "keyExpression":{
     * "expressionStart":"<dummy>",
     * "expressionEnd":"<dummy>"
     * },
     * "value":"dummy",
     * "dataType":"TEXT",
     * "name":"dummy",
     * "allowMultiple":false
     * }
     * ]
     * }
     * },
     * "generics":[
     * 0,
     * 3
     * ],
     * "type":"DESERIALIZER",
     * "name":"deserializer",
     * "allowMultiple":false
     * },
     * {
     * "jsonTypeInfo":"ImplementationSelect",
     * "keyExpression":{
     * "expressionStart":"<dispatcher>",
     * "expressionEnd":"<dispatcher>"
     * },
     * "instance":{
     * "moduleImp":9,
     * "instanceConfiguration":{
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"ImplementationSelect",
     * "keyExpression":{
     * "expressionStart":"<handler>",
     * "expressionEnd":"<handler>"
     * },
     * "instance":{
     * "moduleImp":10,
     * "instanceConfiguration":{
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TextField",
     * "keyExpression":{
     * "expressionStart":"<dummy>",
     * "expressionEnd":"<dummy>"
     * },
     * "value":"dummy",
     * "dataType":"TEXT",
     * "name":"dummy",
     * "allowMultiple":false
     * }
     * ]
     * }
     * },
     * "generics":[
     * 3
     * ],
     * "type":"HANDLER",
     * "name":"handler",
     * "allowMultiple":false
     * }
     * ]
     * }
     * },
     * "generics":[
     * 3
     * ],
     * "type":"DISPATCHER",
     * "name":"dispatcher",
     * "allowMultiple":false
     * }
     * ]
     * },
     * {
     * "jsonTypeInfo":"TextField",
     * "keyExpression":{
     * "expressionStart":"<port>",
     * "expressionEnd":"<port>"
     * },
     * "value":"8080",
     * "dataType":"NUMBER",
     * "name":"port",
     * "allowMultiple":false
     * }
     * ]
     * }
     * @apiError {String} message Errormessage
     */
    @GetMapping("/imp/configuration")
    public Configuration getConfiguration(@ModelAttribute("impId") long moduleId, @ModelAttribute("sessionId") long sessionId) {
        return aasConfigurationService.getConfiguration(sessionId, moduleId);
    }

    /**
     * @api{put}/aas/imp/configuration Update Configuration of specific ModuleImplementation in Session
     * @apiName PutConfiguration
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} [Authorization] Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request body) {Number} sessionId Session unique ID
     * @apiParam (Request body) {Number} impId Implementation unique ID
     * @apiParam (Request body) {Configuration} configuration Configuration of the specified Implementation
     * @apiParamExample Request-Example:
     * {
     * "sessionId":1,
     * "impId":1,
     * {
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"<parent>",
     * "expressionEnd":"<parent>"
     * },
     * "enforceCompatibility":false,
     * "name":"parent",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"<mapping>",
     * "expressionEnd":"<mapping>"
     * },
     * "enforceCompatibility":true,
     * "name":"mapping",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TextField",
     * "keyExpression":{
     * "expressionStart":"<dummy>",
     * "expressionEnd":"<dummy>"
     * },
     * "value":"dummy",
     * "dataType":"TEXT",
     * "name":"dummy",
     * "allowMultiple":false
     * },
     * {
     * "jsonTypeInfo":"ImplementationSelect",
     * "keyExpression":{
     * "expressionStart":"<serializer>",
     * "expressionEnd":"<serializer>"
     * },
     * "instance":{
     * "moduleImp":8,
     * "instanceConfiguration":{
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TextField",
     * "keyExpression":{
     * "expressionStart":"<dummy>",
     * "expressionEnd":"<dummy>"
     * },
     * "value":"dummy",
     * "dataType":"TEXT",
     * "name":"dummy",
     * "allowMultiple":false
     * }
     * ]
     * }
     * },
     * "generics":[
     * 0,
     * 3
     * ],
     * "type":"SERIALIZER",
     * "name":"serializer",
     * "allowMultiple":false
     * },
     * {
     * "jsonTypeInfo":"ImplementationSelect",
     * "keyExpression":{
     * "expressionStart":"<deserializer>",
     * "expressionEnd":"<deserializer>"
     * },
     * "instance":{
     * "moduleImp":7,
     * "instanceConfiguration":{
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TextField",
     * "keyExpression":{
     * "expressionStart":"<dummy>",
     * "expressionEnd":"<dummy>"
     * },
     * "value":"dummy",
     * "dataType":"TEXT",
     * "name":"dummy",
     * "allowMultiple":false
     * }
     * ]
     * }
     * },
     * "generics":[
     * 0,
     * 3
     * ],
     * "type":"DESERIALIZER",
     * "name":"deserializer",
     * "allowMultiple":false
     * },
     * {
     * "jsonTypeInfo":"ImplementationSelect",
     * "keyExpression":{
     * "expressionStart":"<dispatcher>",
     * "expressionEnd":"<dispatcher>"
     * },
     * "instance":{
     * "moduleImp":9,
     * "instanceConfiguration":{
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"ImplementationSelect",
     * "keyExpression":{
     * "expressionStart":"<handler>",
     * "expressionEnd":"<handler>"
     * },
     * "instance":{
     * "moduleImp":10,
     * "instanceConfiguration":{
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TextField",
     * "keyExpression":{
     * "expressionStart":"<dummy>",
     * "expressionEnd":"<dummy>"
     * },
     * "value":"dummy",
     * "dataType":"TEXT",
     * "name":"dummy",
     * "allowMultiple":false
     * }
     * ]
     * }
     * },
     * "generics":[
     * 3
     * ],
     * "type":"HANDLER",
     * "name":"handler",
     * "allowMultiple":false
     * }
     * ]
     * }
     * },
     * "generics":[
     * 3
     * ],
     * "type":"DISPATCHER",
     * "name":"dispatcher",
     * "allowMultiple":false
     * }
     * ]
     * },
     * {
     * "jsonTypeInfo":"TextField",
     * "keyExpression":{
     * "expressionStart":"<port>",
     * "expressionEnd":"<port>"
     * },
     * "value":"8080",
     * "dataType":"NUMBER",
     * "name":"port",
     * "allowMultiple":false
     * }
     * ]
     * }
     * }
     * @apiError {String} message Errormessage
     */
    @PutMapping("/imp/configuration")
    public void updateConfiguration(@ModelAttribute Configuration configuration, @ModelAttribute("impId") long moduleId, @ModelAttribute("sessionId") long sessionId) {
        aasConfigurationService.updateConfiguration(sessionId, configuration, moduleId);
    }

    /**
     * @api{delete}/aas/imp
     * @apiName DeleteModleImp
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} [Authorization] Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request body) {Number} sessionId Session unique ID
     * @apiParam (Request body) {Number} impId Implementation unique ID
     * @apiError {String} message Errormessage
     */
    @DeleteMapping("{/imp")
    public void deleteModuleImp(@ModelAttribute("impId") long moduleId, @ModelAttribute("sessionId") long sessionId) {
        aasConfigurationService.removeModuleInstance(sessionId, moduleId);
    }

    /**
     * @api{get}/aas/file Generates a .java file which starts the AAS
     * @apiName GenerateJavaClass
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} [Authorization] Authorization header using the Bearer schema: Bearer token
     * @apiParam {Number} sessionId Session unique ID
     * @apiError {String} message Errormessage
     */
    @GetMapping("{/sessionId:[0-9]+}/generate")
    public byte[] generateJavaClass(@PathVariable("sessionId") long sessionId) throws IOException {
        return Files.toByteArray(aasConfigurationService.generateJavaClass(sessionId));
    }
}
