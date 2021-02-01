package eskavi.controller;

import eskavi.model.implementation.ImmutableImplementation;
import eskavi.model.user.ImmutableUser;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("imp")
public class ImpController {
    @GetMapping("/user/{userId:[0-9]+}")
    public Collection<ImmutableUser> getAllByUser(@PathVariable long userId) {
        return null;
    }

    @GetMapping("/{id:[0-9]+}")
    public ImmutableImplementation get(@PathVariable("id") long impId) {
        return null;
    }

    // TODO Wait for ImpType
    // public ImmutableImplementation getDefaultImpCreate(ImpType type){}

    @GetMapping("/template/{id:[0-9]+}")
    public ImmutableImplementation getTemplateImpCreate(@PathVariable("id") long impId) {
        return null;
    }

    @PostMapping
    public void add(ImmutableImplementation mi) {
    }

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