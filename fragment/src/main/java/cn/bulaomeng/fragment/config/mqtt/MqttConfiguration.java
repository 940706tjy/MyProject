/*
package cn.bulaomeng.fragment.config.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;


*/
/**
 * mqtt 配置类
 *
 * @author tjy
 * @date 2020-03-10 08:41
 **//*

//@Configuration
@IntegrationComponentScan
@Slf4j
@EnableConfigurationProperties(value = MqttSenderConfig.class)
public class MqttConfiguration {
	private static final byte[] WILL_DATA;

	*/
/**
	 * 订阅的bean名称
	 *//*

	public static final String CHANNEL_NAME_IN = "mqttInboundChannel";
	*/
/**
	 * 发布的bean名称
	 *//*

	public static final String CHANNEL_NAME_OUT = "mqttOutboundChannel";


	@Autowired
	private MqttSenderConfig mqttProperties;

	static {
		WILL_DATA = "offline".getBytes();
	}
	*/
/**
	 * MQTT连接器选项()
	 *
	 *//*

*/
/*	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setPassword(mqttProperties.getPassword());
		factory.setUserName(mqttProperties.getUsername());
		// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
		// 这里设置为true表示每次连接到服务器都以新的身份连接
		factory.setCleanSession(false);
		factory.setServerURIs(mqttProperties.getUrl());
		// 设置超时时间 单位为秒
		factory.setConnectionTimeout(10);
		// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线，但这个方法并没有重连的机制
		factory.setKeepAliveInterval(20);
		// 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
		factory.setWill(new DefaultMqttPahoClientFactory.Will("willTopic",WILL_DATA,1,false));
		return factory;
	}*//*


	*/
/**
	 * MQTT连接器选项()
	 *
	 * @return {@link MqttConnectOptions}
	 *//*

	@Bean
	public MqttConnectOptions getMqttConnectOptions() {
		MqttConnectOptions options = new MqttConnectOptions();
		// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
		// 这里设置为true表示每次连接到服务器都以新的身份连接
		options.setCleanSession(true);
		// 设置连接的用户名
		options.setUserName(mqttProperties.getUsername());
		// 设置连接的密码
		options.setPassword(mqttProperties.getPassword().toCharArray());
		options.setServerURIs(new String[]{mqttProperties.getUrl()});
		// 设置超时时间 单位为秒
		options.setConnectionTimeout(10);
		// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线，但这个方法并没有重连的机制
		options.setKeepAliveInterval(20);
		// 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
		options.setWill("willTopic", WILL_DATA, 2, false);
		return options;
	}


	*/
/**
	 * MQTT客户端（这里有版本差异 1.5.8中没有对应的setConnectionOptions方法）
	 *
	 * @return {@link MqttPahoClientFactory}
	 *//*

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(getMqttConnectOptions());
		return factory;
	}




	*/
/**
	 * MQTT信息通道（生产者）
	 *
	 * @return {@link MessageChannel}
	 *//*

	@Bean(name = CHANNEL_NAME_OUT)
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	*/
/**
	 * MQTT消息处理器（生产者）
	 *
	 * @return {@link MessageHandler}
	 *//*

	@Bean
	@ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
			mqttProperties.getProducer().getClientId(),
			mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(mqttProperties.getProducer().getDefaultTopic());
		return messageHandler;
	}

	*/
/**
	 * MQTT消息订阅绑定（消费者）
	 *
	 * @return {@link org.springframework.integration.core.MessageProducer}
	 *//*

	@Bean
	public MessageProducer inbound() {
		// 可以同时消费（订阅）多个Topic
		MqttPahoMessageDrivenChannelAdapter adapter =
			new MqttPahoMessageDrivenChannelAdapter(
				mqttProperties.getConsumer().getClientId(), mqttClientFactory(),
					StringUtils.split(mqttProperties.getConsumer().getDefaultTopic(),","));
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		// 设置订阅通道
		adapter.setOutputChannel(mqttInboundChannel());
		return adapter;
	}

	*/
/**
	 * MQTT信息通道（消费者）
	 *
	 * @return {@link MessageChannel}
	 *//*

	@Bean(name = CHANNEL_NAME_IN)
	public MessageChannel mqttInboundChannel() {
		return new DirectChannel();
	}

}

*/
