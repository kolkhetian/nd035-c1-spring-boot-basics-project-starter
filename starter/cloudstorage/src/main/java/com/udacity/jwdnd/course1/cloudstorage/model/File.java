package com.udacity.jwdnd.course1.cloudstorage.model;

import javax.persistence.*;

@Entity
public class File
{
    private int fileId;
    @Column(unique=true)
    private String filename;
    private String contentType;
    private String fileSize;
    private int userId;
    @Lob
    @Column(name = "fileData", columnDefinition="BLOB")
    private byte[] fileData;

    public int getFileId()
    {
        return fileId;
    }
    public void setFileId(int fileId)
    {
        this.fileId = fileId;
    }

    public String getFilename()
    {
        return filename;
    }
    public void setFilename(String fileName)
    {
        this.filename = fileName;
    }

    public String getContentType()
    {
        return contentType;
    }
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getFileSize()
    {
        return fileSize;
    }
    public void setFileSize(String fileSize)
    {
        this.fileSize = fileSize;
    }

    public int getUserId()
    {
        return userId;
    }
    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public byte[] getFileData()
    {
        return fileData;
    }
    public void setFileData(byte[] fileData)
    {
        this.fileData = fileData;
    }
}
