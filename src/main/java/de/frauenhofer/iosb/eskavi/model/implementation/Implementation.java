package de.frauenhofer.iosb.eskavi.model.implementation;

import de.frauenhofer.iosb.eskavi.model.user.User;

/**
 * @author Niv Adam
 * @version 1.0.0
 */
public abstract class Implementation implements ImmutableImplementation {
    public void subscribe(User user) {
        return;
    }

    public void unsubscribe(User user) {
        return;
    }
}
