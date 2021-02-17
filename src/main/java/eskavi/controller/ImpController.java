package eskavi.controller;

import eskavi.controller.requests.imp.AddUserRequest;
import eskavi.controller.requests.imp.RemoveUserRequest;
import eskavi.model.configuration.ConfigurationType;
import eskavi.model.configuration.DataType;
import eskavi.model.implementation.ImmutableImplementation;
import eskavi.model.implementation.ImpType;
import eskavi.model.implementation.Implementation;
import eskavi.model.implementation.ImplementationScope;
import eskavi.model.user.ImmutableUser;
import eskavi.service.ImpService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

@RestController
@RequestMapping("imp")
public class ImpController {

    final ImpService impService;
    final UserTokenMatcher userTokenMatcher;

    public ImpController(ImpService impService, UserTokenMatcher userTokenMatcher) {
        this.impService = impService;
        this.userTokenMatcher = userTokenMatcher;
    }

    /**
     * @api{get}/imp Get Implementation
     * @apiName GetImplementation
     * @apiGroup Implementation
     * @apiDescription Gets all implementations, specific type of implementations or a specific one (if impId is provided),
     * the user (if provided through token) has access to. If token is not provided, only public implementations will be returned/accessible through this call.
     * @apiVersion 0.0.1
     * @apiHeader {String} [Authorization] Authorization header using the Bearer schema: Bearer token
     * @apiParam (queryStringParameter) {Number} [id] Implementation unique ID
     * @apiParam (queryStringParameter) {String} [impType] Type of implementation to be displayed, see getImplementationTypes
     * @apiSuccess {Implementation[]} imps Array of implementation objects. One, if called with specific Id.
     * @apiSuccessExample Success-Example:
     * {
     * implementations: [
     * {
     * "jsonTypeInfo":"PersistenceManager",
     * "implementationId":13,
     * "author":"a@gmail.com",
     * "name":"manager",
     * "scope":{
     * "scopeId":0,
     * "impScope":"SHARED"
     * },
     * "configurationRoot":{
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
     * {
     * "jsonTypeInfo":"Endpoint",
     * "implementationId":11,
     * "author":"a@gmail.com",
     * "name":"endpoint",
     * "scope":{
     * "scopeId":0,
     * "impScope":"SHARED"
     * },
     * "configurationRoot":{
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
     * 3,
     * 0
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
     * 3,
     * 0
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
     * },
     * "protocolType":{
     * "jsonTypeInfo":"ProtocolType",
     * "implementationId":0,
     * "author":"a@gmail.com",
     * "name":"protocolType_0",
     * "scope":{
     * "scopeId":0,
     * "impScope":"SHARED"
     * }
     * }
     * }
     * ]
     * @apiError {String} error Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "UserNotFound"
     * }
     */
    @GetMapping
    public Collection<ImmutableImplementation> get(@RequestParam(value = "id", required = false) Long impId,
                                                   @RequestParam(value = "impType", required = false) String impType,
                                                   @RequestHeader String jwtToken) {
        ImmutableUser user = userTokenMatcher.getUser(jwtToken);
        if (impId != null) {
            return Arrays.asList(impService.getImp(impId));
        } else if (impType != null) {
            return impService.getImps(ImpType.valueOf(impType));
        } else {
            return impService.getImps(user);
        }
    }

    /**
     * @api{get}/imp/types Get Implementation types
     * @apiDescription Gets all types of implementations that can be created.
     * @apiName getTypes
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {String[]} types Array of implementation types
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * [
     * {
     * "topLevel":true,
     * "name":"ASSET_CONNECTION"
     * },
     * {
     * "topLevel":false,
     * "name":"DESERIALIZER"
     * },
     * {
     * "topLevel":false,
     * "name":"DISPATCHER"
     * },
     * {
     * "topLevel":true,
     * "name":"ENDPOINT"
     * },
     * {
     * "topLevel":false,
     * "name":"HANDLER"
     * },
     * {
     * "topLevel":true,
     * "name":"INTERACTION_STARTER"
     * },
     * {
     * "topLevel":true,
     * "name":"PERSISTENCE_MANAGER"
     * },
     * {
     * "topLevel":false,
     * "name":"SERIALIZER"
     * }
     * ]
     * }
     * @apiError {String} message Errormessage
     */
    @GetMapping("/types")
    public Collection<ImpType> getImplementationTypes() {
        return EnumSet.allOf(ImpType.class);
    }

    /**
     * @return
     * @api{get}/imp/config/data_types Get Data types of Configuration
     * @apiDescription Gets all types of data that can be used for Configuration fields.
     * @apiName getConfigDataTypes
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {String[]} data_types Array of data types
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "data_types": ["text", "number", "email", "date"]
     * }
     * @apiError {String} message Errormessage
     */
    @GetMapping("/config/data_types")
    public Collection<DataType> getConfigDataTypes(ImmutableUser user) {
        return EnumSet.allOf(DataType.class);
    }


    /**
     * @api{get}/imp/types Get ConfigurationTe Templates
     * @apiDescription Gets templates for all possible configuration types.
     * @apiName getConfigTemplates
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {String[]} types Array of implementation types
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * [
     * {
     * "template":{
     * "jsonTypeInfo":"ConfigurationAggregate",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"",
     * "allowMultiple":false,
     * "children":[
     * <p>
     * ]
     * }
     * },
     * {
     * "template":{
     * "jsonTypeInfo":"TextField",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "dataType":"TEXT",
     * "name":"",
     * "allowMultiple":false
     * }
     * },
     * {
     * "template":{
     * "jsonTypeInfo":"ImplementationSelect",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "generics":[
     * <p>
     * ],
     * "type":"SERIALIZER",
     * "name":"",
     * "allowMultiple":false
     * }
     * },
     * {
     * "template":{
     * "jsonTypeInfo":"Select",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "content":{
     * <p>
     * },
     * "name":"",
     * "allowMultiple":false
     * }
     * },
     * {
     * "template":{
     * "jsonTypeInfo":"FileField",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "name":"",
     * "allowMultiple":false
     * }
     * },
     * {
     * "template":{
     * "jsonTypeInfo":"Switch",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "value":"",
     * "content":{
     * "falseValue":"",
     * "trueValue":""
     * },
     * "name":"",
     * "allowMultiple":false
     * }
     * }
     * ]
     * @apiError {String} message Errormessage
     */
    @GetMapping("/configTemplates")
    public Collection<ConfigurationType> getConfigTemplates() {
        return EnumSet.allOf(ConfigurationType.class);
    }

    /**
     * @return
     * @api{get}/imp/scopes Get possible implementation scopes
     * @apiDescription Gets all accessibility scopes possible for implementations
     * @apiName getImpScopes
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {String[]} impScopes Array of scopes
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "impScopes": ["private", "shared", "public"]
     * }
     * @apiError {String} message Errormessage
     */
    @GetMapping("/scopes")
    public Collection<ImplementationScope> getImpScopes() {
        return EnumSet.allOf(ImplementationScope.class);
    }

    /**
     * @api{get}/imp/default GetDefaultImplementation for ImplementationType
     * @apiName getDefaultImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam {ImpType} impType The implementation type
     * @apiParamExample {json} RequestExample:
     * {
     * "impType":"Handler"
     * }
     * @apiSuccess {Implementation} implementation Implementation object
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "UserNotFound"
     * }
     */
    @GetMapping("/default")
    public ImmutableImplementation getDefaultImpCreate(@PathParam("type") ImpType type) {
        return impService.getDefaultImpCreate(type);
    }


    /**
     * @api{post}/imp Post Implementation
     * @apiName PostImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request body) {Implementation} implementation Implementation object
     * @apiParamExample {json} Request-Example:
     * {
     * "jsonTypeInfo":"Deserializer",
     * "implementationId":7,
     * "author":"a@gmail.com",
     * "name":"deserializer_7",
     * "scope":{
     * "scopeId":0,
     * "impScope":"SHARED"
     * },
     * "configurationRoot":{
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
     * },
     * "protocolType":{
     * "jsonTypeInfo":"ProtocolType",
     * "implementationId":0,
     * "author":"a@gmail.com",
     * "name":"protocolType_0",
     * "scope":{
     * "scopeId":0,
     * "impScope":"SHARED"
     * }
     * },
     * "messageType":{
     * "jsonTypeInfo":"MessageType",
     * "implementationId":3,
     * "author":"a@gmail.com",
     * "name":"messageType_3",
     * "scope":{
     * "scopeId":0,
     * "impScope":"SHARED"
     * }
     * }
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 201 Created
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 403 Forbidden
     * {
     * "error": "Access denied for non publishing user"
     * }
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestHeader String jwtToken, @RequestBody Implementation mi) {
        ImmutableUser user = userTokenMatcher.getUser(jwtToken);
        impService.addImplementation(mi, user.getEmailAddress());
    }

    /**
     * @api{put}/imp Put Implementation
     * @apiName PutImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request body) {Implementation} implementation Implementation object for example view PostImplementation
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 201 Created
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 403 Forbidden
     * {
     * "error": "Access denied for non publishing user"
     * }
     */
    @PutMapping
    public void put(@RequestHeader String jwtToken, @RequestBody ImmutableImplementation request) throws IllegalAccessException {
        ImmutableUser user = userTokenMatcher.getUser(jwtToken);
        impService.updateImplementation(request, user.getEmailAddress());
    }

    /**
     * @api{post}/imp/user Add User to Implementation
     * @apiName AddUserToImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiError {String} message Errormessage
     * @apiParam (Request body) {String} userId User unique ID
     * @apiParam (Request body) {Number} impId Implementation unique ID
     */
    @PostMapping("/user")
    public void addUser(@RequestHeader String jwtToken, @RequestBody AddUserRequest request) throws IllegalAccessException {
        ImmutableUser user = userTokenMatcher.getUser(jwtToken);
        impService.addUser(request.getImpId(), request.getUserId(), user.getEmailAddress());
    }

    /**
     * @api{delete}/imp/user Remove User from Implementation
     * @apiName RemoveUserFromImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiError {String} message Errormessage
     * @apiParam (Request body) {String} userId User unique ID
     * @apiParam (Request body) {Number} impId Implementation unique ID
     */
    @DeleteMapping("/user")
    public void removeUser(@RequestHeader String jwtToken, @RequestBody RemoveUserRequest request) throws IllegalAccessException {
        ImmutableUser user = userTokenMatcher.getUser(jwtToken);
        impService.removeUser(request.getImpId(), request.getUserId(), user.getEmailAddress());
    }

    /**
     * @api{delete}/imp/ Delete Implementation
     * @apiName DeleteImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request body) {Number} impId Implementation unique ID
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "ImplementationNotFound"
     * }
     */
    @DeleteMapping("/{id:[0-9]+}")
    public void delete(@RequestHeader String jwtToken, @PathVariable("id") long impId) throws IllegalAccessException {
        ImmutableUser user = userTokenMatcher.getUser(jwtToken);
        impService.removeImplementation(impId, user.getEmailAddress());
    }
}