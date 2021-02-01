package eskavi.controller;

import eskavi.model.configuration.Configuration;
import eskavi.model.user.ImmutableUser;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("aas")
public class AASConfigurationController {
    /**
     * @api{post}/aas Post AAS session
     * @apiName CreateSession
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiError {String} message Errormessage
     */
    @PostMapping
    public long createSession() {
        return 0;
    }

    /**
     * @api{get}/aas Close AAS session
     * @apiName CloseSession
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiSuccess {Number} id Session unique ID
     * @apiError {String} message Errormessage
     */
    @GetMapping("/{id:[0-9]+}")
    public void closeSession(@PathVariable("id") long sessionId) {
    }

    /**
     * @api{post}/aas/:id/imp/:impId Add ModuleImplementation to Session
     * @apiName AddModleImp
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam {Number} id Session unique ID
     * @apiParam {Number} impId Implementation unique ID
     * @apiError {String} message Errormessage
     */
    @PostMapping("{/id:[0-9]+}/imp/{impId:[0-9]+}")
    public void addModuleImp(@PathVariable("impId") long moduleId, @PathVariable("id") long sessionId) {
    }

    /**
     * @api{get}/aas/:id/imp/:impId/configuration Get Configuration from ModuleImplementation in Session
     * @apiName GetConfiguration
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam {Number} id Session unique ID
     * @apiParam {Number} impId Implementation unique ID
     * @apiError {String} message Errormessage
     */
    @GetMapping("{/id:[0-9]+}/imp/{impId:[0-9]+}/configuration")
    public Configuration getConfiguration(@PathVariable("impId") long moduleId, @PathVariable("id") long sessionId) {
        return null;
    }

    /**
     * @api{put}/aas/:id/imp/:impId/configuration Update Configuration from ModuleImplementation in Session
     * @apiName PutConfiguration
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam {Number} id Session unique ID
     * @apiParam {Number} impId Implementation unique ID
     * @apiError {String} message Errormessage
     */
    @PutMapping("{/id:[0-9]+}/imp/{impId:[0-9]+}/configuration")
    public void updateConfiguration(Configuration configuration, @PathVariable("impId") long moduleId, @PathVariable("id") long sessionId) {
    }

    /**
     * @api{delete}/aas/:id/imp/:impId
     * @apiName DeleteModleImp
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam {Number} id Session unique ID
     * @apiParam {Number} impId Implementation unique ID
     * @apiError {String} message Errormessage
     */
    @DeleteMapping("{/id:[0-9]+}/imp/{impId:[0-9]+}")
    public void deleteModuleImp(@PathVariable("impId") long moduleId, @PathVariable("id") long sessionId) {
    }

    /**
     * @api{get}/aas/:id/generate Generates a .java file which starts the AAS
     * @apiName GenerateJavaClass
     * @apiGroup AAS
     * @apiVersion 0.0.1
     * @apiHeader {String} Authorization Authorization header using the Bearer schema: Bearer token
     * @apiParam {Number} id Session unique ID
     * @apiError {String} message Errormessage
     */
    @GetMapping("{/id:[0-9]+}/generate")
    public File generateJavaClass(@PathVariable("id") long sessionId) {
        return null;
    }

    private boolean isAuthorized(long sessionId, ImmutableUser user) {
        return false;
    }
}
