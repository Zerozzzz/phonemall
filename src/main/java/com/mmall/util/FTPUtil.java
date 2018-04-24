package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;

/**
 * Created by Administrator on 2018/4/24.
 */
public class FTPUtil {

    public static String ftpIp = PropertiesUtil.getProperty("ftp.server.i");
    public static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    public static String ftpPasswd = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String passwd;
    private FTPClient ftpClient;

    public FTPUtil(String ip, int port, String user, String passwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.passwd = passwd;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }



}
