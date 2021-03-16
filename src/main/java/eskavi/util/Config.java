package eskavi.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "config")
public class Config {
    private int TOKEN_EXPIRES_AFTER_HOURS;
    private long ASSET_CONNECTION;
    private long DESERIALIZER;
    private long DISPATCHER;
    private long ENDPOINT;
    private long HANDLER;
    private long INTERACTION_STARTER;
    private long PERSISTENCE_MANAGER;
    private long SERIALIZER;
    private long PROTOCOL_TYPE;
    private long MESSAGE_TYPE;
    private long ENVIRONMENT;
    private String PUBLIC_USER_ID;

    public String getPUBLIC_USER_ID() {
        return PUBLIC_USER_ID;
    }

    public void setPUBLIC_USER_ID(String PUBLIC_USER_ID) {
        this.PUBLIC_USER_ID = PUBLIC_USER_ID;
    }

    public int getTOKEN_EXPIRES_AFTER_HOURS() {
        return TOKEN_EXPIRES_AFTER_HOURS;
    }

    public void setTOKEN_EXPIRES_AFTER_HOURS(int TOKEN_EXPIRES_AFTER_HOURS) {
        this.TOKEN_EXPIRES_AFTER_HOURS = TOKEN_EXPIRES_AFTER_HOURS;
    }

    public long getASSET_CONNECTION() {
        return ASSET_CONNECTION;
    }

    public void setASSET_CONNECTION(long ASSET_CONNECTION) {
        this.ASSET_CONNECTION = ASSET_CONNECTION;
    }

    public long getDESERIALIZER() {
        return DESERIALIZER;
    }

    public void setDESERIALIZER(long DESERIALIZER) {
        this.DESERIALIZER = DESERIALIZER;
    }

    public long getDISPATCHER() {
        return DISPATCHER;
    }

    public void setDISPATCHER(long DISPATCHER) {
        this.DISPATCHER = DISPATCHER;
    }

    public long getENDPOINT() {
        return ENDPOINT;
    }

    public void setENDPOINT(long ENDPOINT) {
        this.ENDPOINT = ENDPOINT;
    }

    public long getHANDLER() {
        return HANDLER;
    }

    public void setHANDLER(long HANDLER) {
        this.HANDLER = HANDLER;
    }

    public long getINTERACTION_STARTER() {
        return INTERACTION_STARTER;
    }

    public void setINTERACTION_STARTER(long INTERACTION_STARTER) {
        this.INTERACTION_STARTER = INTERACTION_STARTER;
    }

    public long getPERSISTENCE_MANAGER() {
        return PERSISTENCE_MANAGER;
    }

    public void setPERSISTENCE_MANAGER(long PERSISTENCE_MANAGER) {
        this.PERSISTENCE_MANAGER = PERSISTENCE_MANAGER;
    }

    public long getSERIALIZER() {
        return SERIALIZER;
    }

    public void setSERIALIZER(long SERIALIZER) {
        this.SERIALIZER = SERIALIZER;
    }

    public long getPROTOCOL_TYPE() {
        return PROTOCOL_TYPE;
    }

    public void setPROTOCOL_TYPE(long PROTOCOL_TYPE) {
        this.PROTOCOL_TYPE = PROTOCOL_TYPE;
    }

    public long getMESSAGE_TYPE() {
        return MESSAGE_TYPE;
    }

    public void setMESSAGE_TYPE(long MESSAGE_TYPE) {
        this.MESSAGE_TYPE = MESSAGE_TYPE;
    }

    public long getENVIRONMENT() {
        return ENVIRONMENT;
    }

    public void setENVIRONMENT(long ENVIRONMENT) {
        this.ENVIRONMENT = ENVIRONMENT;
    }
}
