package cse.java2.project.service;

import cse.java2.project.Repository.AnswerModelRepository;
import cse.java2.project.Repository.CommentModelRepository;
import cse.java2.project.Repository.QuestionModelRepository;
import cse.java2.project.model.AnswerModel;
import cse.java2.project.model.CommentModel;
import cse.java2.project.model.QuestionModel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class WholeService {

    private final QuestionModelRepository questionModelRepository;
    private final AnswerModelRepository answerModelRepository;
    private final CommentModelRepository commentModelRepository;

    public List<QuestionModel> questionModels = null;
    public List<AnswerModel> answerModels = null;
    public List<CommentModel> commentModels = null;

    @Autowired
    public WholeService(QuestionModelRepository questionModelRepository, AnswerModelRepository answerModelRepository, CommentModelRepository commentModelRepository) {
        this.questionModelRepository = questionModelRepository;
        this.answerModelRepository = answerModelRepository;
        this.commentModelRepository = commentModelRepository;
        questionModels = questionModelRepository.findAll();
        answerModels = answerModelRepository.findAll();
        commentModels = commentModelRepository.findAll();
    }

    /*
    gonna to solve four problem.
    1. percent of how many questions have no answer
    2. average and maximum ans count
    3. distribution of ans count
     */
    public void numberOfAnswer() {
        // solve 1
        int questionWithoutAnswerCnt = 0;
        double percentQuestionWithoutAns = 0;
        // solve 2
        double averageAnsCnt = 0;
        int totalAnsCnt = 0;
        int maxAnsCnt = 0;
        // solve 3
        Map<Integer, Integer> ansCntDisMap = new HashMap<>();

        JSONObject questionJsonObject = null;
        for (QuestionModel qm : questionModels) {
            questionJsonObject = new JSONObject(qm.questionJson);

            //solve 3
            int answerCnt = questionJsonObject.getInt("answer_count");
            ansCntDisMap.put(answerCnt, ansCntDisMap.getOrDefault(answerCnt, 0)+1);

            // solve 1
            boolean isAnswered = questionJsonObject.getBoolean("is_answered");
            if (answerCnt<=0){
                questionWithoutAnswerCnt++;
            }

            // solve 2
            totalAnsCnt += answerCnt;
            maxAnsCnt = Math.max(answerCnt, maxAnsCnt);
        }
        // solve 2
        averageAnsCnt = ((double)totalAnsCnt) / ((double)questionModels.size());

        // solve 1
        percentQuestionWithoutAns = ((double)questionWithoutAnswerCnt) / ((double)questionModels.size());

        System.out.println(" 1. percent of how many questions have no answer");
        System.out.println(percentQuestionWithoutAns);
        System.out.println("2. average and maximum ans count");
        System.out.println(averageAnsCnt + " " + maxAnsCnt);
        System.out.println("3. distribution of ans count");
        System.out.println(ansCntDisMap);
    }

    /*
    1. What percentage of questions have accepted answers (one question could only have one accepted answer)
    2. What is the distribution of question resolution time (i.e., the duration between the question posting time and the posting time of the accepted answer)?
    3. What percentage of questions have non-accepted answers (i.e., answers that are not marked as
        accepted) that have received more upvotes than the accepted answers?

    about 2: duration/1000
     */
    public void acceptedAnswer() {
        int numOfQuestion = questionModels.size();

        // solve 1
        int numOfQuestionWithAcAns = 0;
        // solve 2
        Map<Long, Integer> durationDisMap = new HashMap<>();
        // solve 3
        int numOfQuestionWithGreatAnsThatNotAc = 0;
        int numOfQuestionWithAns = 0;

        JSONObject questionJsonObject = null;
        for (QuestionModel qm : questionModels) {
            questionJsonObject = new JSONObject(qm.questionJson);

            long ques_time = questionJsonObject.getLong("creation_date");

            List<AnswerModel> answersFqm = answerModels.stream().filter(am -> am.questionId==qm.questionId).collect(Collectors.toList());
            if (answersFqm.size()>0){
                numOfQuestionWithAns++;
            }
            boolean hasAcAns = false;

            long acAnsId = 0;
            long maxVoteAnsId = 0;
            int maxVote = 0;

            JSONObject answerJsonObject = null;
            int voteForAns = 0;
            long ans_time = 0;
            for (AnswerModel am : answersFqm){
                answerJsonObject = new JSONObject(am.answerJson);
                ans_time = answerJsonObject.getLong("creation_date");
                boolean is_ac = answerJsonObject.getBoolean("is_accepted");

                voteForAns = answerJsonObject.getInt("up_vote_count");
                if (voteForAns > maxVote) {
                    maxVote = voteForAns;
                    maxVoteAnsId = am.answerId;
                }
                if(is_ac){
                    hasAcAns = true;
                    acAnsId = am.answerId;
                    long dur_time = (ans_time - ques_time)/3600;
                    if (dur_time==0) {
//                        System.out.println(am.answerId + " " + qm.questionId);
                    }
                    durationDisMap.put(dur_time, durationDisMap.getOrDefault(dur_time, 0)+1);
                    if (acAnsId!=maxVoteAnsId) {
                        numOfQuestionWithGreatAnsThatNotAc++;
                    }
                }
            }

            if (hasAcAns) {
                numOfQuestionWithAcAns++;
            }
        }

        System.out.println("1. percent of question has ac ans");
        System.out.println((double) numOfQuestionWithAcAns / (double) numOfQuestion);
        System.out.println("2. duration time distribution");
        System.out.println(durationDisMap);
        System.out.println("3. percent of question has greater ans than ac ans");
        System.out.println((double) numOfQuestionWithGreatAnsThatNotAc / (double) numOfQuestionWithAcAns);
    }
}
