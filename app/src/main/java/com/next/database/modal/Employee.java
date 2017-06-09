package com.next.database.modal;

/**
 * Created by next on 24/3/17.
 */
public class Employee
{
    int departMentId;
    String employeeName;
    String joiningDate;
    String mobileNumber;
    int empId;

    public Employee()
    {
    }

    public Employee(int departMentId, String employeeName, String joiningDate, String mobileNumber, int empId)
    {
        this.departMentId = departMentId;
        this.employeeName = employeeName;
        this.joiningDate = joiningDate;
        this.mobileNumber = mobileNumber;
        this.empId = empId;
    }

    public int getEmpId()
    {
        return empId;
    }

    public void setEmpId(int empId)
    {
        this.empId = empId;
    }

    public String getMobileNumber()
    {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }



    public String getEmployeeName()
    {
        return employeeName;
    }

    public void setEmployeeName(String employeeName)
    {
        this.employeeName = employeeName;
    }

    public String getJoiningDate()
    {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate)
    {
        this.joiningDate = joiningDate;
    }
    public int getDepartMentId()
    {
        return departMentId;
    }

    public void setDepartMentId(int departMentId)
    {
        this.departMentId = departMentId;
    }
}
