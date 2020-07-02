package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class CredentialController
{
    private CredentialService credentialService;
    private UserService userService;
    private AuthenticationService authenticationService;

    public CredentialController(CredentialService credentialService, UserService userService, AuthenticationService authenticationService)
    {
        this.credentialService = credentialService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    public List<Credential> fetchAll()
    {
        return credentialService.getCredentialsByUser(authenticationService.currentUser().getUserId());
    }

    @PostMapping("/credentials")
    public String saveOrUpdateCredential(@ModelAttribute("credential") Credential credential, RedirectAttributes redirectAttributes)
    {
        try
        {
            if(credential.getUsername().isEmpty() || credential.getPassword().isEmpty())
            {
                throw new Exception();
            }

            credential.setUserId(authenticationService.currentUser().getUserId());
            credentialService.saveOrUpdate(credential);

            redirectAttributes.addFlashAttribute("credentialMessageSuccess", "Your credential saved successfully");
        }
        catch (Exception e)
        {
            redirectAttributes.addFlashAttribute("credentialMessageError", "An error occurred while saving credential: " + e.getMessage());
        }

        return "redirect:/home#nav-credentials-tab";
    }

    @GetMapping("/credentials/delete/{id}")
    public String deleteCredential(@PathVariable(value = "id")  int id, RedirectAttributes redirectAttributes)
    {
        try
        {
            credentialService.delete(id);
            redirectAttributes.addFlashAttribute("credentialMessageSuccess", "Credential deleted successfully");
        }
        catch(Exception e)
        {
            redirectAttributes.addFlashAttribute("credentialMessageError", "An error occurred: " + e.getMessage());
        }
        return "redirect:/home#nav-credentials-tab";
    }
}
