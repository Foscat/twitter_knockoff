package com.springBoot.repos;

import com.springBoot.models.Tag;

import org.springframework.data.repository.CrudRepository;

public interface TagRepo extends CrudRepository<Tag, Long>{

    Tag findByPhrase(String phrase);

}
