package com.springBoot.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import com.springBoot.models.TweetDisplay;
import com.springBoot.models.User;
import com.springBoot.services.TweetService;
import com.springBoot.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    @Autowired
    UserService userService;

    @Autowired
    TweetService tweetService;

    @GetMapping("/signIn")
    public String renderSignInPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "dashboard/signIn";
    }

    @GetMapping("/signUp")
    public String renderSignUpPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "dashboard/signUp";
    }

    @PostMapping("/signUp")
    public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {

        User userExists = userService.findByUserName(user.getUsername());

        if(userExists != null){
            bindingResult.rejectValue("username", "error.user", "Username is already taken");
        }
        if(!bindingResult.hasErrors()) {
            userService.saveNewUser(user);
            model.addAttribute("success", "Sign up successful");
            model.addAttribute("user", new User());
        }

        return "dashboard/signUp";
        
    }

    private void SetTweetCounts(List<User> users, Model model) {
        HashMap<String,Integer> tweetCounts = new HashMap<>();
        for (User user : users) {
            List<TweetDisplay> tweets = tweetService.findAllByUser(user);
            tweetCounts.put(user.getUsername(), tweets.size());
        }
        model.addAttribute("tweetCounts", tweetCounts);
    }

    private void SetFollowingStatus(List<User> users, List<User> usersFollowing, Model model) {
        HashMap<String, Boolean> followingStatus = new HashMap<>();
        String username = userService.getLoggedInUser().getUsername();

        for(User user: users){
            if(usersFollowing.contains(user)){
                followingStatus.put(user.getUsername(), true);
            }
            else if(!user.getUsername().equals(username)){
                followingStatus.put(user.getUsername() , false);
            }
        }
        model.addAttribute("followingStatus", followingStatus);
    }

    @GetMapping("/users")
    public String getUsers(@RequestParam(value = "filter", required = false) String filter,  Model model){

        List<User> users = new ArrayList<User>();
        model.addAttribute("users", users);
        SetTweetCounts(users, model);

        User loggedInUser = userService.getLoggedInUser();
        List<User> usersFollowing = loggedInUser.getFollowing();
        List<User> usersFollowers = loggedInUser.getFollowers();

        if( filter == null){
            filter = "all";
        }
        if (filter.equalsIgnoreCase("following")) {
            users = usersFollowing;
            model.addAttribute("filter", "following");
        }
        else{
            users = userService.findAll();
            model.addAttribute("filter", "all");
        }
        SetFollowingStatus(users, usersFollowing, model);

        return "usersPage";
    }

    @GetMapping("/users/{username}")
    public String getUser(@PathVariable("username") String username, Model model){
        User user = userService.findByUserName(username);
        List<TweetDisplay> tweets = tweetService.findAllByUser(user);
        model.addAttribute("tweetList", tweets);
        model.addAttribute("user", user);

        User loggedInUser = userService.getLoggedInUser();
        List<User> following = loggedInUser.getFollowing();
        boolean isFollowing = false;
        for(User followedUser : following){
            if (followedUser.getUsername().equals(username)) {
                isFollowing = true;
            }
        }
        model.addAttribute("following", isFollowing);

        boolean isSelfPage = loggedInUser.getUsername().equals(username);
        model.addAttribute("isSelfPage", isSelfPage);

        return "user";
    }

}
