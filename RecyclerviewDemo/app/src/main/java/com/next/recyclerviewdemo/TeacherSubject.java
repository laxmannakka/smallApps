package com.next.recyclerviewdemo;

/**
 * Created by next on 17/3/17.
 */
public class TeacherSubject
{
    String name;
    String section;
    boolean checkstatus;
    String  marks;

    public String getMarks()
    {
        return marks;
    }

    public void setMarks(String marks)
    {
        this.marks = marks;
    }

    public boolean isCheckstatus()
    {
        return checkstatus;
    }

    public void setCheckstatus(boolean checkstatus)
    {
        this.checkstatus = checkstatus;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSection()
    {
        return section;
    }

    public void setSection(String section)
    {
        this.section = section;
    }
}
