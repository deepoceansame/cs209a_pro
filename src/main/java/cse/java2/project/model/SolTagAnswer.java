package cse.java2.project.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolTagAnswer {
    public List<Map.Entry<Set<String>, Integer>> combiCntList;
    public List<Map.Entry<Set<String>, Integer>> combiVoteList;
    public List<Map.Entry<Set<String>, Integer>> combiViewList;

    public Map<Set<String>, Integer> combiCntMap = new LinkedHashMap<>();
    public Map<Set<String>, Integer> combiVoteMap = new LinkedHashMap<>();
    public Map<Set<String>, Integer> combiViewMap = new LinkedHashMap<>();

    public SolTagAnswer(List<Map.Entry<Set<String>, Integer>> combiCntList, List<Map.Entry<Set<String>, Integer>> combiVoteList, List<Map.Entry<Set<String>, Integer>> combiViewList) {
        this.combiCntList = combiCntList;
        this.combiVoteList = combiVoteList;
        this.combiViewList = combiViewList;

        for (Map.Entry<Set<String>, Integer> en : combiCntList) {
            combiCntMap.put(en.getKey(), en.getValue());
        }

        for (Map.Entry<Set<String>, Integer> en : combiVoteList) {
            combiVoteMap.put(en.getKey(), en.getValue());
        }

        for (Map.Entry<Set<String>, Integer> en : combiViewList) {
            combiViewMap.put(en.getKey(), en.getValue());
        }
    }
}
