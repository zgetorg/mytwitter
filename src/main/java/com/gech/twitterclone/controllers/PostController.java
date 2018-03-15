package com.gech.twitterclone.controllers;
import com.gech.twitterclone.models.Friendship;
import com.gech.twitterclone.models.Photo;
import com.gech.twitterclone.models.Post;
import com.gech.twitterclone.models.User;
import com.gech.twitterclone.repositories.FriendshipRepository;
import com.gech.twitterclone.repositories.PhotoRepository;
import com.gech.twitterclone.repositories.PostRepository;
import com.gech.twitterclone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PostController {

  
    @Autowired
    private UserRepository userRepository;
   
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;
    
    @Autowired
    private PhotoRepository photoRepository;
    
    @RequestMapping(value="/newsfeed", method = RequestMethod.GET)
    public String getPostForm(Model m, Principal p){
    	
    	User user = userRepository.findByEmail(p.getName());
    	List<Friendship> friendship = friendshipRepository.findByFollower_Id(user.getId());
    	List<Post> posts = new ArrayList<Post>();
    	List<Photo> pictures =  new ArrayList<Photo>();
    	
    	for(Friendship fr : friendship){
    		User u = fr.getFollowing();
    		posts.addAll(postRepository.findByPostedBy_Id(u.getId()));
    		pictures.addAll(photoRepository.findByUser_Id(u.getId()));
    	}
    	
    	posts.addAll(postRepository.findByPostedBy_Id(user.getId()));
    	pictures.addAll(photoRepository.findByUser_Id(user.getId()));
    	m.addAttribute("allPosts",posts);
    	m.addAttribute("photos", pictures);
    	return "newsfeed";
    	
    }
    
    @RequestMapping(value="/allposts", method = RequestMethod.GET)
    public String allPost(Model m){
    	
    	m.addAttribute("allPosts",postRepository.findAll());
    	m.addAttribute("photos", photoRepository.findAll());
    	return "newsfeed";
    	
    }
    
    @RequestMapping(value="/savepost", method = RequestMethod.POST)
    public String savePost(String content,  Principal p, Model m){
    	Post post = new Post();
    	post.setContent(content);
    	post.setPostedBy(userRepository.findByEmail(p.getName()));
    	post.setPostedDate(new Date());
    	postRepository.save(post);
    	
    	return "redirect:/newsfeed";
    }
    
    @RequestMapping(value="/post/{id}", method = RequestMethod.GET)
    public String savePost(@PathVariable(value="id") Long id,  Principal p, Model m){
    	
    	List<Post> post = postRepository.findByPostedBy_Id(id);
    	List<Photo> photos = photoRepository.findByUser_Id(id);
    	m.addAttribute("allPosts",post);
    	m.addAttribute("photos", photos);
    	return "tweet";
    }
    
    }