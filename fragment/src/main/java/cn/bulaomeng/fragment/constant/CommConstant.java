package cn.bulaomeng.fragment.constant;

public class CommConstant {
    //系统时区
    public final static String TIMEZONE = "GMT+8";

    public final static Integer FAILED_FLAG = 601;
    public final static Integer SUCCESSED_FLAG = 600;
    public final static String SUCCESSED_MSG = "成功";
    public final static String FAILED_MSG = "失败";

    public final static Integer NUMBER_ONE = 1;
    public final static Integer NUMBER_TOW = 2;
    public final static Integer NUMBER_THREE = 3;

    //模板存放位置

    public final static String TEMPLATE_PATH = "download/";

    public final static String REDIS_IMPORT_FAIL = "IMPORT:FAIL";

    public final static String RABBITMQ_PROJECT = "fragment";

    public final static String MQ_CLIENT_MSG_TYPE = "msgType";
    public final static String MQ_CLIENT_DATA_INFO = "data";

    public final static String IDEMPOTENT_TOKEN_PREFIX = "idempotent:";

    public final static String IDEMPOTENT_TOKEN_PREFIX_HEAD = "idempotentHead";

    public final static String IDEMPOTENT_TOKEN_PARAM = "token";

}
