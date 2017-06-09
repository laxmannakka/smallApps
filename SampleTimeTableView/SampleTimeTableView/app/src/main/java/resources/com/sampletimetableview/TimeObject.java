package resources.com.sampletimetableview;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by suresh on 04/02/17.
 */
public class TimeObject
{
    private String startTime;
    private String endTime;
    private String subject;
    private int height;

    public TimeObject(String startTime,String endTime,String subject,int height)
    {
       this.startTime = startTime;
        this.endTime = endTime;
        this.subject = subject;
        this.height = Util.getCellHeight(startTime,endTime);

    }


    public int getHeight()
    {
        return height;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public String getSubject()
    {
        return subject;
    }
}
