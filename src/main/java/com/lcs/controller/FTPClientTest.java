package com.lcs.controller;
import java.io.*;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Log4j2
public class FTPClientTest {

    private static final Log logger = LogFactory.getLog(FTPClientTest.class);

    @Value("${ftp.username}")
    private String userName;         //FTP 登录用户名
    private String password;         //FTP 登录密码
    private String ip;                     //FTP 服务器地址IP地址
    private int port;                        //FTP 端口
    private Properties property = null;    //属性集
    private String configFile = "/application.yml";    //配置文件的路径名
    private FTPClient ftpClient = null; //FTP 客户端代理
    //时间格式化
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    //FTP状态码
    public int i = 1;

    /**
     * 连接到服务器
     *
     * @return true 连接服务器成功，false 连接服务器失败
     */
    public boolean connectServer() {
        boolean flag = true;
        if (ftpClient == null) {
            int reply;
            try {
                if (setArg(configFile)) {
                    ftpClient = new FTPClient();
                    ftpClient.setControlEncoding("GBK");
                    //ftpClient.configure(getFtpConfig());
                    ftpClient.connect(ip, port);
                    ftpClient.login(userName, password);
                    reply = ftpClient.getReplyCode();
                    ftpClient.setDataTimeout(120000);

                    if (!FTPReply.isPositiveCompletion(reply)) {
                        ftpClient.disconnect();
                        logger.debug("FTP 服务拒绝连接！");
                        flag = false;
                    }
                    i++;
                } else {
                    flag = false;
                }
            } catch (SocketException e) {
                flag = false;
                e.printStackTrace();
                logger.debug("登录ftp服务器 " + ip + " 失败,连接超时！");
            } catch (IOException e) {
                flag = false;
                e.printStackTrace();
                logger.debug("登录ftp服务器 " + ip + " 失败，FTP服务器无法打开！");
            }
        }
        return flag;
    }

    /**
     * 上传文件
     *
     * @param remoteFile 远程文件路径,支持多级目录嵌套
     * @param localFile  本地文件名称，绝对路径
     */
    public boolean uploadFile(String remoteFile, File localFile)
            throws IOException {
        boolean flag = false;
        InputStream in = new FileInputStream(localFile);
        String remote = new String(remoteFile.getBytes("GBK"), "iso-8859-1");
        if (ftpClient.storeFile(remote, in)) {
            flag = true;
            logger.debug(localFile.getAbsolutePath() + "上传文件成功！");
        } else {
            logger.debug(localFile.getAbsolutePath() + "上传文件失败！");
        }
        in.close();
        return flag;
    }

    /**
     * 上传单个文件，并重命名
     *
     * @param local--本地文件路径
     * @return true 上传成功，false 上传失败
     * @throws IOException
     */
    public boolean uploadFile(String local, String remote) throws IOException {
        boolean flag = true;
        String remoteFileName = remote;
        if (remote.contains("/")) {
            remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
            // 创建服务器远程目录结构，创建失败直接返回
            if (!CreateDirecroty(remote)) {
                return false;
            }
        }
        FTPFile[] files = ftpClient.listFiles(new String(remoteFileName));
        File f = new File(local);
        if (!uploadFile(remoteFileName, f)) {
            flag = false;
        }
        return flag;
    }

    /**
     * 上传文件夹内的所有文件
     *
     * @param filename   本地文件夹绝对路径
     * @param uploadpath 上传到FTP的路径,形式为/或/dir1/dir2/../
     * @return true 上传成功，false 上传失败
     * @throws IOException
     */
    public List uploadManyFile(String filename, String uploadpath) {
        boolean flag = true;
        List l = new ArrayList();
        StringBuffer strBuf = new StringBuffer();
        int n = 0; //上传失败的文件个数
        int m = 0; //上传成功的文件个数
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            ftpClient.changeWorkingDirectory("/");
            File file = new File(filename);
            File fileList[] = file.listFiles();
            for (File upfile : fileList) {
                if (upfile.isDirectory()) {
                    uploadManyFile(upfile.getAbsoluteFile().toString(), uploadpath);
                } else {
                    String local = upfile.getCanonicalPath().replaceAll("\\\\", "/");
                    String remote = uploadpath.replaceAll("\\\\", "/") + local.substring(local.indexOf("/") + 1);
                    flag = uploadFile(local, remote);
                    ftpClient.changeWorkingDirectory("/");
                }
                if (!flag) {
                    n++;
                    strBuf.append(upfile.getName() + ",");
                    logger.debug("文件［" + upfile.getName() + "］上传失败");
                } else {
                    m++;
                }
            }
            l.add(0, n);
            l.add(1, m);
            l.add(2, strBuf.toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
            logger.debug("本地文件上传失败！找不到上传文件！", e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("本地文件上传失败！", e);
        }
        return l;
    }

    /**
     * 下载文件
     *
     * @param remoteFileName       --服务器上的文件名
     * @param localFileName--本地文件名
     * @return true 下载成功，false 下载失败
     */
    public boolean loadFile(String remoteFileName, String localFileName) {
        boolean flag = true;
        // 下载文件
        BufferedOutputStream buffOut = null;
        try {
            buffOut = new BufferedOutputStream(new FileOutputStream(localFileName));
            flag = ftpClient.retrieveFile(remoteFileName, buffOut);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("本地文件下载失败！", e);
        } finally {
            try {
                if (buffOut != null)
                    buffOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 删除一个文件
     */
    public boolean deleteFile(String filename) {
        boolean flag = true;
        try {
            flag = ftpClient.deleteFile(filename);
            if (flag) {
                logger.debug("删除文件" + filename + "成功！");
            } else {
                logger.debug("删除文件" + filename + "成功！");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除目录
     */
    public void deleteDirectory(String pathname) {
        try {
            File file = new File(pathname);
            if (file.isDirectory()) {
                File file2[] = file.listFiles();
            } else {
                deleteFile(pathname);
            }
            ftpClient.removeDirectory(pathname);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 删除空目录
     */
    public void deleteEmptyDirectory(String pathname) {
        try {
            ftpClient.removeDirectory(pathname);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 列出服务器上文件和目录
     *
     * @param regStr --匹配的正则表达式
     */
    public void listRemoteFiles(String regStr) {
        try {
            String files[] = ftpClient.listNames(regStr);
            if (files == null || files.length == 0)
                logger.debug("没有任何文件!");
            else {
                for (int i = 0; i < files.length; i++) {
                    System.out.println(files[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 列出Ftp服务器上的所有文件和目录
     */
    public void listRemoteAllFiles() {
        try {
            String[] names = ftpClient.listNames();
            for (int i = 0; i < names.length; i++) {
                System.out.println(names[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void closeConnect() {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置传输文件的类型[文本文件或者二进制文件]
     *
     * @param fileType--BINARY_FILE_TYPE、ASCII_FILE_TYPE
     */
    public void setFileType(int fileType) {
        try {
            ftpClient.setFileType(fileType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置参数
     *
     * @param configFile --参数的配置文件
     */
    private boolean setArg(String configFile) {
        boolean flag = true;
        property = new Properties();
        BufferedInputStream inBuff = null;
        System.out.println(getClass().getResource("/").getPath());
        try {
            inBuff = new BufferedInputStream(new FileInputStream(getClass().getResource("/").getPath() + configFile));
            property.load(inBuff);
            userName = property.getProperty("ftp:username");
            password = property.getProperty("ftp:password");
            ip = property.getProperty("ftp:ip");
            port = Integer.parseInt(property.getProperty("ftp:port"));
            System.out.println(userName+password+ip+port);
        } catch (FileNotFoundException e1) {
            flag = false;
            logger.debug("配置文件 " + configFile + " 不存在！");
        } catch (IOException e) {
            flag = false;
            logger.debug("配置文件 " + configFile + " 无法读取！");
        }
        return flag;
    }


    /**
     * 进入到服务器的某个目录下
     *
     * @param directory
     */
    public boolean changeWorkingDirectory(String directory) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
            if (flag) {
                logger.debug("进入文件夹" + directory + " 成功！");

            } else {
                logger.debug("进入文件夹" + directory + " 失败！");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return flag;
    }

    /**
     * 返回到上一层目录
     */
    public void changeToParentDirectory() {
        try {
            ftpClient.changeToParentDirectory();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 重命名文件
     *
     * @param oldFileName --原文件名
     * @param newFileName --新文件名
     */
    public void renameFile(String oldFileName, String newFileName) {
        try {
            ftpClient.rename(oldFileName, newFileName);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 设置FTP客服端的配置--一般可以不设置
     *
     * @return ftpConfig
     */
    private FTPClientConfig getFtpConfig() {
        FTPClientConfig ftpConfig = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        ftpConfig.setServerLanguageCode(FTP.DEFAULT_CONTROL_ENCODING);
        return ftpConfig;
    }

    /**
     * 转码[ISO-8859-1 -> GBK] 不同的平台需要不同的转码
     *
     * @param obj
     * @return ""
     */
    private String iso8859togbk(Object obj) {
        try {
            if (obj == null)
                return "";
            else
                return new String(obj.toString().getBytes("iso-8859-1"), "GBK");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 在服务器上创建一个文件夹
     *
     * @param dir 文件夹名称，不能含有特殊字符，如 \ 、/ 、: 、* 、?、 "、 <、>...
     */
    public boolean makeDirectory(String dir) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(dir);
            if (flag) {
                logger.debug("创建文件夹" + dir + " 成功！");

            } else {
                logger.debug("创建文件夹" + dir + " 失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    // 检查路径是否存在，存在返回true，否则false
    public boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
   /* for (FTPFile ftpFile : ftpFileArr) {
        if (ftpFile.isDirectory()
                && ftpFile.getName().equalsIgnoreCase(path)) {
            flag = true;
            break;
        }
    } */
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 递归创建远程服务器目录
     *
     * @param remote 远程服务器文件绝对路径
     * @return 目录创建是否成功
     * @throws IOException
     */
    public boolean CreateDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                if (changeWorkingDirectory(subDirectory)) {
                    if (makeDirectory(subDirectory)) {
                        changeWorkingDirectory(subDirectory);
                    } else {
                        logger.debug("创建目录[" + subDirectory + "]失败");
                        System.out.println("创建目录[" + subDirectory + "]失败");
                        success = false;
                        return success;
                    }
                }
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

    public static void main(String[] args) throws InterruptedException {
/*        FTPClientTest ftpClientTest = new FTPClientTest();
        System.out.println(ftpClientTest.getUserName());

        ftpClientTest.setArg("/application.yml");*/
       /* FTPClientTest ftpClient = new FTPClientTest();
        if (ftpClient.connectServer()) {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);// 设置传输二进制文件
            ftpClient.uploadManyFile("H:\\d", "/d/");
            ftpClient.closeConnect();// 关闭连接*/
        long l = System.currentTimeMillis();
        for (int i= 0; i<10;i++) {
            System.out.println(i);
            //Thread.sleep(100);
        }
        long m = System.currentTimeMillis();
        System.out.println(System.getProperty(""));
        System.out.println((m-l)/1000);
        }
    }