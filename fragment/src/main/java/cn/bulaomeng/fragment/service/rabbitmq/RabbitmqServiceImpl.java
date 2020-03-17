package cn.bulaomeng.fragment.service.rabbitmq;

import cn.bulaomeng.fragment.config.rabbitmq.RabbitmqParam;
import cn.bulaomeng.fragment.config.rabbitmq.Receiver;
import cn.bulaomeng.fragment.service.rabbitmq.RabbitmqService;
import com.google.common.base.Strings;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.stereotype.Service;

@SuppressWarnings("all")
@Service
public class RabbitmqServiceImpl implements RabbitmqService {

	/**
	 * 声明开启一个 Queue 和 Exchange
	 *
	 * @param param
	 */
	@Override
	public boolean publishTopic(RabbitmqParam param) {
		try {
			CachingConnectionFactory factory = this.getFactory(param);
			RabbitAdmin admin = new RabbitAdmin(factory);
			Binding binding;
			switch (param.getExchangeType()) {
            case "fanout":
                FanoutExchange fanoutExchange = new FanoutExchange(
                        param.getExchange());
                admin.declareExchange(fanoutExchange);
				// 申明一个消息队列
				if (!Strings.isNullOrEmpty(param.getQueueName())) {
					Queue queue = new Queue(param.getQueueName(), param.isQueueDurable(),
							param.isQueueExclusive(), param.isQueueAutoDelete());
					admin.declareQueue(queue);
					binding = BindingBuilder.bind(queue).to(fanoutExchange);
					admin.declareBinding(binding);
				}
                break;
            case "topic":
                // 申明exchange 对象
                TopicExchange topicExchange = new TopicExchange(param.getExchange());
                admin.declareExchange(topicExchange);
				// 申明一个消息队列
				if (!Strings.isNullOrEmpty(param.getQueueName())) {
					Queue queue = new Queue(param.getQueueName(), param.isQueueDurable(),
							param.isQueueExclusive(), param.isQueueAutoDelete());
					admin.declareQueue(queue);
					binding = BindingBuilder.bind(queue).to(topicExchange)
							.with(param.getRoutingKey());
					admin.declareBinding(binding);
				}

                break;
            case "direct":
                DirectExchange directExchange = new DirectExchange(
                        param.getExchange());
                admin.declareExchange(directExchange);
				// 申明一个消息队列
				if (!Strings.isNullOrEmpty(param.getQueueName())) {
					Queue queue = new Queue(param.getQueueName(), param.isQueueDurable(),
							param.isQueueExclusive(), param.isQueueAutoDelete());
					admin.declareQueue(queue);
					binding = BindingBuilder.bind(queue).to(directExchange)
							.with(param.getRoutingKey());
					admin.declareBinding(binding);
				}
                break;
            default: // 默认为topic
                TopicExchange defaultExchange = new TopicExchange(
                        param.getExchange());
                admin.declareExchange(defaultExchange);
				// 申明一个消息队列
				if (!Strings.isNullOrEmpty(param.getQueueName())) {
					Queue queue = new Queue(param.getQueueName(), param.isQueueDurable(),
							param.isQueueExclusive(), param.isQueueAutoDelete());
					admin.declareQueue(queue);
					binding = BindingBuilder.bind(queue).to(defaultExchange)
							.with(param.getRoutingKey());
					admin.declareBinding(binding);
				}

                break;
            }
			factory.destroy();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 订阅
	 *
	 * @param param
	 */
	@Override
	public SimpleMessageListenerContainer subscribeTopic(RabbitmqParam param, Receiver receiver) {
		CachingConnectionFactory factory = this.getFactory(param);
		RabbitAdmin admin = new RabbitAdmin(factory);
		Binding binding;
		// 申明一个消息队列
		Queue queue = new Queue(param.getQueueName(), param.isQueueDurable(),
				param.isQueueExclusive(), param.isQueueAutoDelete());
		switch (param.getExchangeType()) {
		case "fanout":
			FanoutExchange fanoutExchange = new FanoutExchange(
					param.getExchange(), param.isExchangeDurable(),
					param.isExchangeAutoDelete());
			binding = BindingBuilder.bind(queue).to(fanoutExchange);
			break;
		case "topic":
			// 申明exchange 对象
			TopicExchange topicExchange = new TopicExchange(
					param.getExchange(), param.isExchangeDurable(),
					param.isExchangeAutoDelete());
			binding = BindingBuilder.bind(queue).to(topicExchange)
					.with(param.getRoutingKey());
			break;
		case "direct":
			DirectExchange directExchange = new DirectExchange(
					param.getExchange());
			binding = BindingBuilder.bind(queue).to(directExchange)
					.with(param.getRoutingKey());
			break;
		default: // 默认为topic
			TopicExchange defaultExchange = new TopicExchange(
					param.getExchange(), param.isExchangeDurable(),
					param.isExchangeAutoDelete());
			admin.declareExchange(defaultExchange);
			binding = BindingBuilder.bind(queue).to(defaultExchange)
					.with(param.getRoutingKey());
			break;
		}
		admin.declareBinding(binding);
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(
				this.getFactory(param));
		MessageListenerAdapter adapter = new MessageListenerAdapter(receiver,
				"receiveMessage");
		container.setPrefetchCount(param.getPreFetchCount() == 0 ? 1 : param
				.getPreFetchCount());
		if (null != param.getMessageType()) {
			String messageType = param.getMessageType();
			switch (messageType) {
			case "json":
				Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
				adapter.setMessageConverter(messageConverter);
				break;
			case "simple":
				SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();
				adapter.setMessageConverter(simpleMessageConverter);
				break;
			default:
				SimpleMessageConverter contentTypeDelegatingMessageConverter = new SimpleMessageConverter();
				adapter.setMessageConverter(contentTypeDelegatingMessageConverter);
				break;
			}
		}
		container.setMessageListener(adapter);
		container.setRabbitAdmin(admin);
		container.setQueueNames(param.getQueueName());
		container.setReceiveTimeout(60 * 1000);
		container.start();
		return container;
	}

	/**
	 * 发布并订阅
	 *
	 * @param param
	 * @param receiver
	 */
	@Override
	public void pubAndSub(RabbitmqParam param, Receiver receiver) {
		CachingConnectionFactory factory = this.getFactory(param);
		RabbitAdmin admin = new RabbitAdmin(factory);
		// 申明一个消息队列
		Queue queue = new Queue(param.getQueueName(), param.isQueueDurable(),
				param.isQueueExclusive(), param.isQueueAutoDelete());
		admin.declareQueue(queue);
		Binding binding;
		switch (param.getExchangeType()) {
		case "fanout":
			FanoutExchange fanoutExchange = new FanoutExchange(
					param.getExchange());
			admin.declareExchange(fanoutExchange);
			binding = BindingBuilder.bind(queue).to(fanoutExchange);
			break;
		case "topic":
			// 申明exchange 对象
			TopicExchange topicExchange = new TopicExchange(param.getExchange());
			admin.declareExchange(topicExchange);
			binding = BindingBuilder.bind(queue).to(topicExchange)
					.with(param.getRoutingKey());
			break;
		case "direct":
			DirectExchange directExchange = new DirectExchange(
					param.getExchange());
			admin.declareExchange(directExchange);
			binding = BindingBuilder.bind(queue).to(directExchange)
					.with(param.getRoutingKey());
			break;
		default: // 默认为topic
			TopicExchange defaultExchange = new TopicExchange(
					param.getExchange());
			admin.declareExchange(defaultExchange);
			binding = BindingBuilder.bind(queue).to(defaultExchange)
					.with(param.getRoutingKey());
			break;
		}
		admin.declareBinding(binding);
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(
				this.getFactory(param));
		MessageListenerAdapter adapter = new MessageListenerAdapter(receiver,
				"receiveMessage");
		container.setMessageListener(adapter);
		container.setQueueNames(param.getQueueName());
		container.setRabbitAdmin(admin);
		container.start();
	}

	/**
	 * 发送消息
	 * 
	 * @param param
	 * @param message
	 */
	@Override
	public void sendMessage(RabbitmqParam param, String message) {
		CachingConnectionFactory factory = this.getFactory(param);
		RabbitTemplate template = new RabbitTemplate(factory);
		template.convertAndSend(param.getExchange(), param.getRoutingKey(),
				message);
		factory.destroy();
	}

	/**
	 * 获取消息发送者
	 *
	 * @param param
	 * @return
	 */
	@Override
	public RabbitTemplate getSender(RabbitmqParam param) {
		CachingConnectionFactory factory = this.getFactory(param);
		RabbitTemplate template = new RabbitTemplate(factory);
		return template;
	}

	/**
	 * 获取工厂
	 * 
	 * @param param
	 * @return
	 */
	private CachingConnectionFactory getFactory(RabbitmqParam param) {
		// 连接工厂
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		// 根据是否有值设置参数
		if (null != param && !Strings.isNullOrEmpty(param.getHost())) {
			connectionFactory.setHost(param.getHost());
		}
		if (null != param && 0 != param.getPort()) {
			connectionFactory.setPort(param.getPort());
		}
		if (null != param && !Strings.isNullOrEmpty(param.getUsername())) {
			connectionFactory.setUsername(param.getUsername());
		}
		if (null != param && !Strings.isNullOrEmpty(param.getPassword())) {
			connectionFactory.setPassword(param.getPassword());
		}
		if (null != param && !Strings.isNullOrEmpty(param.getVirtualHost())) {
			connectionFactory.setVirtualHost(param.getVirtualHost());
		} else {
			connectionFactory.setVirtualHost("/");
		}
		// 必须要设置,消息的回掉
		connectionFactory.setPublisherConfirms(true);
		return connectionFactory;
	}

}
