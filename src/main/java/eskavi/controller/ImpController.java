package eskavi.controller;

import eskavi.model.implementation.ImmutableImplementation;
import eskavi.model.implementation.ImpType;
import eskavi.model.user.ImmutableUser;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Collection;

@RestController
@RequestMapping("imp")
public class ImpController {

    /**
     * @api{get}/imp/:id Get Implementation
     * @apiName GetImplementation
     * @apiGroup Implementation
     * @apiDescription Gets all implementations or a specific implementation (if impId is provided), the user (if provided through token) has access to. If token is not provided, only public implementations will be returned/accessible through this call.
     * @apiVersion 0.0.1
     * @apiHeader {String} [Authorization] Authorization header using the Bearer schema: Bearer token
     * @apiParam {Number} [id] Implementation unique ID
     * @apiSuccess {Implementation} implementation Implementation object
     * @apiSuccessExample Success-Example:
     * {
     *    "implementationId":10,
     *    "author":{
     *       "emailAddress":"a@gmail.com",
     *       "securityQuestion":"MAIDEN_NAME",
     *       "securityAnswer":"Julia",
     *       "userLevel":"PUBLISHING_USER",
     *       "password":"dfjask;fj",
     *       "subscribed":[
     *
     *       ]
     *    },
     *    "name":"handler_10",
     *    "scope":{
     *       "impScope":"SHARED"
     *    },
     *    "messageType":{
     *       "implementationId":3,
     *       "author":{
     *          "emailAddress":"a@gmail.com",
     *          "securityQuestion":"MAIDEN_NAME",
     *          "securityAnswer":"Julia",
     *          "userLevel":"PUBLISHING_USER",
     *          "password":"dfjask;fj",
     *          "subscribed":[
     *
     *          ]
     *       },
     *       "name":"messageType_3",
     *       "scope":{
     *          "impScope":"SHARED"
     *       }
     *    },
     *    "generics":[
     *       {
     *          "implementationId":3,
     *          "author":{
     *             "emailAddress":"a@gmail.com",
     *             "securityQuestion":"MAIDEN_NAME",
     *             "securityAnswer":"Julia",
     *             "userLevel":"PUBLISHING_USER",
     *             "password":"dfjask;fj",
     *             "subscribed":[
     *
     *             ]
     *          },
     *          "name":"messageType_3",
     *          "scope":{
     *             "impScope":"SHARED"
     *          }
     *       }
     *    ]
     * }
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "UserNotFound"
     * }
     */
    @GetMapping("/{id:[0-9]+}")
    public ImmutableImplementation get(@PathVariable("id") long impId, ImmutableUser user) {
        return null;
    }

    /**
     * @api{get}/imp/types Get Implementation types
     * @apiDescription Gets all types of implementations that can be created.
     * @apiName getTypes
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {String[]} types Array of Strings
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *     "types": ["Serializer", "Deserializer", "Handler"]
     * }
     * @apiError {String} message Errormessage
     */
    @GetMapping("/types")
    public ImpType getImplementationTypes(ImmutableUser user) {return null;}

    /**
     * @api{get}/imp/config/data_types Get Data types of Configuration
     * @apiDescription Gets all types of data that can be used for Configuration fields.
     * @apiName getConfigDataTypes
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {String[]} data_types Array of Strings
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *     "data_types": ["text", "number", "email", "date"]
     * }
     * @apiError {String} message Errormessage
     */
    @GetMapping("/data_types")
    public ImpType getConfigDataTypes(ImmutableUser user) {return null;}

    /**
     * @api{get}/imp/scopes Get possible implementation scopes
     * @apiDescription Gets all accessibility scopes possible for implementations
     * @apiName getImpScopes
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {String[]} impScopes Array of Strings
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *     "impScopes": ["private", "shared", "public"]
     * }
     * @apiError {String} message Errormessage
     */
    @GetMapping("/scopes")
    public ImpType getImpScopes(ImmutableUser user) {return null;}

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
        return null;
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
     *    "implementationId":10,
     *    "author":{
     *       "emailAddress":"a@gmail.com",
     *       "securityQuestion":"MAIDEN_NAME",
     *       "securityAnswer":"Julia",
     *       "userLevel":"PUBLISHING_USER",
     *       "password":"dfjask;fj",
     *       "subscribed":[
     *
     *       ]
     *    },
     *    "name":"handler_10",
     *    "scope":{
     *       "impScope":"SHARED"
     *    },
     *    "messageType":{
     *       "implementationId":3,
     *       "author":{
     *          "emailAddress":"a@gmail.com",
     *          "securityQuestion":"MAIDEN_NAME",
     *          "securityAnswer":"Julia",
     *          "userLevel":"PUBLISHING_USER",
     *          "password":"dfjask;fj",
     *          "subscribed":[
     *
     *          ]
     *       },
     *       "name":"messageType_3",
     *       "scope":{
     *          "impScope":"SHARED"
     *       }
     *    },
     *    "generics":[
     *       {
     *          "implementationId":3,
     *          "author":{
     *             "emailAddress":"a@gmail.com",
     *             "securityQuestion":"MAIDEN_NAME",
     *             "securityAnswer":"Julia",
     *             "userLevel":"PUBLISHING_USER",
     *             "password":"dfjask;fj",
     *             "subscribed":[
     *
     *             ]
     *          },
     *          "name":"messageType_3",
     *          "scope":{
     *             "impScope":"SHARED"
     *          }
     *       }
     *    ]
     * }
     * @apiSuccess {Number} impId Implementation unique ID
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 201 Created
     * {
     * "impId":"1"
     * }
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 403 Forbidden
     * {
     * "error": "Access denied for non publishing user"
     * }
     */
    @PostMapping
    public void add(ImmutableImplementation mi) {
    }

    /**
     * @api{put}/imp Put Implementation
     * @apiName PutImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request body) {Implementation} implementation Implementation object
     * @apiSuccess {Number} impId Implementation unique ID
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 201 Created
     * {
     * "impId":"1"
     * }
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 403 Forbidden
     * {
     * "error": "Access denied for non publishing user"
     * }
     */
    @PutMapping
    public void put(ImmutableImplementation request) {
    }

    /**
     * @api{post}/imp/user Add User to Implementation
     * @apiName AddUserToImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiError {String} message Errormessage
     * @apiParam (Request body) {User} user User object
     * @apiParam (Request body) {Number} impId Implementation unique ID
     */
    @PostMapping("/{id:[0-9]+}/user")
    public void addUser(@PathVariable("id") Long implementationId, ImmutableUser user) {
    }

    /**
     * @api{delete}/imp/user Remove User from Implementation
     * @apiName RemoveUserFromImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiError {String} message Errormessage
     * @apiParam (Request body) {User} user User object
     * @apiParam (Request body) {Number} impId Implementation unique ID
     */
    @DeleteMapping("/user")
    public void removeUser(Long implementationId, ImmutableUser user) {
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
    public void delete(@PathVariable("id") long impId) {
    }
}