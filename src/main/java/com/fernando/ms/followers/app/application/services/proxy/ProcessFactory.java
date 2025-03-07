package com.fernando.ms.followers.app.application.services.proxy;

import com.fernando.ms.followers.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.followers.app.application.ports.output.FollowerPersistencePort;

public class ProcessFactory {

    private ProcessFactory(){

    }

    public static IProcess validSaveFollower(FollowerPersistencePort followerPersistencePort,ExternalUserOutputPort externalUserOutputPort){
        return new RuleSaveFollower( followerPersistencePort, externalUserOutputPort);
    }
}
