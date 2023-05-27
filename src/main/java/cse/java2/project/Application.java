package cse.java2.project;


import cse.java2.project.service.WholeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.text.ParseException;

/**
 * This is the main class of the Spring Boot application.
 */
@SpringBootApplication
public class Application {
    public static ConfigurableApplicationContext ac;
    public static void main(String[] args) throws ParseException, IOException {
        ac = SpringApplication.run(Application.class, args);
//        GrabScri grabScri = ac.getBean(GrabScri.class);
//        grabScri.getQuestion();
//        grabScri.getAnswersForEachQuestionCollected();
//        grabScri.seeQuestionsCollected();
//        grabScri.seeAnswersCollected();
//        grabScri.getCommentsForCollectedQuestions();
//        grabScri.getCommentsForCollectedAnswers();
//        grabScri.getQuestionsAndAnswers();
//        grabScri.seeCommentsCollected();

        WholeService ser = ac.getBean(WholeService.class);
//        ser.numberOfAnswer();
        ser.acceptedAnswer();



    }

}
