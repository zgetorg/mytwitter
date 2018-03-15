package com.gech.twitterclone.controllers;

import com.gech.twitterclone.models.Friendship;
import com.gech.twitterclone.models.Post;
import com.gech.twitterclone.models.User;
import com.gech.twitterclone.repositories.FriendshipRepository;
import com.gech.twitterclone.repositories.PhotoRepository;
import com.gech.twitterclone.repositories.PostRepository;
import com.gech.twitterclone.repositories.UserRepository;
import com.gech.twitterclone.services.UserService;
import com.gech.twitterclone.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private FriendshipRepository friendshipRepository;
    
    @Autowired
    private PhotoRepository photoRepository;

    @RequestMapping("/")
    public String index(Model m){
    	
    	m.addAttribute("allPosts",postRepository.findAll());
    	return "newsfeed";
    }

    @RequestMapping("/login")
    public String login(){
        return "loginpage";
    }

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model){

        model.addAttribute("user", user);
        userValidator.validate(user, result);

        if (result.hasErrors()) {
            return "registration";
        } else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Successfully Created");
            model.addAttribute("newpost", new Post());
        }
        return "redirect:/login";
    }
    
    @RequestMapping("/logout")
    public String LoggingOut(){
    	return "landingpage";
    }
    
    @RequestMapping("/me")
    public String profile(Principal principal, Model model){
    	User user = userRepository.findByEmail(principal.getName());
    	List<Post> posts = postRepository.findByPostedBy_Id(user.getId());
    	model.addAttribute("allPosts", posts);
    	
    	return "tweet";
    }
    
    @RequestMapping("/allusers")
    public String getallUsers( Model model){
    	
    	Iterable<User> users = userRepository.findAll();
    	model.addAttribute("everyUser", users);
    	
    	return "allusers";
    }
    
    @RequestMapping("/allusers/me")
    public String allUsers(Principal principal, Model model){
    	User u = userRepository.findByEmail(principal.getName());
    	List<Friendship> friends = friendshipRepository.findByFollower_Id(u.getId());
    	Iterable<User> users = userRepository.findAll();
    	Map<User, Boolean> allusers = new HashMap<User, Boolean>();
    	
    	for(User user: users){
    			Friendship fd = friendshipRepository.findByFollower_IdAndFollowing_Id(u.getId(), user.getId());
    			if(fd!=null){
    				allusers.put(user, fd.isConfirmed());
    			
    			}else{
    				fd = new Friendship();
    				fd.setConfirmed(false);
    				allusers.put(user, fd.isConfirmed());
    			}
    	}
    	
    	model.addAttribute("allUsers", allusers);
    	
    	return "allusers";
    }
    
    @RequestMapping("/following")
    public String listfollower(Principal principal, Model model){
    	
    	User user = userRepository.findByEmail(principal.getName());
    	
    	List<Friendship> friends = friendshipRepository.findByFollower_Id(user.getId());
    	
    	List<User> users = new ArrayList<User>();
    	
    	for(Friendship friend : friends){
    		
    		users.add(friend.getFollowing());
    	}
    	
    	model.addAttribute("followingUsers", users);
    	
    	return "allusers";
    }
    
    @RequestMapping("/follower")
    public String listfollowing(Principal principal, Model model){
    	
    	User user = userRepository.findByEmail(principal.getName());
    	
    	List<Friendship> friends = friendshipRepository.findByFollowing_Id(user.getId());
    	
    	Map<User, Boolean> users = new HashMap<User, Boolean>();
    	
    	for(Friendship friend : friends){
    		
    		users.put(friend.getFollower(), friend.isConfirmed());
    	}
    	
    	model.addAttribute("followers", users);
    	
    	return "allusers";
    }
    
    @RequestMapping("/follow/{id}")
    public String follow(@PathVariable("id") Long id, Principal principal, Model model){
    	User followed = userRepository.findOne(id);
    	User follower = userRepository.findByEmail(principal.getName());
    	Friendship friendship = friendshipRepository.findByFollower_IdAndFollowing_Id(follower.getId(),followed.getId());
    	
    	if(friendship==null){
    		friendship = new Friendship();
    		friendship.setFollower(follower);
        	friendship.setFollowing(followed);
        	friendship.setConfirmed(true);
        	friendshipRepository.save(friendship);
    	}
    	
    	/*List<Friendship> friends = friendshipRepository.findByFollower_Id(follower.getId());
    	List<User> users = new ArrayList<User>();
    	
    	for(Friendship friend : friends){
    		
    		users.add(friend.getFollowing());
    	}
    	
    	model.addAttribute("friends", users);*/
    	
    	return "redirect:/allusers/me";
    }
    
    
    public UserValidator getUserValidator() {
        return userValidator;
    }

    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }
}
