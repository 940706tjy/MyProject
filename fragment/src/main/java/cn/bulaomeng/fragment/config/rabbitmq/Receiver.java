package cn.bulaomeng.fragment.config.rabbitmq;


public interface Receiver {

    public void receiveMessage(Object message) throws Exception;

}