package guru.ioio.tool.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class Md5Util {

    public static String MD5Encode(byte[] bytes) {
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] digest = md.digest();
            String text;
            for (int i = 0; i < digest.length; i++) {
                text = Integer.toHexString(0xFF & digest[i]);
                if (text.length() < 2) {
                    text = "0" + text;
                }
                hexString.append(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

    public static String MD5Encode(String text) {
        return MD5Encode(text.getBytes());
    }
    
    /**
     * 计算文件的md5
     * 
     * @param fileName
     * @return
     */
    public static String Md5ForFile(String fileName) {
        InputStream fis;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try{
            fis = new FileInputStream(fileName);
            md5 = MessageDigest.getInstance("MD5");
            while((numRead=fis.read(buffer)) > 0) {
                md5.update(buffer,0,numRead);
            }
            fis.close();
            return toHexString(md5.digest());   
        } catch (Exception e) {
            System.out.println("error");
            return null;
        }
    }
    
    private static String toHexString(byte[] digest) {
        StringBuffer hexString = new StringBuffer();
        String text;
        for (int i = 0; i < digest.length; i++) {
            text = Integer.toHexString(0xFF & digest[i]);
            if (text.length() < 2) {
                text = "0" + text;
            }
            hexString.append(text);
        }
        return hexString.toString();
    }
}
