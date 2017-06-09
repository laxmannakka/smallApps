package com.next.matchthefollowing;

import java.util.Map;

/**
 * Created by next on 6/5/17.
 */
public class Question
{
    int q_id;
    String question;

    public Question(int q_id, String question)
    {
        this.q_id = q_id;
        this.question = question;
    }

    public int getQ_id()
    {
        return q_id;
    }

    public void setQ_id(int q_id)
    {
        this.q_id = q_id;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }
}

