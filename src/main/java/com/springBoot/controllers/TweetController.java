package com.springBoot.controllers;



import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.springBoot.models.Tweet;
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
public class TweetController {

    @Autowired
    private UserService userService;

    @Autowired 
    TweetService tweetService;

    @GetMapping("/tweets")
    public String getFeed(@RequestParam(value = "filter", required = false) String filter ,Model model) {

        List<TweetDisplay> tweets = new ArrayList<>();
        User loggedInUser = userService.getLoggedInUser();

        if(filter == null){
            filter = "all";
        }
        if (filter.equalsIgnoreCase("following")) {
            List<User> following = loggedInUser.getFollowing();
            tweets = tweetService.findAllByUsers(following);
            model.addAttribute("filter", "following");
        }
        else{
            tweets = tweetService.findAll();
            model.addAttribute("filter", "all");
        }
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
        List<TweetDisplay> tweets = tweetService.findAllWithTag(tag);
        model.addAttribute(("tweetList"), tweets);
        model.addAttribute("tag", tag);
        return "tweets/taggedTweets";
    }

}
