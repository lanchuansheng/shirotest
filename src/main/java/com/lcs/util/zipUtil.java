package com.lcs.util;

import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 *
 * @version 1.0
 * @since 2015-9-11
 * @category com.feng.util
 *
 */
public final class zipUtil
{

    /**
     * 缓冲大小
     * 10
     * 100000000000
     */
    private static int BUFFERSIZE = 2 << 10;

    /**
     * 压缩
     * @param paths
     * @param fileName
     */
    public static void zip(String[] paths, String fileName)
    {

        ZipOutputStream zos = null;
        try
        {
            zos = new ZipOutputStream(new FileOutputStream(fileName));
            for(String filePath : paths)
            {
                //递归压缩文件
                File file = new File(filePath);
                String relativePath = file.getName();
                if(file.isDirectory())
                {
                    relativePath += File.separator;
                }
                zipFile(file, relativePath, zos);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(zos != null)
                {
                    zos.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void zipFile(File file, String relativePath, ZipOutputStream zos)
    {
        InputStream is = null;
        try
        {
            if(!file.isDirectory())
            {
                ZipEntry zp = new ZipEntry(relativePath);
                zos.putNextEntry(zp);
                is = new FileInputStream(file);
                byte[] buffer = new byte[BUFFERSIZE];
                int length = 0;
                while ((length = is.read(buffer)) >= 0)
                {
                    zos.write(buffer, 0, length);
                }
                zos.flush();
                zos.closeEntry();
            }
            else
            {
                String tempPath = null;
                for(File f: file.listFiles())
                {
                    tempPath = relativePath + f.getName();
                    if(f.isDirectory())
                    {
                        tempPath += File.separator;
                    }
                    zipFile(f, tempPath, zos);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(is != null)
                {
                    is.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压缩
     * @param fileName
     * @param path
     */
    public static void unzip(String fileName, String path)
    {
        FileOutputStream fos = null;
        InputStream is = null;
        try
        {
            ZipFile zf = new ZipFile(new File(fileName));
            Enumeration en = zf.entries();
            while (en.hasMoreElements())
            {
                ZipEntry zn = (ZipEntry) en.nextElement();
                if (!zn.isDirectory())
                {
                    is = zf.getInputStream(zn);
                    File f = new File(path + zn.getName());
                    File file = f.getParentFile();
                    file.mkdirs();
                    fos = new FileOutputStream(path + zn.getName());
                    int len = 0;
                    byte bufer[] = new byte[BUFFERSIZE];
                    while (-1 != (len = is.read(bufer)))
                    {
                        fos.write(bufer, 0, len);
                    }
                    fos.close();
                }
            }
        }
        catch (ZipException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(null != is)
                {
                    is.close();
                }
                if(null != fos)
                {
                    fos.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }


    }

    /**
     * 通过网络地址获取文件InputStream
     *
     * @param path 地址
     * @return
     */
    public static InputStream returnBitMap(String path) {
        URL url = null;
        InputStream is = null;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//利用HttpURLConnection对象,我们可以从网络中获取网页数据.
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();    //得到网络返回的输入流

        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }



    /**
     * 通过网络url取文件，并保存
     *
     * @param path 文件保存路径
     * @param url  网络地址
     */
    public static void uploadImage(String path, String url) {
        try {
            URL pathUrl = new URL(url);
            DataInputStream dataInputStream = new DataInputStream(pathUrl.openStream());
            File file = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            while (dataInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);//将buffer中的字节写入文件中区
            }
            dataInputStream.close();//关闭输入流
            fileOutputStream.close();//关闭输出流

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param args
     */
    public static void main(String[] args)
    {
        //zip(new String[] {"https://yunzhongbao.oss-cn-qingdao.aliyuncs.com/ceshi/3856.jpg?Expires=1614223469&OSSAccessKeyId=LTAIWLcvKrb5FOm7&Signature=ettVnFQ86Am8BzQCezbAncms6Nw%3D"}, "g:/test.zip");
        //unzip("g:/账号密码.zip", "d:/");
        uploadImage("G:/hello.jpg","https://yunzhongbao.oss-cn-qingdao.aliyuncs.com/ceshi/3856.jpg?Expires=1614223469&OSSAccessKeyId=LTAIWLcvKrb5FOm7&Signature=ettVnFQ86Am8BzQCezbAncms6Nw%3D");
    }
}