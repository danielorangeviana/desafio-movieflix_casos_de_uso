package com.devsuperior.movieflix.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.projections.ReviewProjection;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

	@Query(nativeQuery = true, value = """
			SELECT tb_review.id, tb_review.text, tb_review.movie_id AS movieId,
			tb_user.id AS userId, tb_user.name AS userName, tb_user.email AS userEmail
			FROM tb_review
			INNER JOIN tb_user ON tb_user.id = tb_review.user_id
			WHERE tb_review.movie_id = :movieId
			""")
	List<ReviewProjection> searchByMovie(Long movieId);
}
