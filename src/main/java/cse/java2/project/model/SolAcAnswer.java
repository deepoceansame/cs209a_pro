package cse.java2.project.model;

import java.util.HashMap;
import java.util.Map;

public class SolAcAnswer {
    public double percentQuestionWithAcAns;
    public Map<Long, Integer> durationDisMap;
    public double percentQuestionWithMoreVotedUnAcAns;

    public SolAcAnswer(double percentQuestionWithAcAns, Map<Long, Integer> durationDisMap, double percentQuestionWithMoreVotedUnAcAns) {
        this.percentQuestionWithAcAns = percentQuestionWithAcAns;
        this.durationDisMap = durationDisMap;
        this.percentQuestionWithMoreVotedUnAcAns = percentQuestionWithMoreVotedUnAcAns;
    }
}
