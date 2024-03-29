package com.bmdb.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.bmdb.business.Movie;
import com.bmdb.db.MovieRepository;

@CrossOrigin
@RestController

@RequestMapping("/movies")

public class MovieController {
	
	@Autowired
	private MovieRepository movieRepo;
	
	// list - return all movies
	@GetMapping("/")
	public JsonResponse listMovies() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(movieRepo.findAll());
		} catch  (Exception e ){
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// demo use of a Path Variable
	// return one movie for a given ID
	@GetMapping("/{id}")
	public JsonResponse getMovie(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(movieRepo.findById(id)); 
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// add - adds a new Movie
	@PostMapping("/")
	public JsonResponse addMovie(@RequestBody Movie m) {
		// add a new movie
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(movieRepo.save(m));
		} catch (DataIntegrityViolationException dive) {	
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// update - update a movie
	@PutMapping("/")
	public JsonResponse updateMovie(@RequestBody Movie m) {
		// update a movie
		JsonResponse jr = null;
		try {
			if (movieRepo.existsById(m.getId())) {
				jr = JsonResponse.getInstance(movieRepo.save(m));
			} else {
				// record doesn't exist
				jr = JsonResponse.getInstance("Error updating Movie. "+
						"id: " + m.getId() + " doesn't exist");
			}
		} catch (DataIntegrityViolationException dive) {	
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}
	
	// delete - delete a new Movie
	@DeleteMapping("/{id}")
	public JsonResponse deleteMovie(@PathVariable int id) {
		// delete a movie
		JsonResponse jr = null;
		try {
			if (movieRepo.existsById(id)) {
				movieRepo.deleteById(id);
			} else {
				jr = JsonResponse.getInstance("Error deleting Movie. "  +
						"id: " + id + " doesn't exist");
			}
		} catch (DataIntegrityViolationException dive) {	
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		
		return jr;
	}
	
	@GetMapping("/list-movies-for-rating")
	public JsonResponse listMoviesForRating(@RequestParam String rating) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(movieRepo.findByRating(rating)); 
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

}
