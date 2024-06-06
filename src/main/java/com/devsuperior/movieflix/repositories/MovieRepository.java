package com.devsuperior.movieflix.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	@Query(value = """
			SELECT obj FROM Movie obj JOIN FETCH obj.genre WHERE :genre IS NULL OR obj.genre = :genre ORDER BY obj.title
			""", countQuery = """
			SELECT COUNT(obj) FROM Movie obj JOIN obj.genre WHERE :genre IS NULL OR obj.genre = :genre
			""")
	Page<Movie> searchMovieByGenre(Genre genre, Pageable pageable);
}
