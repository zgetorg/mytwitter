package com.gech.twitterclone.controllers;
import com.cloudinary.utils.ObjectUtils;
import com.gech.twitterclone.configs.CloudinaryConfig;
import com.gech.twitterclone.models.Friendship;

import com.gech.twitterclone.models.Post;
import com.gech.twitterclone.models.User;
import com.gech.twitterclone.repositories.FriendshipRepository;
import com.gech.twitterclone.repositories.PostRepository;
import com.gech.twitterclone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class PostController {

  
    @Autowired
    private UserRepository userRepository;
   
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
	private CloudinaryConfig clodc;


    
    @RequestMapping(value="/newsfeed", method = RequestMethod.GET)
    public String getPostForm(Model m, Principal p){
    	
    	User user = userRepository.findByEmail(p.getName());
    	List<Friendship> friendship = friendshipRepository.findByFollower_Id(user.getId());
    	List<Post> posts = new ArrayList<Post>();

    	for(Friendship fr : friendship){
    		User u = fr.getFollowing();
    		posts.addAll(postRepository.findByPostedBy_Id(u.getId()));
//    		pictures.addAll(photoRepository.findByUser_Id(u.getId()));
    	}
    	
    	posts.addAll(postRepository.findByPostedBy_Id(user.getId()));
//    	pictures.addAll(photoRepository.findByUser_Id(user.getId()));
    	m.addAttribute("allPosts",posts);
//    	m.addAttribute("photos", pictures);
    	return "newsfeed";
    	
    }
    
    @GetMapping("/allposts")
    public String allPost(Model m){
    	
    	m.addAttribute("allPosts",postRepository.findAll());
//    	m.addAttribute("photos", photoRepository.findAll());
    	return "newsfeed";
    	
    }
    
    @PostMapping("/savepost")
    public String savePost(String content, @RequestParam("file") MultipartFile file, Principal p, Model m){

		Post post = new Post();
		if (file.isEmpty()&& content==null){
			return "redirect:/newsfeed";
		}
		else if(file.isEmpty()){

			post.setContent(content);
			post.setPostedBy(userRepository.findByEmail(p.getName()));
			post.setPostedDate(new Date());
			postRepository.save(post);
			return "redirect:/allposts";
		}
		else {

			try {

				Map uploadResult = clodc.upload(file.getBytes(),
						ObjectUtils.asMap("resourcetype", "auto"));
				post.setImage(uploadResult.get("url").toString());
				post.setContent(content);
				post.setPostedBy(userRepository.findByEmail(p.getName()));
				post.setPostedDate(new Date());
				postRepository.save(post);
				return "redirect:/allposts";

			} catch (IOException e) {
				e.printStackTrace();
				return "redirect:/allposts";
			}
		}

    }
    
    @RequestMapping(value="/post/{id}", method = RequestMethod.GET)
    public String savePost(@PathVariable(value="id") Long id,  Principal p, Model m){
    	
    	List<Post> post = postRepository.findByPostedBy_Id(id);
//    	List<Photo> photos = photoRepository.findByUser_Id(id);
    	m.addAttribute("allPosts",post);
//    	m.addAttribute("photos", photos);
    	return "tweet";
    }
    
    }