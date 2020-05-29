package cn.bulaomeng.fragment.config.rabbitmq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @Description: rabbitmq java client的ReturnCallback及ConfirmCallback机制
* @Author: tjy
* @Date: 2020/4/1
*/
@Configuration
@ConditionalOnProperty(name = "spring.rabbitmq.publisher-confirms", matchIfMissing = true, havingValue = "true")
@Slf4j
public class RabbitMQCallBackConfig {


    /**
     * @deprecated ConfirmCallback：每一条发到rabbitmq server的消息都会调一次confirm方法。
     *              如果消息成功到达exchange，则ack参数为true，反之为false；
     *              cause参数是错误信息；
     *              CorrelationData可以理解为context，在发送消息时传入的这个参数，此时会拿到。
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate confirmCallback(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * @param correlationData 唯一标识，有了这个唯一标识，我们就知道可以确认（失败）哪一条消息了
             * @param ack
             * @param cause
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("=====消息进行消费了======");
                if(ack){
                    System.out.println("消息id为: "+correlationData+"的消息，已经被ack成功");
                }else{
                    System.out.println("消息id为: "+correlationData+"的消息，消息nack，失败原因是："+cause);
                }
            }
        });
        return rabbitTemplate;
    }

   /* @Bean
    @ConditionalOnMissingBean(value = RabbitTemplate.ReturnCallback.class)
    public RabbitTemplate.ReturnCallback returnCallback() {
        return new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                //
                log.info("Message [{}] replyCode [{}] replyText[{}] exchange [{}] routingKey[{}] "
                        ,message,replyCode,replyText,exchange,routingKey);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(value = RabbitTemplate.ConfirmCallback.class)
    public RabbitTemplate.ConfirmCallback confirmCallback() {
        return new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                //
                log.info("Message [{}] replyCode [{}] replyText[{}] exchange [{}] routingKey[{}] "
                        ,correlationData,ack,cause);
            }
        };
    }*/
}
