package com.chatroom.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "h2.server")
public class H2ServerProperties {
    private boolean enabled;
    private final Server tcp = new Server("-tcp");
    private final Server web = new Server("-web");

    @Getter
    @Setter
    public static class Server {
        private boolean enabled;
        private boolean allowOthers;
        private boolean daemon;
        private boolean ssl;
        private Integer port;
        private final String prefix;

        public Server(String prefix) {
            this.prefix = prefix;
        }

        public String[] getArgs() {
            List<String> argList = new ArrayList<String>();
            if (enabled) {
                argList.add(this.prefix);
            }
            if (allowOthers) {
                argList.add(this.prefix + "AllowOthers");
            }
            if (daemon) {
                argList.add(this.prefix + "Daemon");
            }
            if (ssl) {
                argList.add(this.prefix + "SSL");
            }
            if (port != null) {
                argList.add(this.prefix + "Port");
                argList.add(String.valueOf(port));
            }
            return argList.toArray(new String[0]);
        }
    }
}
