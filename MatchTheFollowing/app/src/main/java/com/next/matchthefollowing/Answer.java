package com.next.matchthefollowing;

import java.util.Map;

/**
 * Created by next on 6/5/17.
 */
public class Answer
{
    String answer;
    int ans_id;
    int keyset; // correct answer

    public Answer(String answer, int ans_id, int keyset)
    {
        this.answer = answer;
        this.ans_id = ans_id;
        this.keyset = keyset;
    }

    public int getKeyset()
    {
        return keyset;
    }

    public void setKeyset(int keyset)
    {
        this.keyset = keyset;
    }

    public Answer(String answer, int ans_id)
    {
        this.answer = answer;
        this.ans_id = ans_id;
    }

    public int getAns_id()
    {
        return ans_id;
    }

    public void setAns_id(int ans_id)
    {
        this.ans_id = ans_id;
    }

    public String getAnswer()
    {

        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }
}
