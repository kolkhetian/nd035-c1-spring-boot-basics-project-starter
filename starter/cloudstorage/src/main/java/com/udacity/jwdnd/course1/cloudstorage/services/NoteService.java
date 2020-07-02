package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NoteService
{
    private Logger logger = LoggerFactory.getLogger(NoteService.class);

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper)
    {
        this.noteMapper = noteMapper;
    }

    public List<Note> getAllNotes()
    {
        return noteMapper.getAllNotes();
    }

    public List<Note> getNotesByUser(int userId)
    {
        return noteMapper.getNotesByUser(userId);
    }

    public Note saveOrUpdate(Note note)
    {
        if(note.getNoteId() == null)
        {
            noteMapper.save(note);
        }
        else
        {
            noteMapper.update(note);
        }
        return note;
    }

    public void delete(int id)
    {
        noteMapper.delete(id);
    }
}
