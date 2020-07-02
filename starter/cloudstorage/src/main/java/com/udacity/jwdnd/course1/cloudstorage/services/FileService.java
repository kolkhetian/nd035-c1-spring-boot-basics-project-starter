package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService
{
    private Logger logger = LoggerFactory.getLogger(FileService.class);

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper)
    {
        this.fileMapper = fileMapper;
    }

    public List<File> getAllFiles()
    {
        return fileMapper.getAllFiles();
    }

    public List<File> getFilesByUser(int userId)
    {
        return fileMapper.getFilesByUser(userId);
    }

    public File getFile(int fileId)
    {
        return fileMapper.getFile(fileId);
    }

    public void delete(int id)
    {
        fileMapper.delete(id);
    }

    public File uploadFile(MultipartFile file, int userId) throws IOException
    {
        File fileModel = new File();
        fileModel.setFilename(file.getOriginalFilename());
        fileModel.setContentType(file.getContentType());
        fileModel.setFileSize(getSizeFromLong(file.getSize()));
        fileModel.setFileData(file.getBytes());
        fileModel.setUserId(userId);
        fileMapper.save(fileModel);
        return fileModel;
    }

    private String getSizeFromLong(Long longSize)
    {
        int kiloByteValue = 1024*1024;
        int megaByteValue = kiloByteValue*1024;

        if (longSize >= megaByteValue)
        {
            return longSize / megaByteValue + "MB";
        }
        else if (longSize >= kiloByteValue)
        {
            return longSize / kiloByteValue + "KB";
        }
        else
        {
            return String.valueOf(longSize);
        }
    }

}
