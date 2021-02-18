package eskavi.controller.responses.imp;

import eskavi.model.configuration.ConfigurationType;

import java.util.Collection;

public class ConfigurationTemplatesResponse {
    private Collection<ConfigurationType> templates;

    public Collection<ConfigurationType> getTemplates() {
        return templates;
    }

    public void setTemplates(Collection<ConfigurationType> templates) {
        this.templates = templates;
    }

    public ConfigurationTemplatesResponse(Collection<ConfigurationType> templates) {
        this.templates = templates;
    }
}
