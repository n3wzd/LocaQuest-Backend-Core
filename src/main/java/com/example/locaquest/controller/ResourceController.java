package com.example.locaquest.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.locaquest.constant.Route;
import com.example.locaquest.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Route.RESOURCE)
@RequiredArgsConstructor
public class ResourceController {
	private final UserService userService;

    @GetMapping(Route.RESOURCE_USER_PROFILE + "/{userId}.jpg")
    public Resource getProfileImage(@PathVariable("userId") int userId) throws MalformedURLException {
    	Path path = Paths.get(userService.getProfileImagePath(userId));
        Resource resource;
		resource = new UrlResource(path.toUri());
		return resource;
    }
}
