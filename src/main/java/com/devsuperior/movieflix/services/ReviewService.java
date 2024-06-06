package com.devsuperior.movieflix.services;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.projections.ReviewProjection;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReviewService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Autowired
	private ReviewRepository repository;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private AuthService authService;

	@Transactional(readOnly = true)
	public List<ReviewDTO> findByMovie(Long movieId) {
		if (!movieRepository.existsById(movieId)) {
			throw new ResourceNotFoundException("Id not found " + movieId);
		}
		List<ReviewProjection> list = repository.searchByMovie(movieId);
		return list.stream().map(ReviewDTO::new).toList();
	}

	@Transactional
	public ReviewDTO insert(ReviewDTO dto) {

		User user = authService.authenticated();

		try {
			Review entity = new Review();
			entity.setMovie(movieRepository.getReferenceById(dto.getMovieId()));
			entity.setUser(user);
			entity.setText(dto.getText());

			entity = repository.save(entity);

			return new ReviewDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + dto.getMovieId());
		}
	}
}
