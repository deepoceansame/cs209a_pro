package cse.java2.project.Repository;

import cse.java2.project.model.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentModelRepository extends JpaRepository<CommentModel, Long> {
}
