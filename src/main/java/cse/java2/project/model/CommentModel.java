package cse.java2.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CommentModel {

    @Id
    public long commentId;
    public long questionId;
    public long answerId;
    @Column(name="comment_json",columnDefinition="LONGTEXT")
    public String commentJson;

    public CommentModel (long commentId, long questionId, long answerId, String commentJson) {
        this.commentId = commentId;
        this.answerId = answerId;
        this.questionId = questionId;
        this.commentJson = commentJson;
    }

    public CommentModel() {}
}
