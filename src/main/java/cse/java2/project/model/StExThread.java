package cse.java2.project.model;

import java.util.List;
import java.util.Map;

public class StExThread {
    public QuestionModel question;
    public List<AnswerModel> answers;
    public Map<Long, List<CommentModel>> answerId2Comments;
    public List<CommentModel> comments;
}
