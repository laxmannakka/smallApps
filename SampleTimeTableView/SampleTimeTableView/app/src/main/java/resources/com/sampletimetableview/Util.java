package resources.com.sampletimetableview;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by suresh on 04/02/17.
 */

public class Util
{
    public static int getCellHeight(String startTime, String endTime)
    {

        Date startDate = null;
        Date endDate = null;
        DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm");

        try
        {
            startDate = sdf.parse(startTime);
            endDate = sdf.parse(endTime);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        int diffMs = (int) (endDate.getTime() - startDate.getTime());
        int diffSec = diffMs / 1000;
        int min = diffSec / 60;
        System.out.println("minutes======>"+min);

        return min;
    }
}
