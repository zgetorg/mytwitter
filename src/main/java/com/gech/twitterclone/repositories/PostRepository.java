package com.gech.twitterclone.repositories;


import com.gech.twitterclone.models.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {

	List<Post> findByPostedBy_Id(long id);


}