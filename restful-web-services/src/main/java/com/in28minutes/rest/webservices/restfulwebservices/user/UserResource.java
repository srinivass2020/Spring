package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

@RestController
public class UserResource {
	
	@Autowired
	private UserDaoService userService;
	
	//http://localhost:8080/users
	@GetMapping("/users")
	public List<User> retrieveAllUsers(){
		return userService.findAll();
	}
	
	//http://localhost:8080/users/1
	@GetMapping(path="/users/{id}")
	public Resource<User> retrieveUser( @PathVariable int id) {
		User user = userService.findOne(id);
		if(null!=user) {
			Resource<User> model = new Resource<>(user);
			ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
			model.add(linkTo.withRel("all-users"));
			return model;
		}else {
			throw new UserNotFoundException("User Id : "+id+" Not Found");
		}
	}
	
	//http://localhost:8080/delusers/1
	@DeleteMapping(path="/delusers/{id}")
	public void deleteUser(@PathVariable int id) {
		System.out.println("Hi FindOne");
		User user = userService.deleteById(id);
		if(null==user) {
			throw new UserNotFoundException("id - "+id) ; 
		}
	}
	/**
	 * headers 	: 	Content-Type = application/json
	 * Post 	:	http://localhost:8080/users
	 * Body		:	Type = Custom / Raw
	 *				{
  						"name": "Sri",
  						"birthDate": "2000-05-10T03:29:37.460+0000"
					}
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/users")
	public ResponseEntity createUser(@Valid @RequestBody User user) {
		User savedUser = userService.save(user);
		  UriComponents location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()); 
		  return ResponseEntity.created(location.toUri()).build();
	}

	
	/**
	 * Swager UI Link 	: http://localhost:8080/swagger-ui.html
	 * Swager Link	  	:	http://localhost:8080/v2/api-docs
	 * actiator Link  	:	http://localhost:8080/actuator
	 * actiator UI Link :	http://localhost:8080/
	 */
}
