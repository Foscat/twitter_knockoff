package com.springBoot.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.springBoot.models.Tag;
import com.springBoot.models.Tweet;
import com.springBoot.models.User;
import com.springBoot.repos.TagRepo;
import com.springBoot.repos.TweetRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TweetService {

    @Autowired
    private TweetRepo tweetRepo;

    @Autowired
    private TagRepo tagRepo;

    User user;
    List<User> users;

    @Autowired
    public TweetService(TweetRepo tweetRepo) {
        this.tweetRepo = tweetRepo;
    }

    public List<Tweet> findAll() {

        List<Tweet> tweets = tweetRepo.findAllByOrderByCreatedAtDesc();
        return formatTweets(tweets);
        
    }

    public List<Tweet> findAllByUser(User user) {

        List<Tweet> tweets = tweetRepo.findAllByUserOrderByCreatedAtDesc(user);
        return tweets;
        
    }

    public List<Tweet> findAllByUsers(List<User> users) {

        List<Tweet> tweets = tweetRepo.findAllByUserInOrderByCreatedAtDesc(users);
        return tweets;
        
    }

    public void save(Tweet tweet){
        handleTags(tweet);
        tweetRepo.save(tweet);
    }

    private void handleTags(Tweet tweet){
        List<Tag> tags = new ArrayList<Tag>();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(tweet.getMessage());
        while (matcher.find()) {
            String phrase = matcher.group().substring(1).toLowerCase();
            Tag tag = tagRepo.findByPhrase(phrase);
            if (tag == null) {
                tag = new Tag();
                tag.setPhrase(phrase);
                tagRepo.save(tag);   
            }
            tags.add(tag);
        }
         tweet.setTags(tags);
    }

    private void addTagLinks(List<Tweet> tweets) {

        Pattern pattern = Pattern.compile("#\\w+");
        for (Tweet tweet : tweets) {
            String message = tweet.getMessage();
            Matcher matcher = pattern.matcher(message);
            Set<String> tags = new HashSet<String>();
            while (matcher.find()) {
                tags.add(matcher.group());
            }
            for (String tag : tags) {
                message = message.replaceAll(tag, 
                "<a class='tag' href='/tweets/" + tag.substring(1).toLowerCase() + "'>" + tag + "</a>");
            }
            tweet.setMessage(message);
        }
        
    }

    private List<Tweet> formatTweets(List<Tweet> tweets){
        addTagLinks(tweets);
        return tweets;
    }

    public List<Tweet> findAllWithTag(String tag){
        List<Tweet> tweets = tweetRepo.findByTags_PhraseOrderByCreatedAtDesc(tag);
        return formatTweets(tweets);
    }

}
