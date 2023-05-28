package cse.java2.project.service;

import com.google.gson.Gson;
import cse.java2.project.Repository.AnswerModelRepository;
import cse.java2.project.Repository.CommentModelRepository;
import cse.java2.project.Repository.QuestionModelRepository;
import cse.java2.project.Repository.StExThreadModelRepository;
import cse.java2.project.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class WholeService {

    private final QuestionModelRepository questionModelRepository;
    private final AnswerModelRepository answerModelRepository;
    private final CommentModelRepository commentModelRepository;

//    private final StExThreadModelRepository stExThreadModelRepository;

    public List<QuestionModel> questionModels = null;
    public List<AnswerModel> answerModels = null;
    public List<CommentModel> commentModels = null;

    public static List<StExThread> threadList = null;

    @Autowired
    public WholeService(QuestionModelRepository questionModelRepository, AnswerModelRepository answerModelRepository,
                        CommentModelRepository commentModelRepository) {
        this.questionModelRepository = questionModelRepository;
        this.answerModelRepository = answerModelRepository;
        this.commentModelRepository = commentModelRepository;


        questionModels = questionModelRepository.findAll();
        answerModels = answerModelRepository.findAll();
        commentModels = commentModelRepository.findAll();
        if (threadList == null){
            threadList = buildThreadLis();
        }
//        Gson gson = new Gson();
//        threadList = new ArrayList<>();
//        List<StExThreadModel> thModels = stExThreadModelRepository.findAll();
//        for (StExThreadModel thModel : thModels) {
//            StExThread th = gson.fromJson(thModel.threadJson, StExThread.class);
//            threadList.add(th);
//        }
    }

    /*
    gonna to solve four problem.
    1. percent of how many questions have no answer
    2. average and maximum ans count
    3. distribution of ans count
     */
    public SolNumAnswer numberOfAnswer() {
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

        return new SolNumAnswer(percentQuestionWithoutAns, averageAnsCnt, maxAnsCnt, new TreeMap<>(ansCntDisMap));
    }

    /*
    1. What percentage of questions have accepted answers (one question could only have one accepted answer)
    2. What is the distribution of question resolution time (i.e., the duration between the question posting time and the posting time of the accepted answer)?
    3. What percentage of questions have non-accepted answers (i.e., answers that are not marked as
        accepted) that have received more upvotes than the accepted answers?

    about 2: duration/1000
     */
    public SolAcAnswer acceptedAnswer() {
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
        double percentQuestionWithAcAns = (double) numOfQuestionWithAcAns / (double) numOfQuestion;
        System.out.println(percentQuestionWithAcAns);
        System.out.println("2. duration time distribution");
        System.out.println(durationDisMap);
        System.out.println("3. percent of question has greater ans than ac ans");
        double percentQuestionWithMoreVotedUnAcAns = (double) numOfQuestionWithGreatAnsThatNotAc / (double) numOfQuestionWithAcAns;
        System.out.println(percentQuestionWithMoreVotedUnAcAns);

        SolAcAnswer solAcAnswer = new SolAcAnswer(percentQuestionWithAcAns, new TreeMap<>(durationDisMap), percentQuestionWithMoreVotedUnAcAns);
        return solAcAnswer;
    }

    /*
    1. Which tags frequently appear together with the java tag?
    2. Which tags or tag combinations receive the most upvotes?
    3. Which tags or tag combinations receive the most views?
     */
    public SolTagAnswer tagsProblem() {
        Map<Set<String>, Integer> combiCntDistriMap = new HashMap<>();
        Map<Set<String>, Integer> combiVoteDistriMap = new HashMap<>();
        Map<Set<String>, Integer> combiViewDistriMap = new HashMap<>();

        JSONObject questionJsonObject = null;
        JSONArray tags = null;
        Set<Set<String>> combis = null;
        List<String> tagsLis = null;
        int viewCnt = 0;
        int upVoteCnt = 0;
        for (QuestionModel qm : questionModels) {
            questionJsonObject = new JSONObject(qm.questionJson);
            tags = questionJsonObject.getJSONArray("tags");
            tagsLis = new ArrayList<>();
            for (int i=0;i<tags.length();i++){
                tagsLis.add(tags.optString(i));
            }
            combis = getTagsCombi(tagsLis, null, null, 0);

            viewCnt = questionJsonObject.getInt("view_count");
            upVoteCnt = questionJsonObject.getInt("up_vote_count");

            for (Set<String> combi : combis) {
                if (!combi.contains("java")){
                    combiCntDistriMap.put(combi, combiCntDistriMap.getOrDefault(combi, 0) + 1);
                    combiVoteDistriMap.put(combi, combiVoteDistriMap.getOrDefault(combi, 0) + upVoteCnt);
                    combiViewDistriMap.put(combi, combiViewDistriMap.getOrDefault(combi, 0) + viewCnt);
                }
            }
        }

        int limitCnt = 20;

        System.out.println("1. frequency of combi");
//        System.out.println(combiCntDistriMap);
        combiCntDistriMap.entrySet().stream().sorted((e1, e2) -> e2.getValue()-e1.getValue()).limit(limitCnt).forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
        List<Map.Entry<Set<String>, Integer>> combiCntList =  combiCntDistriMap
                .entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue()-e1.getValue())
                .limit(limitCnt)
                .collect(Collectors.toList());

        System.out.println("2. vote of combi");
//        System.out.println(combiVoteDistriMap);
        combiVoteDistriMap.entrySet().stream().sorted((e1, e2) -> e2.getValue()-e1.getValue()).limit(limitCnt).forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
        List<Map.Entry<Set<String>, Integer>> combiVoteList = combiVoteDistriMap
                .entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue()-e1.getValue())
                .limit(limitCnt).
                collect(Collectors.toList());

        System.out.println("3. view of combi");
//        System.out.println(combiViewDistriMap);
        combiViewDistriMap.entrySet().stream().sorted((e1, e2) -> e2.getValue()-e1.getValue()).limit(limitCnt).forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
        List<Map.Entry<Set<String>, Integer>> combiViewList =  combiViewDistriMap
                .entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue()-e1.getValue())
                .limit(limitCnt)
                .collect(Collectors.toList());
        SolTagAnswer solTagAnswer = new SolTagAnswer(combiCntList, combiVoteList, combiViewList);
        return solTagAnswer;
    }


    /*
    1. the number of distinct users who post the question, answers, or comments in a thread
    2. Which are the most active users who frequently participate in thread discussions
     */
    public SolUserAnswer userProblem() {
        List<StExThread> threadLis = threadList;
        System.out.println(threadLis.size());
        Map<Integer, Integer> userCntThreadDistriMap = new HashMap<>();
        Map<Long, Integer> userActivityDistriMap = new HashMap<>();


        for (StExThread th : threadLis) {
            Set<Long> userIdSet = new HashSet<>();
            JSONObject quesJsonObject = new JSONObject(th.question.questionJson);
            JSONObject userFq = quesJsonObject.getJSONObject("owner");
            if (userFq.has("user_id")){
                long userFqId = userFq.getLong("user_id");
                userActivityDistriMap.put(userFqId, userActivityDistriMap.getOrDefault(userFqId, 0) + 1);
                userIdSet.add(userFqId);
            }

            for (CommentModel cm : th.commentsForquestion) {
                JSONObject cmJsonObject = new JSONObject(cm.commentJson);
                JSONObject userFqc = cmJsonObject.getJSONObject("owner");
                if (userFqc.has("user_id")) {
                    long userFqcId = userFqc.getLong("user_id");
                    userActivityDistriMap.put(userFqcId, userActivityDistriMap.getOrDefault(userFqcId, 0) + 1);
                    userIdSet.add(userFqcId);
                }
            }


            for (AnswerModel am : th.answers) {
                JSONObject amJsonObject = new JSONObject(am.answerJson);
                JSONObject userFa = amJsonObject.getJSONObject("owner");
                if (userFa.has("user_id")) {
                    long userFaId = userFa.getLong("user_id");
                    userActivityDistriMap.put(userFaId, userActivityDistriMap.getOrDefault(userFaId, 0) + 1);
                    userIdSet.add(userFaId);
                }
                for (CommentModel cm : th.answerId2Comments.get(am.answerId)) {
                    JSONObject cmJsonObject = new JSONObject(cm.commentJson);
                    JSONObject userFac = cmJsonObject.getJSONObject("owner");
                    if (userFac.has("user_id")){
                        long userFacId = userFac.getLong("user_id");
                        userActivityDistriMap.put(userFacId, userActivityDistriMap.getOrDefault(userFacId, 0) + 1);
                        userIdSet.add(userFacId);
                    }
                }
            }

            int userCntOfTheThread = userIdSet.size()==0 ? 1 : userIdSet.size();
            userCntThreadDistriMap.put(userCntOfTheThread, userCntThreadDistriMap.getOrDefault(userCntOfTheThread, 0) + 1);
        }

        int limitCnt = 20;

        System.out.println("user number in a thread");
        System.out.println(userCntThreadDistriMap);
        System.out.println("most active user");
        userActivityDistriMap.entrySet().stream().sorted((e1, e2) -> e2.getValue()-e1.getValue()).limit(limitCnt).forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
        List<Map.Entry<Long, Integer>> activeUserList = userActivityDistriMap
                .entrySet()
                .stream()
                .filter(e1 -> e1.getKey()!=-1)
                .sorted((e1, e2) -> e2.getValue()-e1.getValue())
                .limit(limitCnt)
                .collect(Collectors.toList());

        userCntThreadDistriMap = userCntThreadDistriMap.entrySet().stream().filter(entry -> entry.getKey()!=0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));

        SolUserAnswer solUserAnswer = new SolUserAnswer(new TreeMap<>(userCntThreadDistriMap), activeUserList);
        return solUserAnswer;
    }


    public List<StExThread> buildThreadLis() {
        List<StExThread> to_return = new ArrayList<>();
        for (QuestionModel qm : questionModels) {
            StExThread stExThread = new StExThread();
            stExThread.question = qm;
            List<AnswerModel> ams = answerModels.stream().filter(am -> am.questionId==qm.questionId).collect(Collectors.toList());
            stExThread.answers = ams;
            List<CommentModel> cmsFq = commentModels.stream().filter(cm -> cm.postId==qm.questionId).collect(Collectors.toList());
            stExThread.commentsForquestion = cmsFq;

            Map<Long, List<CommentModel>> answerId2Comments = new HashMap<>();
            for (AnswerModel am : answerModels) {
                List<CommentModel> cmsFa = commentModels.stream().filter(cm -> cm.postId==am.answerId).collect(Collectors.toList());
                answerId2Comments.put(am.answerId, cmsFa);
            }
            stExThread.answerId2Comments = answerId2Comments;

            to_return.add(stExThread);
        }
        return to_return;
    }


    public List<StExThread> buildThreadLisOnce() {
        List<StExThread> to_return = new ArrayList<>();
        for (QuestionModel qm : questionModels) {
            StExThread stExThread = new StExThread();
            stExThread.question = qm;
            List<AnswerModel> ams = answerModels.stream().filter(am -> am.questionId==qm.questionId).collect(Collectors.toList());
            stExThread.answers = ams;
            List<CommentModel> cmsFq = commentModels.stream().filter(cm -> cm.postId==qm.questionId).collect(Collectors.toList());
            stExThread.commentsForquestion = cmsFq;

            Map<Long, List<CommentModel>> answerId2Comments = new HashMap<>();
            for (AnswerModel am : answerModels) {
                List<CommentModel> cmsFa = commentModels.stream().filter(cm -> cm.postId==am.answerId).collect(Collectors.toList());
                answerId2Comments.put(am.answerId, cmsFa);
            }
            stExThread.answerId2Comments = answerId2Comments;

            to_return.add(stExThread);
            break;
        }
        return to_return;
    }

    public void testTh2JsonAndBack() {
        List<StExThread> ths = buildThreadLisOnce();
        Gson gson = new Gson();
        StExThread th = ths.get(0);
        String thJson = gson.toJson(th);
        System.out.println(thJson);
        StExThread thBack = gson.fromJson(thJson, StExThread.class);
        System.out.println(thBack);
        System.out.println("===================");
        System.out.println(thBack.question.questionJson);
        System.out.println("===================");
    }




    public static Set<Set<String>> getTagsCombi (List<String> tags, Set<Set<String>> storeAnsPlace, Set<String> formingAns, int handleInd) {
        if (handleInd==0) {
            storeAnsPlace = new HashSet<>();
            formingAns = new HashSet<>();
        }

        if (handleInd==tags.size()-1) {
            if (formingAns.size()>0) {
                storeAnsPlace.add(new HashSet<>(formingAns));
            }
            formingAns.add(tags.get(handleInd));
            storeAnsPlace.add(new HashSet<>(formingAns));
            formingAns.remove(tags.get(handleInd));
        } else {
            getTagsCombi(tags, storeAnsPlace, formingAns, handleInd+1);
            formingAns.add(tags.get(handleInd));
            getTagsCombi(tags, storeAnsPlace, formingAns, handleInd+1);
            formingAns.remove(tags.get(handleInd));
        }


        return storeAnsPlace;
    }
}
