package eskavi.service.aasconfigurationservice;


import eskavi.model.user.User;

import java.util.Collection;
import java.util.HashSet;

public class AASSessionHandler {
    private Collection<AASConstructionSession> sessions;

    public AASSessionHandler() {
        this.sessions = new HashSet<>();
    }

}
