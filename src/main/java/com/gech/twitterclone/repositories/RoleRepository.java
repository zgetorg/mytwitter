package com.gech.twitterclone.repositories;

import com.gech.twitterclone.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(String role);
}
