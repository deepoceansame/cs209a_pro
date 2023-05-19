package cse.java2.project.model;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class QuestionModel {
    @Id
    public long questionId;

    @Column(name="question_json",columnDefinition="LONGTEXT")
    public String questionJson;

    public QuestionModel(long questionId, String questionJson) {
        this.questionId = questionId;
        this.questionJson = questionJson;
    }

    public QuestionModel() {

    }
}
