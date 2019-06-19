package com.springBoot.repos;

import com.springBoot.models.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Long>{

	public User findByUsername(String username);
}
