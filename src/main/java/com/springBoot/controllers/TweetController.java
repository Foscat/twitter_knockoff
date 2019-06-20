package com.springBoot.controllers;



import java.util.List;

import javax.validation.Valid;

import com.springBoot.models.Tweet;
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

@Controller
public class TweetController {

    @Autowired
    private UserService userService;

    @Autowired 
    TweetService tweetService;

    @GetMapping("/tweets")
    public String getFeed(Model model) {

        List<Tweet> tweets = tweetService.findAll();
        model.addAttribute("tweetlist", tweets);
        return "tweets/feed";
        
    }

    @GetMapping("/tweets/new")
    public String getTweetForm(Model model) {

        model.addAttribute("tweet", new Tweet());

        return "tweets/newTweet";
        
    }

    @PostMapping("/tweets")
    public String submitTweetForm(@Valid Tweet tweet, BindingResult bindingResult, Model model) {

        User user = userService.getLoggedInUser();

        // if(bindingResult.hasErrors()){
        //     model.addAttribute("mesage", "dafuq bro");
        // }
        if (!bindingResult.hasErrors()) {

            tweet.setUser(user);
            tweetService.save(tweet);
            model.addAttribute("successMessage", "Tweet made! Im so proud of you.");
            model.addAttribute("tweet", new Tweet());

            
        }
        return "tweets/newTweet";
    }

    @GetMapping("/tweets/{tag}")
    public String getTweetsByTag(@PathVariable("tag") String tag, Model model) {
        List<Tweet> tweets = tweetService.findAllWithTag(tag);
        model.addAttribute(("tweetList"), tweets);
        model.addAttribute("tag", tag);
        return "tweets/taggedTweets";
    }

}
