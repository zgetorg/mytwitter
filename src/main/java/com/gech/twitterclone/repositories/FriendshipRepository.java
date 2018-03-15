package com.gech.twitterclone.repositories;


import com.gech.twitterclone.models.Friendship;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

	Friendship findByFollower_IdAndFollowing_Id(long id, long id2);

	List<Friendship> findByFollower_Id(long id);
	
	List<Friendship> findByFollowing_Id(long id);

}
