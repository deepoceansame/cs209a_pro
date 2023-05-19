package cse.java2.project;

import com.google.code.stackexchange.client.query.AnswerApiQuery;
import com.google.code.stackexchange.client.query.QuestionApiQuery;
import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import cse.java2.project.Repository.AnswerModelRepository;
import cse.java2.project.Repository.QuestionModelRepository;
import cse.java2.project.model.AnswerModel;
import cse.java2.project.model.QuestionModel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Component
public class GrabScri {
    public static final String APP_KEY = "OzEDe9HvmTuTIQ7np0nV4A((";
    public static final String SITE = "stackoverflow";

    private final QuestionModelRepository questionModelRepository;
    private final AnswerModelRepository answerModelRepository;

    @Autowired
    public GrabScri(QuestionModelRepository questionModelRepository, AnswerModelRepository answerModelRepository) {
        this.questionModelRepository = questionModelRepository;
        this.answerModelRepository = answerModelRepository;
    }

    public static void main(String[] args) throws ParseException {

    }

    /*
        Want to get 520 questions from 2022-01-01 to 2023-01-01 with tag of java
        27 60
    */
    public void getQuestion() throws ParseException {

        // prepare to date
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String fromDateString = "1-Jan-2022";
        Date fromDate = formatter.parse(fromDateString);

        String toDateString = "1-Jan-2023";
        Date toDate = formatter.parse(toDateString);

        // let stackexchange sdk help
        StackExchangeApiQueryFactory queryFactory = StackExchangeApiQueryFactory.newInstance(APP_KEY, StackExchangeSite.STACK_OVERFLOW);
        QuestionApiQuery query = queryFactory.newQuestionApiQuery();
        PagedList<Question> questions = query
                .withSort(Question.SortOrder.MOST_RECENTLY_CREATED)
                .withPaging(new Paging(28, 20))
                .withTimePeriod(new TimePeriod(fromDate, toDate))
                .withTags("java")
                .withFilter("default")
                .list();

        // restore collected questions
        while (questions.hasMore()) {
            questions = query.withPaging(new Paging(questions.getPage() + 1, questions.getPageSize())).list();
            Gson gson = new Gson();
            for (Question q : questions) {
                System.out.println(q.getTitle() + ":" + q.getTags() + ":" + q.getAnswerCount());
                System.out.println(gson.toJson(q));
                QuestionModel questionModel = new QuestionModel(q.getQuestionId(), gson.toJson(q));
                if (!questionModelRepository.existsById(questionModel.questionId ) && (questionModel.questionId!=0) ) {
                    questionModelRepository.save(questionModel);
                }
            }
            if (questions.getPage() > 60) {
                break;
            }
        }
//        Gson gson = new Gson();
//        for (Question q:questions){
//            System.out.println(q.getTitle()+":"+q.getTags()+":"+q.getAnswerCount());
//            System.out.println(gson.toJson(q));
//            QuestionModel questionModel = new QuestionModel(q.getQuestionId(), gson.toJson(q));
//            questionModelRepository.save(questionModel);
//        }
    }

    public void seeQuestionCollected() {
        Gson gson = new Gson();
        List<QuestionModel> questionModels = questionModelRepository.findAll();
        for (QuestionModel qm : questionModels) {
            long questionId = qm.questionId;
            String questionJson = qm.questionJson;
//            System.out.println(questionJson.length());
            Question question = gson.fromJson(questionJson, Question.class);
            System.out.println(question.getQuestionId() + ":" + question.getTags() + ":" + question.getTitle());
            System.out.println(question.getOwner().getUserId());
        }
    }


    public void getAnswersForEachQuestionCollected() throws IOException {
        Gson gson = new Gson();
        List<QuestionModel> questionModels = questionModelRepository.findAll();
        String BASE_URI = "https://api.stackexchange.com/2.3/questions";
        String getAnswer_URI = "";
        for(QuestionModel qm : questionModels){

//            ExampleMatcher modelMatcher = ExampleMatcher.matching()
//                    .withIgnorePaths("answerId")
//                    .withMatcher("questionId", exact());
//            AnswerModel probe = new AnswerModel();
//            probe.questionId = qm.questionId;
//            Example<AnswerModel> example = Example.of(probe, modelMatcher);
//            if (answerModelRepository.exists(example)){
//                System.out.println("the question is done");
//                break;
//            }

            if (answerModelRepository.existsAnswerModelByQuestionId(qm.questionId)){
                System.out.println("the question with" + qm.questionId + " has finished answer finding");
                continue;
            }

            // for each question, build uri
            getAnswer_URI = BASE_URI + "/" + qm.questionId + "/answers";
            URI uri = UriComponentsBuilder.fromUriString(getAnswer_URI)
                    .queryParam("key", APP_KEY)
                    .queryParam("site", "stackoverflow")
                    .queryParam("page", 1)
                    .queryParam("pagesize", 30)
                    .build().toUri();

            // send request and get response
            URL url = new URL(uri.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=utf-8");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new GZIPInputStream(connection.getInputStream()), StandardCharsets.UTF_8)
            );
            String response = reader.lines().reduce("", String::concat);
            reader.close();

            JSONObject respJsonObject = new JSONObject(response);
            JSONArray items = respJsonObject.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                System.out.println("------item-------");
                System.out.println(item);
                System.out.println("------item-------");
                AnswerModel answerModel = new AnswerModel();
                answerModel.answerId = item.getLong("answer_id");
                answerModel.questionId = item.getLong("question_id");
                answerModel.answerJson = item.toString();
                if (!answerModelRepository.existsById(answerModel.answerId) && (answerModel.answerId!=0)){
                    answerModelRepository.save(answerModel);
                }

            }

            boolean hasMore = respJsonObject.getBoolean("has_more");
            int quota_max = respJsonObject.getInt("quota_max");
            int quota_remaining = respJsonObject.getInt("quota_remaining");
            System.out.println(hasMore+";"+quota_max+";"+quota_remaining);

            int page = 2;
            while (hasMore) {
                uri = UriComponentsBuilder.fromUriString(getAnswer_URI)
                        .queryParam("key", APP_KEY)
                        .queryParam("site", "stackoverflow")
                        .queryParam("page", page)
                        .queryParam("pagesize", 30)
                        .build().toUri();

                // send request and get response
                url = new URL(uri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=utf-8");

                reader = new BufferedReader(
                        new InputStreamReader(new GZIPInputStream(connection.getInputStream()), StandardCharsets.UTF_8)
                );
                response = reader.lines().reduce("", String::concat);
                reader.close();

                respJsonObject = new JSONObject(response);
                items = respJsonObject.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    System.out.println("------item-------");
                    System.out.println(item);
                    System.out.println("------item-------");
                    AnswerModel answerModel = new AnswerModel();
                    answerModel.answerId = item.getLong("answer_id");
                    answerModel.questionId = item.getLong("question_id");
                    answerModel.answerJson = item.toString();
                    if (!answerModelRepository.existsById(answerModel.answerId) && (answerModel.answerId!=0)){
                        answerModelRepository.save(answerModel);
                    }
                }

                hasMore = respJsonObject.getBoolean("has_more");
                quota_max = respJsonObject.getInt("quota_max");
                quota_remaining = respJsonObject.getInt("quota_remaining");
                System.out.println(hasMore+";"+quota_max+";"+quota_remaining);
                page += 1;
            }
        }
    }

    public void seeAnswersCollected() throws IOException {
        List<AnswerModel> answerModelList = answerModelRepository.findAll();
        for (AnswerModel answerModel : answerModelList) {
            boolean questionIDExistINquestionTable = false;
            long questionId = answerModel.questionId;
            questionIDExistINquestionTable = questionModelRepository.existsById(questionId);
            if (!questionIDExistINquestionTable){
                System.out.println("warn");
                throw  new IOException();
            }
        }
        for (AnswerModel answerModel : answerModelList) {
            System.out.println(answerModel.answerJson.length());
        }
    }

    public void getAnswerWithId() {
        long answerId = 74942646;
        System.out.println(answerId);
        StackExchangeApiQueryFactory queryFactory = StackExchangeApiQueryFactory.newInstance(APP_KEY, StackExchangeSite.STACK_OVERFLOW);
        AnswerApiQuery query = queryFactory.newAnswerApiQuery();
        PagedList<Answer> answers = query.withAnswerIds(answerId).withFilter("default").list();
        for (Answer answer : answers){
            System.out.println(answer.getQuestionId()+":"+answer.getAnswerId());
        }
    }
}
