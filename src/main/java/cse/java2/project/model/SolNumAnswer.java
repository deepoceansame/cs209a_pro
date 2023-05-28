package cse.java2.project.model;

import java.util.HashMap;
import java.util.Map;

public class SolNumAnswer {
    public double percentQuestionWithoutAns;

    public double averageAnsCnt;

    public double maxAnsCnt;

    public Map<Integer, Integer> ansCntDisMap;

    public SolNumAnswer(double percentQuestionWithoutAns, double averageAnsCnt, double maxAnsCnt, Map<Integer, Integer> ansCntDisMap) {
        this.percentQuestionWithoutAns = percentQuestionWithoutAns;
        this.averageAnsCnt = averageAnsCnt;
        this.maxAnsCnt = maxAnsCnt;
        this.ansCntDisMap = ansCntDisMap;
    }
}
