package cn.bulaomeng.fragment.entity;

public class User {
    private String userName; //姓名
    private String userNo;   //学工号
    private String checkCode; //解密后校验码
    private String linkCheckCode; //拼接处理后的校验码

    public String getLinkCheckCode() {
        return linkCheckCode;
    }

    public void setLinkCheckCode(String linkCheckCode) {
        this.linkCheckCode = linkCheckCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userNo='" + userNo + '\'' +
                ", checkCode='" + checkCode + '\'' +
                ", linkCheckCode='" + linkCheckCode + '\'' +
                '}';
    }
}
