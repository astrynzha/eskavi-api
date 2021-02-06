package eskavi.service.aasconfigurationservice;

import org.springframework.stereotype.Service;

@Service
public class AASConfigurationService {
    final ModuleInstanceFactory miFactory;
    final AASSessionHandler aasSessionHandler;

    //TODO Autowire
    public AASConfigurationService(ModuleInstanceFactory miFactory,AASSessionHandler aasSessionHandler) {
        this.miFactory = miFactory;
        this.aasSessionHandler = aasSessionHandler;
    }
}
