package cse.java2.project.model;



import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SolUserAnswer {
    public Map<Integer, Integer> userCntThreadDistriMap;

    public List<Map.Entry<Long, Integer>> activeUserList;
    public Map<Long, Integer> activeUserMap = new LinkedHashMap<>();

    public SolUserAnswer(Map<Integer, Integer> userCntThreadDistriMap, List<Map.Entry<Long, Integer>> activeUserList) {
        this.userCntThreadDistriMap = userCntThreadDistriMap;
        this.activeUserList = activeUserList;
        for (Map.Entry<Long, Integer> en : activeUserList) {
            activeUserMap.put(en.getKey(), en.getValue());
        }
    }
}
