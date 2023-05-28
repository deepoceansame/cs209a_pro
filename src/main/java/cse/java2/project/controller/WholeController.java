package cse.java2.project.controller;

import cse.java2.project.model.SolAcAnswer;
import cse.java2.project.model.SolNumAnswer;
import cse.java2.project.model.SolTagAnswer;
import cse.java2.project.model.SolUserAnswer;
import cse.java2.project.service.WholeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WholeController {

    private final WholeService wholeService;

    public WholeController(WholeService wholeService) {
        this.wholeService = wholeService;
    }

    @GetMapping({"/", "/index"})
    public String answerAllQuestion(Model model) {

//        percentQuestionWithoutAns
//        averageAnsCnt
//        maxAnsCnt
//        ansCntDisMap
        SolNumAnswer solNumAnswer = wholeService.numberOfAnswer();
        model.addAttribute("solNumAnswer", solNumAnswer);
        SolAcAnswer solAcAnswer = wholeService.acceptedAnswer();
        model.addAttribute("solAcAnswer", solAcAnswer);
        SolTagAnswer solTagAnswer = wholeService.tagsProblem();
        model.addAttribute("solTagAnswer", solTagAnswer);
        SolUserAnswer solUserAnswer = wholeService.userProblem();
        model.addAttribute("solUserAnswer", solUserAnswer);

        return "index";
    }
}
