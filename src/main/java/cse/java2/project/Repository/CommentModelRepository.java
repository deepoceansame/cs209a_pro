package cse.java2.project.Repository;

import cse.java2.project.model.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentModelRepository extends JpaRepository<CommentModel, Long> {
    List<CommentModel> findByPostId(long postId);
}
