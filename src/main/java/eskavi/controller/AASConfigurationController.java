package eskavi.controller;

import eskavi.model.configuration.Configuration;
import eskavi.model.user.ImmutableUser;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("aas")
public class AASConfigurationController {
    @PostMapping
    public long createSession() {
        return 0;
    }

    @GetMapping
    public void closeSession(long sessionId) {
    }


    @PostMapping("{id:[0-9]+}/imp/{impId:[0-9]+}")
    public void addModuleImp(@PathVariable("impId") long moduleId, @PathVariable("id") long sessionId) {
    }

    @GetMapping("{id:[0-9]+}/imp/{impId:[0-9]+}/configuration")
    public Configuration getConfiguration(@PathVariable("impId") long moduleId, @PathVariable("id") long sessionId) {
        return null;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateConfiguration(Configuration configuration, long sessionId, long moduleId) {
    }

    @DeleteMapping("{id:[0-9]+}/imp/{impId:[0-9]+}")
    public void deleteModuleImp(@PathVariable("impId") long moduleId, @PathVariable("id") long sessionId) {
    }

    @GetMapping("{id:[0-9]+}/generate")
    public File generateJavaClass(@PathVariable("id") long sessionId) {
        return null;
    }

    private boolean isAuthorized(long sessionId, ImmutableUser user) {
        return false;
    }
}
