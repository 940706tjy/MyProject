package cn.bulaomeng.fragment.config.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitmqParam {

    // ip
    private String host;

    // 端口号
    private int port;

    // json simple
    private String messageType;

    // 队列是否可重复使用
    private boolean queueDurable;

    // 队列是否独占
    private boolean queueExclusive;

    // 队列是否自动删除
    private boolean queueAutoDelete;

    // 交换机是否持久使用
    private boolean exchangeDurable;

    // 交换机是否自动删除
    private boolean exchangeAutoDelete;

    // 交换机类型 direct fanout topic headers
    private String exchangeType;

    //
    private boolean automaticRecovery;

    private int channelRpcTimeout;

    private boolean channelShouldCheckRpcResponseType;

    // 超时时间
    private int timeout;

    private int shakeTimeout;

    private int networkRecoveryInterval;

    private long networkRecoveryIntervalLong;

    // 密码
    private String password;

    private int requestedChannelMax;

    private int requestedFrameMax;

    private int requestedHeartbeat;

    private int shutdownTimeout;

    private String uriString;

    // 用户名
    private String username;

    private String virtualHost;

    // 队列名称
    private String queueName;

    // 交换机名
    private String exchange;

    // 路由key
    private String routingKey;

    // 预取数
    private int preFetchCount;

    public RabbitmqParam() {
    }

    public int getPreFetchCount() {
        return preFetchCount;
    }

    public void setPreFetchCount(int preFetchCount) {
        this.preFetchCount = preFetchCount;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public boolean isQueueDurable() {
        return queueDurable;
    }

    public void setQueueDurable(boolean queueDurable) {
        this.queueDurable = queueDurable;
    }

    public boolean isQueueExclusive() {
        return queueExclusive;
    }

    public void setQueueExclusive(boolean queueExclusive) {
        this.queueExclusive = queueExclusive;
    }

    public boolean isQueueAutoDelete() {
        return queueAutoDelete;
    }

    public void setQueueAutoDelete(boolean queueAutoDelete) {
        this.queueAutoDelete = queueAutoDelete;
    }

    public boolean isExchangeDurable() {
        return exchangeDurable;
    }

    public void setExchangeDurable(boolean exchangeDurable) {
        this.exchangeDurable = exchangeDurable;
    }

    public boolean isExchangeAutoDelete() {
        return exchangeAutoDelete;
    }

    public void setExchangeAutoDelete(boolean exchangeAutoDelete) {
        this.exchangeAutoDelete = exchangeAutoDelete;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isAutomaticRecovery() {
        return automaticRecovery;
    }

    public void setAutomaticRecovery(boolean automaticRecovery) {
        this.automaticRecovery = automaticRecovery;
    }

    public int getChannelRpcTimeout() {
        return channelRpcTimeout;
    }

    public void setChannelRpcTimeout(int channelRpcTimeout) {
        this.channelRpcTimeout = channelRpcTimeout;
    }

    public boolean isChannelShouldCheckRpcResponseType() {
        return channelShouldCheckRpcResponseType;
    }

    public void setChannelShouldCheckRpcResponseType(boolean channelShouldCheckRpcResponseType) {
        this.channelShouldCheckRpcResponseType = channelShouldCheckRpcResponseType;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getShakeTimeout() {
        return shakeTimeout;
    }

    public void setShakeTimeout(int shakeTimeout) {
        this.shakeTimeout = shakeTimeout;
    }

    public int getNetworkRecoveryInterval() {
        return networkRecoveryInterval;
    }

    public void setNetworkRecoveryInterval(int networkRecoveryInterval) {
        this.networkRecoveryInterval = networkRecoveryInterval;
    }

    public long getNetworkRecoveryIntervalLong() {
        return networkRecoveryIntervalLong;
    }

    public void setNetworkRecoveryIntervalLong(long networkRecoveryIntervalLong) {
        this.networkRecoveryIntervalLong = networkRecoveryIntervalLong;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRequestedChannelMax() {
        return requestedChannelMax;
    }

    public void setRequestedChannelMax(int requestedChannelMax) {
        this.requestedChannelMax = requestedChannelMax;
    }

    public int getRequestedFrameMax() {
        return requestedFrameMax;
    }

    public void setRequestedFrameMax(int requestedFrameMax) {
        this.requestedFrameMax = requestedFrameMax;
    }

    public int getRequestedHeartbeat() {
        return requestedHeartbeat;
    }

    public void setRequestedHeartbeat(int requestedHeartbeat) {
        this.requestedHeartbeat = requestedHeartbeat;
    }

    public int getShutdownTimeout() {
        return shutdownTimeout;
    }

    public void setShutdownTimeout(int shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

    public String getUriString() {
        return uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
