package cse.java2.project;

import com.google.code.stackexchange.schema.Answer;
import com.google.gson.Gson;

import java.util.*;

public class quickTest {
    public static void main(String[] args) {
//        String jsonString = "{\"owner\":{\"profile_image\":\"https://www.gravatar.com/avatar/cc3a1166ac3f2a34076b32c4ccb4bcab?s=256&d=identicon&r=PG&f=y&so-version=2\",\"account_id\":17810393,\"user_type\":\"registered\",\"user_id\":12935154,\"link\":\"https://stackoverflow.com/users/12935154/samir-ahmane\",\"reputation\":760,\"display_name\":\"Samir Ahmane\"},\"content_license\":\"CC BY-SA 4.0\",\"score\":0,\"is_accepted\":false,\"last_activity_date\":1672244801,\"creation_date\":1672244801,\"answer_id\":74942646,\"question_id\":74939429}";
//        Gson gson = new Gson();
//        Answer ans = gson.fromJson(jsonString, Answer.class);
//        System.out.println(ans.getQuestionId());

        Map<Set<String>, Integer> map1 = new HashMap<>();
        Set<String> set1 = new HashSet<>();
//        List<String> set1 = new ArrayList<>();
        set1.add("b");
        set1.add("a");
        map1.put(set1, 1);

        Set<String> set2 = new HashSet<>();
        set2.add("a");
        set2.add("b");
        System.out.println(map1.get(set2));


        Map<List<String>, Integer> map2 = new HashMap<>();
        List<String> lis1 = new ArrayList<>();
//        List<String> set1 = new ArrayList<>();
        lis1.add("b");
        lis1.add("a");
        map2.put(lis1, 1);

        List<String> lis2 = new ArrayList<>();
        lis2.add("a");
        lis2.add("b");
        System.out.println(map2.get(lis2));

//        List<String> tags = new ArrayList<>();
//        tags.add("java");
//        tags.add("a");
//        tags.add("a");
//        tags.add("a");
//        System.out.println(getTagsCombi(tags, null, null, 0));
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
