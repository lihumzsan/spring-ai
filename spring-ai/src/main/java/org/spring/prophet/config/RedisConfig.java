package org.spring.prophet.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(RedisConfig.class)
@ConfigurationProperties(prefix = "spring.data.redisson")
@Data
public class RedisConfig {

    private List<String> nodeAddresses;
    private String password;
    private int connectionPoolSize;
    private int connectionMinimumIdleSize;
    private int timeout;
    private int database;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        if ("prod".equals(activeProfile)) {
            config.useClusterServers()
                    .addNodeAddress(nodeAddresses.toArray(new String[0]))
                    .setTimeout(timeout)
                    .setRetryAttempts(3)
                    .setMasterConnectionPoolSize(connectionPoolSize)
                    .setMasterConnectionMinimumIdleSize(connectionMinimumIdleSize);
            return Redisson.create(config);
        }
        String host = nodeAddresses.getFirst();
        config.useSingleServer()
                .setAddress(host)
                .setPassword(password)
                .setTimeout(timeout)
                .setRetryAttempts(3)
                .setConnectionPoolSize(connectionPoolSize)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setPassword(password)
                .setDatabase(database);
        return Redisson.create(config);
    }
}
