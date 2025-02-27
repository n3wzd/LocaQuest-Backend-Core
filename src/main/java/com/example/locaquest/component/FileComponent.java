package com.example.locaquest.component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.example.locaquest.exception.FileNotExistsException;
import com.example.locaquest.exception.ServiceException;

@Component
public class FileComponent {
	@Value("${public.user-profile}")
    private String userProfilePath;
	
	public void saveProfileImage(MultipartFile file, int userId) {
		if (file.isEmpty()) {
    		throw new FileNotExistsException();
        }
		if (!isValidImage(file)) {
    		throw new ServiceException("Invalid image file");
        }
        try {
        	File newFile = new File(getProfileImagePath(userId));
        	newFile.createNewFile();
        	Path path = Paths.get(newFile.toURI()).toAbsolutePath();
			file.transferTo(path);
		} catch (Exception e) {
			throw new ServiceException("Faild to save file: " + e.toString());
		}
    }
	
	public String getProfileImagePath(int userId) {
        return userProfilePath + Integer.toString(userId) + ".jpg";
    }
	
	private boolean isValidImage(MultipartFile file) {
	    try {
	        BufferedImage image = ImageIO.read(file.getInputStream());
	        return image != null;
	    } catch (IOException e) {
	        return false;
	    }
	}
}
