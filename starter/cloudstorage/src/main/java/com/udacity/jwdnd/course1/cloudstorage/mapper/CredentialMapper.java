package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CredentialMapper
{
    @Select("SELECT * FROM credentials")
    List<Credential> getAllCredentials();

    @Select("SELECT * FROM credentials WHERE userId=#{userId}")
    List<Credential> getCredentialsByUser(int userId);

    @Insert("INSERT INTO credentials (url, username, key, password, userId) VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    void save(Credential credential);

    @Update("UPDATE credentials SET url = #{url}, username = #{username}, key = #{key}, password = #{password} WHERE credentialId = #{credentialId}")
    void update(Credential credential);

    @Delete("DELETE FROM credentials WHERE credentialId = #{id}")
    void delete(int id);
}
