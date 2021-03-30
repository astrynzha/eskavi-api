package eskavi.controller;

import com.google.common.io.Files;
import eskavi.controller.requests.aas.AddModuleImpRequest;
import eskavi.controller.requests.aas.AddRegistryRequest;
import eskavi.controller.requests.aas.UpdateConfigurationRequest;
import eskavi.controller.responses.aas.CreateSessionResponse;
import eskavi.controller.responses.aas.GetConfigurationResponse;
import eskavi.controller.responses.aas.GetImpsResponse;
import eskavi.model.user.ImmutableUser;
import eskavi.service.aasconfigurationservice.AASConfigurationService;
import eskavi.service.aasconfigurationservice.AASConstructionSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("api/aas")
public class AASConfigurationController {

    final AASConfigurationService aasConfigurationService;
    final UserTokenMatcher userTokenMatcher;

    public AASConfigurationController(AASConfigurationService aasConfigurationService, UserTokenMatcher userTokenMatcher) {
        this.aasConfigurationService = aasConfigurationService;
        this.userTokenMatcher = userTokenMatcher;
    }

    /**
     * @return
     * @api{post}/aas Post AAS session
     * @apiName CreateSession
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} [Authorization] Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {Number} sessionId Session unique ID
     * @apiError {String} message Errormessage
     */
    @PostMapping
    public CreateSessionResponse createSession(@RequestHeader(required = false) String Authorization) {
        if (Authorization == null) {
            return new CreateSessionResponse(aasConfigurationService.createAASConstructionSessionWithoutUser());
        }
        ImmutableUser user = userTokenMatcher.getUser(Authorization);
        return new CreateSessionResponse(aasConfigurationService.createAASConstructionSession(user.getEmailAddress()));
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
    public void closeSession(@RequestHeader(required = false) String Authorization, @RequestParam("sessionId") long sessionId) {
        if (hasAccess(Authorization, sessionId)) {
            aasConfigurationService.removeAASConstructionSession(sessionId);
        }
    }

    @PostMapping("/registry")
    public void addRegistry(@RequestBody AddRegistryRequest registryRequest) {
        aasConfigurationService.addRegistry(registryRequest.getSessionId(), registryRequest.getUrl());
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
    @PostMapping("/imp")
    public void addModuleImp(@RequestHeader(required = false) String Authorization, @RequestBody AddModuleImpRequest request) {

        if (hasAccess(Authorization, request.getSessionId())) {
            aasConfigurationService.addModuleInstance(request.getSessionId(), request.getImpId());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private boolean hasAccess(String Authorization, long sessionId) {
        AASConstructionSession session = aasConfigurationService.getSessionById(sessionId);
        ImmutableUser caller = null;
        if (Authorization != null) {
            caller = userTokenMatcher.getUser(Authorization);
        }
        return Objects.equals(session.getOwner(), caller);
    }

    /**
     * @api{post}/aas/toplevelimps Get all Top-Level imps of the given Session
     * @apiName GetTopLevel
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} [Authorization] Authorization header using the Bearer schema: Bearer token
     * @apiParam (Query String param) {Number} sessionId Session unique ID
     * @apiError {String} message Errormessage
     */
    @GetMapping("toplevelimps")
    public GetImpsResponse getImps(@RequestParam long sessionId) {
        return new GetImpsResponse(aasConfigurationService.getTopLevelImps(sessionId));
    }

    /**
     * @return
     * @api{get}/aas/imp/configuration Get Configuration from ModuleImplementation in Session
     * @apiName GetConfiguration
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} [Authorization] Authorization header using the Bearer schema: Bearer token
     * @apiParam (queryStringParameter) {Number} sessionId Session unique ID
     * @apiParam (queryStringParameter) {Number} impId Implementation unique ID
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
    public GetConfigurationResponse getConfiguration(@RequestParam long sessionId, @RequestParam long impId) {
        return new GetConfigurationResponse(aasConfigurationService.getConfiguration(sessionId, impId));
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
    public void updateConfiguration(@RequestHeader(required = false) String Authorization, @RequestBody UpdateConfigurationRequest request) {
        if (hasAccess(Authorization, request.getSessionId())) {
            aasConfigurationService.updateConfiguration(request.getSessionId(), request.getConfiguration(), request.getImpId());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
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
    @DeleteMapping("/imp")
    public void deleteModuleImp(@RequestHeader(required = false) String Authorization, @RequestParam long moduleId, @RequestParam long sessionId) {
        if (hasAccess(Authorization, sessionId)) {
            aasConfigurationService.removeModuleInstance(sessionId, moduleId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
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
    @GetMapping("/generate")
    public byte[] generateJavaClass(@RequestParam("sessionId") long sessionId) throws IOException {
        return Files.toByteArray(aasConfigurationService.generateJavaClass(sessionId));
    }
}
