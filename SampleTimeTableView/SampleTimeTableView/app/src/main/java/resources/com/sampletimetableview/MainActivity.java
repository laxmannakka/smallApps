package resources.com.sampletimetableview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity
{
   private LinearLayout mainLayout;
   private RelativeLayout layout1;
   private RelativeLayout layout2;
   private RelativeLayout layout3;
   private RelativeLayout layout4;
   private RelativeLayout layout5;
   private RelativeLayout layout6;

    private RelativeLayout layout11;
    private RelativeLayout layout12;
    private RelativeLayout layout13;
    private RelativeLayout layout14;
    private RelativeLayout layout15;
    private RelativeLayout layout16;
   private TimeObject data;
   private LinkedHashMap<Integer,ArrayList<TimeObject>> map = new LinkedHashMap<>();
   private LinkedHashMap<Integer,ArrayList<TimeObject>> dayMap = new LinkedHashMap<>();

   private LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initilizeLayouts();
        ArrayList<TimeObject> timeObjects = new ArrayList<>();
        timeObjects.add(new TimeObject("2017-01-15 9:00","2017-01-15 10:00","English",0));
        timeObjects.add(new TimeObject("2017-01-15 10:00","2017-01-15 11:00","Physics",0));
        timeObjects.add(new TimeObject("2017-01-15 11:00","2017-01-15 12:00","Science",0));
        timeObjects.add(new TimeObject("2017-01-15 12:00","2017-01-15 13:00","Social",0));
        timeObjects.add(new TimeObject("2017-01-15 13:00","2017-01-15 14:00","Maths",0));
        timeObjects.add(new TimeObject("2017-01-15 14:00","2017-01-15 15:00","History",0));
        map.put(0,timeObjects);
         map.put(1,timeObjects);
         map.put(2,timeObjects);
       map.put(3,timeObjects);
//        map.put(4,timeObjects);
//        map.put(5,timeObjects);

        Iterator mainiterator = map.keySet().iterator();
        while (mainiterator.hasNext())
        {
            int key = (int) mainiterator.next();
            ArrayList<TimeObject> objects = map.get(key);
            int count = 1;
            int mainid = key + 1;
            for(TimeObject timeObject:objects)
            {
                formLayout(timeObject,count,mainid);
                count++;
            }

        }



        ArrayList<TimeObject> firstObjects = new ArrayList<>();
        firstObjects.add(new TimeObject("2017-01-15 9:00","2017-01-15 10:30","English",0));
        firstObjects.add(new TimeObject("2017-01-15 10:40","2017-01-15 12:30","Physics",0));
        firstObjects.add(new TimeObject("2017-01-15 14:40","2017-01-15 14:50","Physics",0));
        //firstObjects.add(new TimeObject("2017-01-15 10:45","2017-01-15 11:00","English",0));
       // ArrayList<TimeObject> secondObjects = new ArrayList<>();
       // secondObjects.add(new TimeObject("2017-01-15 10:00","2017-01-15 11:00","Physics",0));
        dayMap.put(1,firstObjects);
       // dayMap.put(2,secondObjects);

        Iterator iterator = dayMap.keySet().iterator();
        while (iterator.hasNext())
        {
            int key = (int) iterator.next();
            ArrayList<TimeObject> list = dayMap.get(key);
            for(TimeObject object:list)
            {
              formMainLayout(key,object);
            }
        }

    }

    private void formMainLayout(int key, TimeObject timeObject)
    {
        ViewGroup mainView = (ViewGroup) findViewById(10+key);
        View view = inflater.inflate(R.layout.note_view,null);
        view.setId(key+20);
        TextView textView= (TextView) view.findViewById(R.id.subject_txt);
         textView.setText(timeObject.getSubject());
        float density = getResources().getDisplayMetrics().density;
        float dp = timeObject.getHeight() * density;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) dp);

          int margin = Util.getCellHeight("2017-01-15 9:00",timeObject.getStartTime());
         float maindp = margin * density;
          params.setMargins(0, (int) maindp,0,0);

        view.setLayoutParams(params);
        mainView.addView(view);

    }

    private void formLayout(TimeObject timeObject, int count, int mainid)
    {
       View view = inflater.inflate(R.layout.custom_view,null);
        //TextView textView= (TextView) view.findViewById(R.id.subject_txt);
        // textView.setText(timeObject.getSubject());
        float density = getResources().getDisplayMetrics().density;
        float dp = timeObject.getHeight() * density;
        view.setId(count);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) dp);

        if(view.getId() != 1)
        {
            View previousView = findViewById(view.getId()-1);
            params.addRule(RelativeLayout.BELOW,previousView.getId());
        }
        params.setMargins(0,0,3,0);
        view.setLayoutParams(params);

        ViewGroup layout = (ViewGroup) findViewById(40+mainid);

        if(layout!=null)
        {
            layout.addView(view);
        }


    }

    private void initilizeLayouts()
    {
        layout1= (RelativeLayout) findViewById(R.id.monday_layout);
        layout1.setId(41);
        layout2= (RelativeLayout) findViewById(R.id.tuesday_layout);
        layout2.setId(42);
        layout3= (RelativeLayout) findViewById(R.id.wednesday_layout);
        layout3.setId(43);
        layout4= (RelativeLayout) findViewById(R.id.thursday_layout);
        layout4.setId(44);
        layout5= (RelativeLayout) findViewById(R.id.friday_layout);
        layout5.setId(45);
        layout6= (RelativeLayout) findViewById(R.id.saturday_layout);
        layout6.setId(46);

        layout11= (RelativeLayout) findViewById(R.id.smonday_layout);
        layout11.setId(11);
        layout12= (RelativeLayout) findViewById(R.id.stuesday_layout);
        layout12.setId(12);
        layout13= (RelativeLayout) findViewById(R.id.swednesday_layout);
        layout13.setId(13);
        layout14= (RelativeLayout) findViewById(R.id.sthursday_layout);
        layout14.setId(14);
        layout15= (RelativeLayout) findViewById(R.id.sfriday_layout);
        layout15.setId(15);
        layout16= (RelativeLayout) findViewById(R.id.ssaturday_layout);
        layout16.setId(16);

    }
    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
    private int getTotalMargin(String time)
    {
        Date startDate = null;
        Date endDate = null;
        DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm");

        return 0;
    }
}
