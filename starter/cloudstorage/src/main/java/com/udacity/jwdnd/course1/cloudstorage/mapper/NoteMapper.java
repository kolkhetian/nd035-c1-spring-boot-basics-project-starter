package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface NoteMapper
{
    @Select("SELECT * FROM notes")
    List<Note> getAllNotes();

    @Select("SELECT * FROM notes WHERE userId=#{userId}")
    List<Note> getNotesByUser(int userId);

    @Insert("INSERT INTO notes (noteTitle, noteDescription, userId) VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    void save(Note note);

    @Update("UPDATE NOTES SET noteTitle = #{noteTitle}, noteDescription = #{noteDescription} WHERE noteId = #{noteId}")
    void update(Note note);

    @Delete("DELETE from notes WHERE noteId=#{id}")
    void delete(int id);
}
