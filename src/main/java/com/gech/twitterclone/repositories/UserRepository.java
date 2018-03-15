package com.gech.twitterclone.repositories;


import com.gech.twitterclone.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

    Long countByEmail(String email);

	

}

