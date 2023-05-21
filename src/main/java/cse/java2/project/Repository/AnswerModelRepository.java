package cse.java2.project.Repository;

import cse.java2.project.model.AnswerModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerModelRepository extends JpaRepository<AnswerModel, Long> {
    boolean existsAnswerModelByQuestionId(long questioId);

    List<AnswerModel> findByQuestionId(long questionId);
}
