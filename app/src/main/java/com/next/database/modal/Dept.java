package com.next.database.modal;

/**
 * Created by next on 24/3/17.
 */
public class Dept
{

    int departmentId;
    String getDepartmentName;

    public Dept()
    {
    }

    public Dept(int departmentId, String getDepartmentName)
    {
        this.departmentId = departmentId;
        this.getDepartmentName = getDepartmentName;
    }

    public int getDepartmentId()
    {
        return departmentId;
    }

    public void setDepartmentId(int departmentId)
    {
        this.departmentId = departmentId;
    }

    public String getGetDepartmentName()
    {
        return getDepartmentName;
    }

    public void setGetDepartmentName(String getDepartmentName)
    {
        this.getDepartmentName = getDepartmentName;
    }
}
