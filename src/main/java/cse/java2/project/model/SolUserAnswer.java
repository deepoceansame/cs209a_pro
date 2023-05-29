package cse.java2.project.model;



import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SolUserAnswer {
    public Map<Integer, Integer> userCntThreadDistriMap;
    public Map<Integer, Integer> userCntThreadDistriMap_ans;
    public Map<Integer, Integer> userCntThreadDistriMap_comment;

    public List<Map.Entry<Long, Integer>> activeUserList;
    public Map<Long, Integer> activeUserMap = new LinkedHashMap<>();

    public SolUserAnswer(Map<Integer, Integer> userCntThreadDistriMap,
                         Map<Integer, Integer> userCntThreadDistriMap_ans,
                         Map<Integer, Integer> userCntThreadDistriMap_comment,
                         List<Map.Entry<Long, Integer>> activeUserList) {
        this.userCntThreadDistriMap = userCntThreadDistriMap;
        this.userCntThreadDistriMap_ans = userCntThreadDistriMap_ans;
        this.userCntThreadDistriMap_comment = userCntThreadDistriMap_comment;
        this.activeUserList = activeUserList;
        for (Map.Entry<Long, Integer> en : activeUserList) {
            activeUserMap.put(en.getKey(), en.getValue());
        }
    }
}
