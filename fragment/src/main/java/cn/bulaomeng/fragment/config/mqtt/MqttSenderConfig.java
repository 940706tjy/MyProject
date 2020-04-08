package cn.bulaomeng.fragment.config.mqtt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 */
@ConfigurationProperties(prefix = "mqtt")
@Getter
@Setter
public class MqttSenderConfig {

    private String username;

    private String password;

    private String url;

    private Producer producer = new Producer();

    private Consumer consumer = new Consumer();

    @Data
    public class Producer {
        private String clientId;
        private String defaultTopic;
    }

    @Data
    public class Consumer {
        private String clientId;
        private String defaultTopic;
    }
}