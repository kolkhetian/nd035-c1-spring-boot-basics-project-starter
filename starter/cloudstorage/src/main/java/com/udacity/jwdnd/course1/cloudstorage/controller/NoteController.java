package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class NoteController
{
    private NoteService noteService;
    private UserService userService;
    private AuthenticationService authenticationService;

    public NoteController(NoteService noteService, UserService userService, AuthenticationService authenticationService)
    {
        this.noteService = noteService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    public List<Note> fetchAll()
    {
        return noteService.getNotesByUser(authenticationService.currentUser().getUserId());
    }

    @PostMapping("/notes")
    public String saveOrUpdateNote(@ModelAttribute("note") Note note, RedirectAttributes redirectAttributes)
    {
        try
        {
            if(note.getNoteTitle().isEmpty() || note.getNoteDescription().isEmpty())
            {
                throw new Exception();
            }

            note.setUserId(authenticationService.currentUser().getUserId());
            noteService.saveOrUpdate(note);

            redirectAttributes.addFlashAttribute("noteMessageSuccess", "Your note saved successfully");
        }
        catch (Exception e)
        {
            redirectAttributes.addFlashAttribute("noteMessageError", "An error occurred while saving note: " + e.getMessage());
        }

        return "redirect:/home#nav-notes-tab";
    }

    @GetMapping("/notes/delete/{id}")
    public String deleteNote(@PathVariable(value = "id")  int id, RedirectAttributes redirectAttributes)
    {
        try
        {
            noteService.delete(id);
            redirectAttributes.addFlashAttribute("noteMessageSuccess", "Note deleted successfully");
        }
        catch(Exception e)
        {
            redirectAttributes.addFlashAttribute("noteMessageError", "An error occurred: " + e.getMessage());
        }
        return "redirect:/home#nav-notes-tab";
    }
}
