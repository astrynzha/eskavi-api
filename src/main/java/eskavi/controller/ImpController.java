package eskavi.controller;

import eskavi.controller.requests.imp.AddImplementationResponse;
import eskavi.controller.requests.imp.AddUserRequest;
import eskavi.controller.requests.imp.RemoveUserRequest;
import eskavi.controller.responses.imp.*;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

@CrossOrigin
@RestController
@RequestMapping("api/imp")
public class ImpController {

    final ImpService impService;
    final UserTokenMatcher userTokenMatcher;

    public ImpController(ImpService impService, UserTokenMatcher userTokenMatcher) {
        this.impService = impService;
        this.userTokenMatcher = userTokenMatcher;
    }

    /**
     * @return
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
     * "jsonTypeInfo":"PERSISTENCE_MANAGER",
     * "implementationId":13,
     * "author":"a@gmail.com",
     * "name":"manager",
     * "scope":{
     * "scopeId":0,
     * "impScope":"SHARED"
     * },
     * "configurationRoot":{
     * "jsonTypeInfo":"CONFIGURATION_AGGREGATE",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TEXT_FIELD",
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
     * "jsonTypeInfo":"ENDPOINT",
     * "implementationId":11,
     * "author":"a@gmail.com",
     * "name":"endpoint",
     * "scope":{
     * "scopeId":0,
     * "impScope":"SHARED"
     * },
     * "configurationRoot":{
     * "jsonTypeInfo":"CONFIGURATION_AGGREGATE",
     * "keyExpression":{
     * "expressionStart":"<parent>",
     * "expressionEnd":"<parent>"
     * },
     * "enforceCompatibility":false,
     * "name":"parent",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"CONFIGURATION_AGGREGATE",
     * "keyExpression":{
     * "expressionStart":"<mapping>",
     * "expressionEnd":"<mapping>"
     * },
     * "enforceCompatibility":true,
     * "name":"mapping",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TEXT_FIELD",
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
     * "jsonTypeInfo":"IMPLEMENTATION_SELECT",
     * "keyExpression":{
     * "expressionStart":"<serializer>",
     * "expressionEnd":"<serializer>"
     * },
     * "instance":{
     * "moduleImp":8,
     * "instanceConfiguration":{
     * "jsonTypeInfo":"CONFIGURATION_AGGREGATE",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TEXT_FIELD",
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
     * "jsonTypeInfo":"IMPLEMENTATION_SELECT",
     * "keyExpression":{
     * "expressionStart":"<deserializer>",
     * "expressionEnd":"<deserializer>"
     * },
     * "instance":{
     * "moduleImp":7,
     * "instanceConfiguration":{
     * "jsonTypeInfo":"CONFIGURATION_AGGREGATE",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TEXT_FIELD",
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
     * "jsonTypeInfo":"IMPLEMENTATION_SELECT",
     * "keyExpression":{
     * "expressionStart":"<dispatcher>",
     * "expressionEnd":"<dispatcher>"
     * },
     * "instance":{
     * "moduleImp":9,
     * "instanceConfiguration":{
     * "jsonTypeInfo":"CONFIGURATION_AGGREGATE",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"IMPLEMENTATION_SELECT",
     * "keyExpression":{
     * "expressionStart":"<handler>",
     * "expressionEnd":"<handler>"
     * },
     * "instance":{
     * "moduleImp":10,
     * "instanceConfiguration":{
     * "jsonTypeInfo":"CONFIGURATION_AGGREGATE",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TEXT_FIELD",
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
     * "jsonTypeInfo":"TEXT_FIELD",
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
     * "jsonTypeInfo":"PROTOCOL_TYPE",
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
    public GetImplementationsResponse get(@RequestParam(value = "id", required = false) Long impId,
                                          @RequestParam(value = "impType", required = false) String impType,
                                          @RequestParam(value = "generics", required = false) Collection<Long> generics,
                                          @RequestHeader(required = false) String Authorization) {
        ImmutableUser user = Authorization != null ? userTokenMatcher.getUser(Authorization) : impService.getPublicUser();
        ImpType type = impType != null ? ImpType.valueOf(impType) : null;
        if (impId != null) {
            return new GetImplementationsResponse(Arrays.asList(impService.getImp(impId, user.getEmailAddress())));
        }
        if (generics != null) {
            return new GetImplementationsResponse(impService.getImps(type, generics, user.getEmailAddress()));
        }
        if (impType != null) {
            return new GetImplementationsResponse(impService.getImps(type, user.getEmailAddress()));
        }
        return new GetImplementationsResponse(impService.getImps(user));
    }

    /**
     * @return
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
     * "name":"ASSET_CONNECTION",
     * "topLevel":true,
     * "maxUse":-1
     * },
     * {
     * "name":"DESERIALIZER",
     * "topLevel":false,
     * "maxUse":-1
     * },
     * {
     * "name":"DISPATCHER",
     * "topLevel":false,
     * "maxUse":-1
     * },
     * {
     * "name":"ENDPOINT",
     * "topLevel":true,
     * "maxUse":-1
     * },
     * {
     * "name":"HANDLER",
     * "topLevel":false,
     * "maxUse":-1
     * },
     * {
     * "name":"INTERACTION_STARTER",
     * "topLevel":true,
     * "maxUse":-1
     * },
     * {
     * "name":"PERSISTENCE_MANAGER",
     * "topLevel":true,
     * "maxUse":1
     * },
     * {
     * "name":"SERIALIZER",
     * "topLevel":false,
     * "maxUse":1
     * },
     * {
     * "name":"PROTOCOL_TYPE",
     * "topLevel":false,
     * "maxUse":0
     * },
     * {
     * "name":"MESSAGE_TYPE",
     * "topLevel":false,
     * "maxUse":0
     * }
     * ]
     * }
     * @apiError {String} message Errormessage
     */
    @GetMapping("/types")
    public ImpTypesResponse getImplementationTypes() {
        return new ImpTypesResponse(EnumSet.allOf(ImpType.class));
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
     * ["TEXT","NUMBER","EMAIL","PASSWORD","DATE","DATETIME"]
     * }
     * @apiError {String} message Errormessage
     */
    @GetMapping("/config/data_types")
    public DataTypesResponse getConfigDataTypes(ImmutableUser user) {
        return new DataTypesResponse(EnumSet.allOf(DataType.class));
    }


    /**
     * @return
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
     * "jsonTypeInfo":"CONFIGURATION_AGGREGATE",
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
     * "jsonTypeInfo":"TEXT_FIELD",
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
     * "jsonTypeInfo":"IMPLEMENTATION_SELECT",
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
     * "jsonTypeInfo":"SELECT",
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
     * "jsonTypeInfo":"FILE_FIELD",
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
     * "jsonTypeInfo":"SWITCH",
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
    public ConfigurationTemplatesResponse getConfigTemplates() {
        return new ConfigurationTemplatesResponse(EnumSet.allOf(ConfigurationType.class));
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
     * ["PRIVATE", "SHARED", "PUBLIC"]
     * }
     * @apiError {String} message Errormessage
     */
    @GetMapping("/scopes")
    public ImpScopesResponse getImpScopes() {
        return new ImpScopesResponse(EnumSet.allOf(ImplementationScope.class));
    }

    /**
     * @return
     * @api{get}/imp/default GetDefaultImplementation for ImplementationType
     * @apiName getDefaultImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam {ImpType} impType The implementation type
     * @apiSuccess {Implementation} implementation Implementation object
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "UserNotFound"
     * }
     */
    @GetMapping("/default")
    public GetDefaultImpResponse getDefaultImpCreate(@RequestParam(value = "impType", required = false) String impType) {
        return new GetDefaultImpResponse(impService.getDefaultImpCreate(ImpType.valueOf(impType)));
    }


    /**
     * @return
     * @api{post}/imp Post Implementation
     * @apiName PostImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request body) {Implementation} implementation Implementation object
     * @apiParamExample {json} Request-Example:
     * {
     * "jsonTypeInfo":"DESERIALIZER",
     * "implementationId":7,
     * "author":"a@gmail.com",
     * "name":"deserializer_7",
     * "scope":{
     * "scopeId":0,
     * "impScope":"SHARED"
     * },
     * "configurationRoot":{
     * "jsonTypeInfo":"CONFIGURATION_AAGGREGATE",
     * "keyExpression":{
     * "expressionStart":"",
     * "expressionEnd":""
     * },
     * "enforceCompatibility":false,
     * "name":"root",
     * "allowMultiple":false,
     * "children":[
     * {
     * "jsonTypeInfo":"TEXT_FIELD",
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
     * "jsonTypeInfo":"PROTOCOL_TYPE",
     * "implementationId":0,
     * "author":"a@gmail.com",
     * "name":"protocolType_0",
     * "scope":{
     * "scopeId":0,
     * "impScope":"SHARED"
     * }
     * },
     * "messageType":{
     * "jsonTypeInfo":"MESSAGE_TYPE",
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
    public AddImplementationResponse add(@RequestHeader String Authorization, @RequestBody Implementation mi) {
        ImmutableUser user = userTokenMatcher.getUser(Authorization);
        return new AddImplementationResponse(impService.addImplementation(mi, user.getEmailAddress()));
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
    public void put(@RequestHeader String Authorization, @RequestBody ImmutableImplementation request) throws IllegalAccessException {
        ImmutableUser user = userTokenMatcher.getUser(Authorization);
        impService.updateImplementation(request, user.getEmailAddress());
    }

    /**
     * @api{post}/imp/user Add User to Implementation
     * @apiName AddUserToImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiError {String} message Errormessage
     * @apiParam (Request body) {String[]} userIds Array of unique User IDs
     * @apiParam (Request body) {Number} impId Implementation unique ID
     */
    //TODO /user or /users?
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestHeader String Authorization, @RequestBody AddUserRequest request) throws IllegalAccessException {
        ImmutableUser user = userTokenMatcher.getUser(Authorization);
        for (String userId : request.getUserIds()) {
            impService.addUser(request.getImpId(), userId, user.getEmailAddress());
        }
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
    public void removeUser(@RequestHeader String Authorization, @RequestBody RemoveUserRequest request) throws IllegalAccessException {
        ImmutableUser user = userTokenMatcher.getUser(Authorization);
        impService.removeUser(request.getImpId(), request.getUserId(), user.getEmailAddress());
    }

    /**
     * @api{delete}/imp/ Delete Implementation
     * @apiName DeleteImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request Parameter) {Number} id Implementation unique ID
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "ImplementationNotFound"
     * }
     */
    @DeleteMapping
    public void delete(@RequestHeader String Authorization, @RequestParam("id") long impId) throws IllegalAccessException {
        ImmutableUser user = userTokenMatcher.getUser(Authorization);
        impService.removeImplementation(impId, user.getEmailAddress());
    }
}