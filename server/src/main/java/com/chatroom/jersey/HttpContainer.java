package com.chatroom.jersey;

import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;

import javax.ws.rs.core.Application;

public class HttpContainer implements Container {

    private volatile ApplicationHandler appHandler;

    public HttpContainer(Application application) {
        this.appHandler = new ApplicationHandler(application);
        this.appHandler.onStartup(this);
    }

    @Override
    public ResourceConfig getConfiguration() {
        return appHandler.getConfiguration();
    }

    @Override
    public ApplicationHandler getApplicationHandler() {
        return appHandler;
    }

    @Override
    public void reload() {
        reload(appHandler.getConfiguration());
    }

    @Override
    public void reload(ResourceConfig configuration) {
        appHandler.onShutdown(this);

        appHandler = new ApplicationHandler(configuration);
        appHandler.onReload(this);
        appHandler.onStartup(this);
    }

//    /**
//     * Get {@link java.util.concurrent.ExecutorService}.
//     *
//     * @return Executor service associated with this container.
//     */
//    ExecutorService getExecutorService() {
//        return appHandler.getInjectionManager().getInstance(ExecutorServiceProvider.class).getExecutorService();
//    }
//
//    /**
//     * Get {@link ScheduledExecutorService}.
//     *
//     * @return Scheduled executor service associated with this container.
//     */
//    ScheduledExecutorService getScheduledExecutorService() {
//        return appHandler.getInjectionManager().getInstance(ScheduledExecutorServiceProvider.class).getExecutorService();
//    }
}
