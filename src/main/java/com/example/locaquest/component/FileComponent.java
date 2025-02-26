package com.example.locaquest.component;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
    	String fileName = Integer.toString(userId);
    	String fileOriginalName = file.getOriginalFilename();
    	if (fileOriginalName.contains(".")) {
    		fileName += fileOriginalName.substring(fileOriginalName.lastIndexOf("."));
        }
        try {
        	File outputFile = new File(userProfilePath + fileName);
			file.transferTo(outputFile);
			BufferedImage bufferedImage = ImageIO.read(outputFile);
        	ImageIO.write(bufferedImage, "jpg", outputFile);
		} catch (Exception e) {
			throw new ServiceException("Faild to save file: " + e.toString());
		}
    }
}
