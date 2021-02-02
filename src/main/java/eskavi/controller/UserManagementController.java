package eskavi.controller;

import eskavi.model.user.ImmutableUser;
import eskavi.model.user.UserLevel;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("user")
public class UserManagementController {

    /**
     * @api{post}/user/register Register a new User
     * @apiName Register
     * @apiGroup User
     * @apiVersion 0.0.1
     * @apiSuccess {String} message Returns whether a request was successful
     * @apiParam (Request body) {String} email User mail
     * @apiParam (Request body) {String} password User password
     * @apiParamExample {json} Request-Example:
     * {
     * "email": "test@web.de",
     * "password": "12345678"
     * }
     */
    @PostMapping("/register")
    public void register(@ModelAttribute("email") String email, @ModelAttribute("password") String password) {

    }

    /**
     * @api{post}/user/login Login for a registered User
     * @apiName Login
     * @apiGroup User
     * @apiVersion 0.0.1
     * @apiSuccess {String} jwt Token to authenticate future requests
     * @apiError {String} message Errormessage
     * @apiParam (Request body) {String} email User mail
     * @apiParam (Request body) {String} password User password
     * @apiParamExample {json} Request-Example:
     * {
     * "email": "test@web.de",
     * "password": "12345678"
     * }
     */
    @PostMapping("/login")
    public String login(@ModelAttribute("email") String email, @ModelAttribute("password") String password) {
        return null;
    }

    /**
     * @api{get}/user Get User information
     * @apiName GetUser
     * @apiGroup User
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {User} user User object
     * @apiSuccessExample {json} Success-Example:
     * {
     *   "user":{
     *     "email":"test@web.de",
     *     "password":"$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8",
     *     "securityQuestion":"petName",
     *     "securityAnswer":"Jim"
     *     "userLevel":"BasicUser"
     *   }
     * }
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "UserNotFound"
     * }
     */
    @RequestMapping(method = RequestMethod.GET)
    public ImmutableUser getUser(@RequestHeader String jwtToken) {
        return null;
    }

    /**
     * @api{delete}/user Delete User
     * @apiName DeleteUser
     * @apiGroup User
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 404 Not Found
     * {
     * "error": "UserNotFound"
     * }
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteUser(@RequestHeader String jwtToken) {

    }

    /**
     * @api{get}/user/security_question Get security question
     * @apiName GetSecurityQuestion
     * @apiGroup User
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {String} securityQuestion Security question to reset the password
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "error": "Unauthorized please login to your account"
     * }
     */
    @GetMapping("/security_question")
    public String geSecurityQuestion(@RequestHeader String jwtToken) {
        return null;
    }

    /**
     * @api{post}/user/reset_password Resets the password after security question was answered
     * @apiName ResetPasswordQuestion
     * @apiGroup User
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request body) {String} answer Answer to the security question
     * @apiParam (Request body) {String} newPassword New password
     * @apiParamExample {json} Request-Example:
     * {
     * "answer": "Jim",
     * "newPassword": "password"
     * }
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "error": "Unauthorized please login to your account"
     * }
     */
    @PostMapping("/reset_password")
    public void resetPassword(@ModelAttribute String answer, @ModelAttribute String newPassword) {

    }

    /**
     * @api{post}/user/security_question Set a new password
     * @apiName SetPasswordQuestion
     * @apiGroup User
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request body) {String} oldPassword Old password
     * @apiParam (Request body) {String} newPassword New password
     * @apiParamExample {json} Request-Example:
     * {
     * "oldPassword": "12345678",
     * "newPassword": "password"
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "error": "Unauthorized please login to your account"
     * }
     */
    @PostMapping("/set_password")
    public void setPassword(@ModelAttribute String oldPassword, @ModelAttribute String newPassword) {

    }

    /**
     * @api{put}/user/level Set user level
     * @apiName SetUserLevel
     * @apiGroup User
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 403 Forbidden
     * {
     * "error": "Access denied for non admin user"
     * }
     */
    @PostMapping("/level")
    public void setUserLevel(@ModelAttribute("email") String email, @ModelAttribute("email") UserLevel userLevel) {

    }


    /**
     * @api{get}/user/levels Get user levels
     * @apiName GetUserLevels
     * @apiGroup User
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 403 Forbidden
     * {
     * "error": "Access denied for non admin user"
     * }
     * @apiSuccessExample {json} Success-Response:
     * {
     *  "userLevels":[
     *  "BasicUser",
     *  "PublishingUser",
     *  "Administrator"
     *  ]
     * }
     */
    @GetMapping("/levels")
    public Collection<String> getUserLevels() {
        return null;
    }
}
