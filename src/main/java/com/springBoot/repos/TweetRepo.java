package com.springBoot.repos;

import java.util.List;

import com.springBoot.models.Tweet;
import com.springBoot.models.User;

import org.springframework.data.repository.CrudRepository;

public interface TweetRepo extends CrudRepository<Tweet, Long>{

    public List<Tweet> findAllByOrderByCreatedAtDesc();
    public List<Tweet> findAllByUserOrderByCreatedAtDesc(User user);
    public List<Tweet> findAllByUserInOrderByCreatedAtDesc(List<User> users);
    public List<Tweet> findByTags_PhraseOrderByCreatedAtDesc(String phrase);

}
