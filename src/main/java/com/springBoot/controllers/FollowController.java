package com.springBoot.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.springBoot.models.User;
import com.springBoot.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FollowController {

    @Autowired
    private UserService userService;

    @PostMapping("/follow/{username}")
    public String follow(@PathVariable("username") String username, HttpServletRequest request){

        User loggedInUser = userService.getLoggedInUser();
        User userToFollow = userService.findByUserName(username);

        List<User> followers = userToFollow.getFollowers();
        followers.add(loggedInUser);
        userToFollow.setFollowers(followers);

        userService.save(userToFollow);

        return "redirect:" + request.getHeader("Referer");

    }

    @PostMapping("/unfollow/{username}")
    public String unfollow(@PathVariable("username") String username, HttpServletRequest request){
        User loggedInUser = userService.getLoggedInUser();
        User userToUnFollow = userService.findByUserName(username);
        List<User> followers = userToUnFollow.getFollowers();
        followers.remove(loggedInUser);
        userToUnFollow.setFollowers(followers);
        userService.save(userToUnFollow);

        return "redirect" + request.getHeader("Referer");
    }

}
