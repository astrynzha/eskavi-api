package eskavi.controller;

import eskavi.controller.requests.user.*;
import eskavi.model.user.ImmutableUser;
import eskavi.model.user.User;
import eskavi.model.user.UserLevel;
import eskavi.service.UserManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserManagementController {

    final UserTokenMatcher userTokenMatcher;

    final UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService, UserTokenMatcher userTokenMatcher) {
        this.userManagementService = userManagementService;
        this.userTokenMatcher = userTokenMatcher;
    }

    /**
     * @api{post}/user/register Register a new User
     * @apiName Register
     * @apiGroup User
     * @apiVersion 0.0.1
     * @apiSuccess {String} jwt Token to authenticate future requests
     * @apiParam (Request body) {String} email User mail
     * @apiParam (Request body) {String} password User password
     * @apiParamExample {json} Request-Example:
     * {
     * "email": "test@web.de",
     * "password": "12345678"
     * }
     */
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        userManagementService.createUser(request.getEmail(), new BCryptPasswordEncoder().encode(request.getEmail()));
        return userTokenMatcher.generateToken(request.getEmail());
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
    public String login(@RequestBody LoginRequest request) {
        ImmutableUser user = userManagementService.getUser(request.getEmail());
        if (new BCryptPasswordEncoder().encode(request.getPassword()).equals(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "wrong Password");
        }
        return userTokenMatcher.generateToken(request.getEmail());
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
     * "user":{
     * "email":"test@web.de",
     * "password":"$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8",
     * "securityQuestion":"petName",
     * "securityAnswer":"Jim"
     * "userLevel":"BasicUser"
     * }
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
        ImmutableUser user = userTokenMatcher.getUser(jwtToken);
        return userManagementService.getUser(user.getEmailAddress());
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
        User user = (User) userManagementService.getUser(jwtToken);
        userManagementService.deleteUser(user.getEmailAddress());
    }

    /**
     * @api{get}/user/security_question Get security question
     * @apiName GetSecurityQuestion
     * @apiGroup User
     * @apiVersion 0.0.1
     * @apiParam (Request - Body) email User mail
     * @apiSuccess {String} securityQuestion Security question to reset the password
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "error": "Unauthorized please login to your account"
     * }
     */
    @GetMapping("/security_question")
    public String getSecurityQuestion(@RequestBody String email) {
        ImmutableUser user = userManagementService.getUser(email);
        return userManagementService.getSecurityQuestion(user.getEmailAddress()).getQuestion();
    }

    /**
     * @api{post}/user/reset_password Resets the password after security question was answered
     * @apiName ResetPasswordQuestion
     * @apiGroup User
     * @apiVersion 0.0.1
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
    public void resetPassword(@RequestHeader String jwtToken, @RequestBody ResetPasswordRequest request) throws IllegalAccessException {
        ImmutableUser user = userTokenMatcher.getUser(jwtToken);
        if (userManagementService.checkSecurityQuestion(user.getEmailAddress(), request.getAnswer())) {
            userManagementService.setPassword(user.getEmailAddress(), new BCryptPasswordEncoder().encode(request.getNewPassword()));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @api{post}/user/change_password Set a new password
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
    @PostMapping("/change_password")
    public void setPassword(@RequestHeader String jwtToken, @RequestBody SetPasswordRequest request) throws IllegalAccessException {
        ImmutableUser user = userTokenMatcher.getUser(jwtToken);
        if (userManagementService.checkPassword(user.getEmailAddress(), new BCryptPasswordEncoder().encode(request.getOldPassword()))) {
            userManagementService.setPassword(user.getEmailAddress(), new BCryptPasswordEncoder().encode(request.getNewPassword()));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @api{put}/user/level Set user level
     * @apiName SetUserLevel
     * @apiGroup User
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam (Request Body) email User mail
     * @apiParam (Request Body) userLevel new user level
     * @apiError {String} message Errormessage
     * @apiErrorExample {json} Error-Response:
     * HTTP/1.1 403 Forbidden
     * {
     * "error": "Access denied for non admin user"
     * }
     */
    @PostMapping("/level")
    public void setUserLevel(@RequestHeader String jwtToken, @RequestBody SetUserLevelRequest request) throws IllegalAccessException {
        ImmutableUser user = userTokenMatcher.getUser(jwtToken);
        userManagementService.setUserLevel(user.getEmailAddress(), request.getUserLevel(), request.getEmail());
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
     * "userLevels":[
     * "BasicUser",
     * "PublishingUser",
     * "Administrator"
     * ]
     * }
     */
    @GetMapping("/levels")
    public Collection<UserLevel> getUserLevels() {
        EnumSet<UserLevel> enumValues = EnumSet.allOf(UserLevel.class);
        List<UserLevel> userLevels = enumValues.stream().collect(Collectors.toList());
        return userLevels;
    }
}
