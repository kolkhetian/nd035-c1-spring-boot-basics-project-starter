package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequestMapping("/files")
@Controller
public class FileController
{
    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService)
    {
        this.fileService = fileService;
        this.userService = userService;
    }

    public User getCurrentUser()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUser(auth.getName());
        return user;
    }

    public List<File> fetchAll()
    {
        return fileService.getFilesByUser(getCurrentUser().getUserId());
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes)
    {
        try
        {
            if(file.isEmpty())
            {
                throw new IOException();
            }

            java.io.File dir = new java.io.File("upload");
            if (! dir.exists())
            {
                dir.mkdir();
            }

            Path copyLocation = Paths.get(dir.getName() + java.io.File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation);
            fileService.uploadFile(file, getCurrentUser().getUserId());
            redirectAttributes.addFlashAttribute("fileMessageSuccess", "You successfully uploaded " + file.getOriginalFilename());
        }
        catch (Exception e)
        {
            if (e instanceof IOException)
            {
                redirectAttributes.addFlashAttribute("fileMessageError", "File upload failed: " + e.getMessage());
            }
            else
            {
                redirectAttributes.addFlashAttribute("fileMessageError", "There already exists a file with the same name.");
            }
        }

        return "redirect:/home#nav-files";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable(value = "id")  int id, RedirectAttributes redirectAttributes)
    {
        try
        {
            File file = fileService.getFile(id);
            java.io.File f = new java.io.File("upload" + java.io.File.separator + file.getFilename());

            if(f.delete())
            {
                fileService.delete(id);
                redirectAttributes.addFlashAttribute("fileMessageSuccess", "Image deleted successfully");
            }
        }
        catch(Exception e)
        {
            redirectAttributes.addFlashAttribute("fileMessageError", "An error occurred: " + e.getMessage());
        }
        return "redirect:/home#nav-files";
    }

    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable(value = "id")  int id, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        File file = fileService.getFile(id);
        java.io.File f = new java.io.File("upload" + java.io.File.separator + file.getFilename());

        byte[] content = Files.readAllBytes(f.toPath());
        response.reset();
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "public, must-revalidate, post-check=0, pre-check=0, max-age=0");
        response.setContentType(file.getContentType());
        response.setHeader("Content-length", Integer.toString(content.length));
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getFilename());
        response.getOutputStream().write(content, 0, content.length);
        response.getOutputStream().flush();
    }
}
