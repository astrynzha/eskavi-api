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
     * @api{get}/imp/user Get Implementations by user
     * @apiName GetImplementationByUser
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "UserNotFound"
     * }
     */
    @GetMapping("/user/{userId:[0-9]+}")
    public Collection<ImmutableImplementation> getAllByUser(@PathVariable long userId) {
        return null;
    }

    /**
     * @api{get}/imp/:id Get Implementation
     * @apiName GetImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam {Number} id Implementation unique ID
     * @apiSuccess {Implementation} implementation Implementation object
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "UserNotFound"
     * }
     */
    @GetMapping("/{id:[0-9]+}")
    public ImmutableImplementation get(@PathVariable("id") long impId) {
        return null;
    }

    @GetMapping("/default")
    public ImmutableImplementation getDefaultImpCreate(@PathParam("type") ImpType type) {
        return null;
    }

    @GetMapping("/template/{id:[0-9]+}")
    public ImmutableImplementation getTemplateImpCreate(@PathVariable("id") long impId) {
        return null;
    }

    /**
     * @api{post}/imp Post Implementation
     * @apiName PostImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {Number} id Implementation unique ID
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 201 Created
     * {
     * "id":"1"
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
     * @apiName PostImplementation
     * @apiGroup Implementation
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiError {String} message Errormessage
     */
    @PutMapping
    public void put(ImmutableImplementation request) {
    }

    @PostMapping("/user")
    public void addUser(Long implementationId, ImmutableUser user) {
    }

    @DeleteMapping("/user")
    public void removeUser(Long implementationId, ImmutableUser user) {
    }

    @DeleteMapping("/{id:[0-9]+}")
    public void delete(@PathVariable("id") long impId) {
    }
}