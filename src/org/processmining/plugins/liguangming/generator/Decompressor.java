package org.processmining.plugins.liguangming.generator;

  
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
  
/** 
 * ZIP�럨煐⒴램�끁 
 *  
 * @author 歟곫젉    
 * @since 1.0 
 */  
public class Decompressor {  
  
    public static final String EXT = ".zip";  
    private static final String BASE_DIR = "";  
    private static final String PATH = File.separator;  
    private static final int BUFFER = 1024;  
  
    /** 
     * �뻼餓� 鰲ｅ럨煐� 
     *  
     * @param srcPath 
     *            繹먩뻼餓띈러孃� 
     *  
     * @throws Exception 
     */  
    public static void decompress(String srcPath) throws Exception {  
        File srcFile = new File(srcPath);  
  
        decompress(srcFile);  
    }  
  
    /** 
     * 鰲ｅ럨煐� 
     *  
     * @param srcFile 
     * @throws Exception 
     */  
    public static void decompress(File srcFile) throws Exception {  
        String basePath = srcFile.getParent();  
        decompress(srcFile, basePath);  
    }  
  
    /** 
     * 鰲ｅ럨煐� 
     *  
     * @param srcFile 
     * @param destFile 
     * @throws Exception 
     */  
    public static void decompress(File srcFile, File destFile) throws Exception {  
  
        CheckedInputStream cis = new CheckedInputStream(new FileInputStream(  
                srcFile), new CRC32());  
  
        ZipInputStream zis = new ZipInputStream(cis);  
  
        decompress(destFile, zis);  
  
        zis.close();  
  
    }  
  
    /** 
     * 鰲ｅ럨煐� 
     *  
     * @param srcFile 
     * @param destPath 
     * @throws Exception 
     */  
    public static void decompress(File srcFile, String destPath)  
            throws Exception {  
        decompress(srcFile, new File(destPath));  
  
    }  
  
    /** 
     * �뻼餓� 鰲ｅ럨煐� 
     *  
     * @param srcPath 
     *            繹먩뻼餓띈러孃� 
     * @param destPath 
     *            �쎅�젃�뻼餓띈러孃� 
     * @throws Exception 
     */  
    public static void decompress(String srcPath, String destPath)  
            throws Exception {  
  
        File srcFile = new File(srcPath);  
        decompress(srcFile, destPath);  
    }  
  
    /** 
     * �뻼餓� 鰲ｅ럨煐� 
     *  
     * @param destFile 
     *            �쎅�젃�뻼餓� 
     * @param zis 
     *            ZipInputStream 
     * @throws Exception 
     */  
    private static void decompress(File destFile, ZipInputStream zis)  
            throws Exception {  
  
        ZipEntry entry = null;  
        while ((entry = zis.getNextEntry()) != null) {  
  
            // �뻼餓�  
            String dir = destFile.getPath() + File.separator + entry.getName();  
  
            File dirFile = new File(dir);  
  
            // �뻼餓뜻��윥  
            fileProber(dirFile);  
  
            if (entry.isDirectory()) {  
                dirFile.mkdirs();  
            } else {  
                decompressFile(dirFile, zis);  
            }  
  
            zis.closeEntry();  
        }  
    }  
  
    /** 
     * �뻼餓뜻렋�뭹 
     *  
     *  
     * 壤볡댍�쎅壤뺜툖耶섇쑉�뿶竊뚦닗兩븀쎅壤뺧펯 
     *  
     *  
     * @param dirFile 
     */  
    private static void fileProber(File dirFile) {  
  
        File parentFile = dirFile.getParentFile();  
        if (!parentFile.exists()) {  
  
            // �믣퐩野삥돻訝딁벨�쎅壤�  
            fileProber(parentFile);  
  
            parentFile.mkdir();  
        }  
  
    }  
  
    /** 
     * �뻼餓띈㎗�럨煐� 
     *  
     * @param destFile 
     *            �쎅�젃�뻼餓� 
     * @param zis 
     *            ZipInputStream 
     * @throws Exception 
     */  
    private static void decompressFile(File destFile, ZipInputStream zis)  
            throws Exception {  
  
        BufferedOutputStream bos = new BufferedOutputStream(  
                new FileOutputStream(destFile));  
  
        int count;  
        byte data[] = new byte[BUFFER];  
        while ((count = zis.read(data, 0, BUFFER)) != -1) {  
            bos.write(data, 0, count);  
        }  
  
        bos.close();  
    }  
  
}  