package org.webworkbench.web.request;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUpload {

    private byte[] bytes;

    private String fileName;

    private String fileType;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void write(File file) throws IOException {
//        if(file.exists()){
//            // TODO 提示文件已经存在！
//        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes,0,bytes.length);
        fos.flush();
        fos.close();
    }
}
