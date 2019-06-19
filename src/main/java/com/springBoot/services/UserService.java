package com.springBoot.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.springBoot.models.Role;
import com.springBoot.models.User;
import com.springBoot.repos.RoleRepo;
import com.springBoot.repos.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private RoleRepo roleRepo;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findByUserName(String username) {
        
        return userRepo.findByUsername(username); 
    }

    public List<User> findAll(){

        return (List<User>) userRepo.findAll();
    }

    public void save(User user){
        userRepo.save(user);
    }

    public User saveNewUser(User user){

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepo.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepo.save(user);
        
    }

    public User getLoggedInUser(){
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return findByUserName(loggedInUsername);
        
    }

}
