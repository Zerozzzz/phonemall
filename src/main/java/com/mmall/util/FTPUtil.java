package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/4/24.
 */
public class FTPUtil {

    public static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);
    public static String ftpIp = PropertiesUtil.getProperty("ftp.server.i");
    public static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    public static String ftpPasswd = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String passwd;
    private FTPClient ftpClient;

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp,21,ftpUser,ftpPasswd);
        logger.info("开始连接ftp服务器");
        boolean resualt = ftpUtil.uploadFile("img", fileList);
        logger.info("结束上传，上传结果为{}", resualt);
        return resualt;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean upload = true;
        FileInputStream fis = null;

        if (connectsServer(this.ip, this.port, this.user, this.passwd)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File fileItem : fileList
                     ) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }

            } catch (IOException e) {
                logger.error("上传文件异常",e);
                upload = false;
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return upload;
    }

    private boolean connectsServer(String ip, int port, String user, String passwd){
        boolean connectSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip,port);
            connectSuccess = ftpClient.login(user, passwd);

        } catch (IOException e) {
            logger.error("ftp服务器连接异常",e);
        }

        return connectSuccess;
    }



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
