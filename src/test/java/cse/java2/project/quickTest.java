package cse.java2.project;

import com.google.code.stackexchange.schema.Answer;
import com.google.gson.Gson;

public class quickTest {
    public static void main(String[] args) {
        String jsonString = "{\"owner\":{\"profile_image\":\"https://www.gravatar.com/avatar/cc3a1166ac3f2a34076b32c4ccb4bcab?s=256&d=identicon&r=PG&f=y&so-version=2\",\"account_id\":17810393,\"user_type\":\"registered\",\"user_id\":12935154,\"link\":\"https://stackoverflow.com/users/12935154/samir-ahmane\",\"reputation\":760,\"display_name\":\"Samir Ahmane\"},\"content_license\":\"CC BY-SA 4.0\",\"score\":0,\"is_accepted\":false,\"last_activity_date\":1672244801,\"creation_date\":1672244801,\"answer_id\":74942646,\"question_id\":74939429}";
        Gson gson = new Gson();
        Answer ans = gson.fromJson(jsonString, Answer.class);
        System.out.println(ans.getQuestionId());
    }



}
