package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface FileMapper
{
    @Select("SELECT * FROM files")
    List<File> getAllFiles();

    @Select("SELECT * FROM files WHERE userId=#{userId}")
    List<File> getFilesByUser(int userId);

    @Select("SELECT * FROM files WHERE fileId=#{id}")
    File getFile(int id);

    @Insert("INSERT INTO files (filename, contentType, fileSize, fileData, userId) VALUES (#{filename}, #{contentType}, #{fileSize}, #{fileData}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void save(File file);

    @Delete("DELETE FROM files WHERE fileId=#{id}")
    void delete(int id);
}
