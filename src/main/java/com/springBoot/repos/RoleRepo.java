package com.springBoot.repos;

import com.springBoot.models.Role;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository <Role, Long>{

    public Role findByRole(String role);

}
