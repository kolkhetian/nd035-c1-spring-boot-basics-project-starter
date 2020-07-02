package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService
{
    private Logger logger = LoggerFactory.getLogger(CredentialService.class);

    private CredentialMapper credentialMapper;
    private HashService hashService;

    public CredentialService(CredentialMapper credentialMapper, HashService hashService)
    {
        this.credentialMapper = credentialMapper;
        this.hashService = hashService;
    }

    public List<Credential> getCredentialsByUser(int userId)
    {
        List<Credential> listOfCredential = new ArrayList<>();

        for (Credential c :credentialMapper.getCredentialsByUser(userId))
        {
            c.setEncryptedPass(securePassword(c.getPassword()));
            listOfCredential.add(c);
        }

        return listOfCredential;
    }

    public List<Credential> getAllCredentials()
    {
        List<Credential> listOfCredential = new ArrayList<>();

        for (Credential c :credentialMapper.getAllCredentials())
        {
            c.setEncryptedPass(securePassword(c.getPassword()));
            listOfCredential.add(c);
        }

        return listOfCredential;
    }

    public Credential saveOrUpdate(Credential credential)
    {
        if(credential.getCredentialId() == null)
        {
            credentialMapper.save(credential);
        }
        else
        {
            credentialMapper.update(credential);
        }
        return credential;
    }

    public void delete(int id)
    {
        credentialMapper.delete(id);
    }

    private String securePassword(String password)
    {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(password, encodedSalt);
        return hashedPassword;
    }
}
