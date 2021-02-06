package eskavi.service.aasconfigurationservice;


import eskavi.model.user.User;

import java.util.HashMap;
import java.util.Map;

public class AASSessionHandler {
    private Map<Long, AASConstructionSession> sessionMap;
    private long nextFreeId;

    public AASSessionHandler() {
        this.sessionMap = new HashMap<>();
        this.nextFreeId = 0;
    }

    public long createAASConstructionSession(User user) {
        long id = nextFreeId++;
        sessionMap.put(id, new AASConstructionSession(id, user));
        return id;
    }

    public AASConstructionSession getAASConstructionSession(long sessionId) throws IllegalAccessException {
        if (!sessionMap.containsKey(sessionId)) {
            throw new IllegalAccessException("no session with id " + sessionId + " is found");
        }
        return sessionMap.get(sessionId);
    }

    public void deleteAASConstructionSession(long sessionId) {
        sessionMap.remove(sessionId);
    }
}
