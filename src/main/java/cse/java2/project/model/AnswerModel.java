package cse.java2.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AnswerModel {
    @Id
    public long answerId;

    public long questionId;
    @Column(name="answer_json",columnDefinition="LONGTEXT")
    public String answerJson;

    public AnswerModel(long answerId, long questionId, String answerJson){
        this.answerId = answerId;
        this.questionId = questionId;
        this.answerJson = answerJson;
    }

    public AnswerModel(){}
}
