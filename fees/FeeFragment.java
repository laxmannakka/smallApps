package com.fees;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.examination.helper.AnimatedExpandableListView;
import com.fees.interfaces.PaymentListener;
import com.fees.model.Bank;
import com.fees.model.FeeDue;
import com.fees.model.FeeDueDetail;
import com.fees.model.TransactionDetails;
import com.helper.BaseFragment;
import com.helper.CustomLogger;
import com.helper.DownloadFileTask;
import com.helper.ServerRequestTask;
import com.home.HomeActivity;
import com.interfaces.RecyclerViewClickListener;
import com.interfaces.ServerRequestListener;
import com.interfaces.StudentInfoListener;
import com.personalInformation.modal.Session;
import com.resources.erp.R;
import com.utils.ERPModel;
import com.utils.ERPUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.fees.FeePagerFragment.pagerView;


public class FeeFragment extends BaseFragment implements RecyclerViewClickListener,ServerRequestListener
{

    StudentInfoListener studentInfoListener;
    List<FeeDue> feeDueList;
    public ExpandableFeeAdapter feeDueSummaryAdapter;
    ExpandableFeeChildAdapter feeDueChildAdapter;
    LinearLayout loadingLayout;
    LinearLayout listLayout;
    String currentTab;
    View view;
    RecyclerView.LayoutManager recyclerVierw_LayoutManager;
    RelativeLayout totalFeeDueLayout;
    LinearLayout paymentLayout;
    Button payNow;
    Button downloadChallan;
    Button goToBagButton;
    LinearLayout proceedLayout;
    LinearLayout errorLayout;
    TextView textError;
    public double totalFeeDue = 0.0;
    public double totalScholarShip = 0.0;
    TextView totalValueFeeDue;
    String uri;
    int previousClickedGroupPosition = -1;
    int currentClickedGroupPosition = -1;
    boolean isPayNowTitle;
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

    LinearLayout payNowButtonLayout;
    /*
      download challan variables
     */
    private RelativeLayout _fromDateLayout;
    private Spinner _monthsSpinner;
    private Spinner _bankSpinner;
    private Button _downloadChallanButton;
    private Button _cancel;
    private TextView fromDate;
    DatePickerDialog fromDatePickerDialog;

    String selectedTillDate = null;
    String selectedBankId = null;
    String selectedMonth = null;
    Date currentSessionStartDate = null;
    Date currentSessionEndDate = null;
   // public ViewGroup pagerView;
    public FeeFragment(View view)
    {
      //  this.pagerView = view;
    }
    AnimatedExpandableListView listView;
    List<String> feeMonthList = new ArrayList<>();
    Map<String, List<FeeDue>> filteredFeeList ;
    public FeeFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_fee_due_summary, container, false);

        listLayout = (LinearLayout) view.findViewById(R.id.listLayout);
        listView = (AnimatedExpandableListView) view.findViewById(R.id.listView);
        totalFeeDueLayout = (RelativeLayout) pagerView.findViewById(R.id.totalFeeDueLayout);
        paymentLayout = (LinearLayout) pagerView.findViewById(R.id.layout_dashboard_payment);
        payNowButtonLayout = (LinearLayout) pagerView.findViewById(R.id.PayNowButtonLayout);
        totalValueFeeDue = (TextView) pagerView.findViewById(R.id.value_totalFee);
        payNow = (Button) pagerView.findViewById(R.id.payNow);
        payNow.setEnabled(false);

        downloadChallan = (Button) pagerView.findViewById(R.id.download_challan);
        downloadChallan.setEnabled(false);

        goToBagButton = (Button) view.findViewById(R.id.goToBagButton);
        proceedLayout = (LinearLayout) view.findViewById(R.id.proceedLayout);

        loadingLayout = (LinearLayout) view.findViewById(R.id.loadingLayout);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        textError = (TextView) view.findViewById(R.id.error);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //studentInfoListener.fetchInfo(uri);
        new ServerRequestTask(this, ERPModel.SERVER_URL, uri, null, false, "get").execute();
        totalFeeDue = 0;
        totalScholarShip = 0;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        uri = ERPModel.parentLoggedIn.getBranch().getBranchId() + "/profile/" + ERPModel.selectedKid.getId() + "/fee";

        listLayout.setVisibility(View.GONE);

        totalFeeDueLayout.setVisibility(View.VISIBLE);
        if (ERPModel.duration_selected != null && !(ERPModel.current_duration.equals(ERPModel.duration_selected)))
        {
            paymentLayout.setVisibility(View.GONE);
        }

        if (ERPModel.updateFeedueList.equals(true))
        {
            ERPModel.updateFeedueList = false;
        }
        if (feeDueList != null)
        {
            setInfo(ERPModel.selectedKid.getFeeDueDetail().getFeeDueList());
        } else if (ERPModel.responseMap.get(uri) != null && ERPModel.responseMap.get(uri) == true)
        {
            showErrorLayout("Student Fee Due Details not found");
            payNowButtonLayout.setVisibility(View.GONE);
            totalFeeDueLayout.setVisibility(View.GONE);
        } /*else
        {
            studentInfoListener.fetchInfo(uri);
        }*/
       // payNowButtonLayout.setVisibility(View.GONE);
    }


    public void setInfo(final List<FeeDue> feeDueSummaryList)
    {
        if(ERPModel.enableFeatures.containsKey(ERPModel.featureConfig.ONLINE_PAYMENT.getFeatureNo()) && ((ERPModel.enableFeatures.containsKey(ERPModel.featureConfig.Download_Challan.getFeatureNo()) &&  ERPModel.selectedKid.getFeeDueDetail().isFeeChallan())))
        {
            payNowButtonLayout.setVisibility(View.VISIBLE);
        }
        else if(ERPModel.enableFeatures.containsKey(ERPModel.featureConfig.ONLINE_PAYMENT.getFeatureNo()))
        {
            payNowButtonLayout.setVisibility(View.VISIBLE);
            payNow.setVisibility(View.VISIBLE);
            downloadChallan.setVisibility(View.GONE);
        }
        else if(ERPModel.enableFeatures.containsKey(ERPModel.featureConfig.Download_Challan.getFeatureNo()) && ERPModel.selectedKid.getFeeDueDetail().isFeeChallan())
        {
            payNowButtonLayout.setVisibility(View.VISIBLE);
            payNow.setVisibility(View.GONE);
            downloadChallan.setVisibility(View.VISIBLE);
        }



//        if (ERPModel.enableFeatures.containsKey(ERPModel.featureConfig.ONLINE_PAYMENT.getFeatureNo()))
//        {
//            paymentLayout.setVisibility(View.VISIBLE);
//            payNowButtonLayout.setVisibility(View.VISIBLE);
//            payNow.setVisibility(View.VISIBLE);
//
//        }
//        if (ERPModel.enableFeatures.containsKey(ERPModel.featureConfig.Download_Challan.getFeatureNo()))
//        {
//            paymentLayout.setVisibility(View.VISIBLE);
//            payNowButtonLayout.setVisibility(View.VISIBLE);
//            downloadChallan.setVisibility(View.VISIBLE);
//        }else{
//            paymentLayout.setVisibility(View.VISIBLE);
//            downloadChallan.setVisibility(View.GONE);
//        }
//
//        if(ERPModel.enableFeatures.containsKey(ERPModel.featureConfig.ONLINE_PAYMENT.getFeatureNo()) && !ERPModel.enableFeatures.containsKey(ERPModel.featureConfig.Download_Challan.getFeatureNo())){
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
//            payNow.setLayoutParams(params);
//        }else if(!ERPModel.enableFeatures.containsKey(ERPModel.featureConfig.ONLINE_PAYMENT.getFeatureNo()) && ERPModel.enableFeatures.containsKey(ERPModel.featureConfig.Download_Challan.getFeatureNo())){
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
//            downloadChallan.setLayoutParams(params);
//        }

//
//        if(ERPModel.selectedKid.getFeeDueDetail().isFeeChallan()
//                && ERPModel.enableFeatures.containsKey(ERPModel.featureConfig.Download_Challan.getFeatureNo())) {
//            paymentLayout.setVisibility(View.VISIBLE);
//            downloadChallan.setVisibility(View.VISIBLE);
//            totalFeeDueLayout.setVisibility(View.VISIBLE);
//        }else if(ERPModel.enableFeatures.containsKey(ERPModel.featureConfig.ONLINE_PAYMENT.getFeatureNo())){
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            payNow.setLayoutParams(params);
//        }

        filteredFeeList = putFeeDueListInMap(feeDueSummaryList);
        List<String> monthslist = feeMonthList;

        feeDueSummaryAdapter = new ExpandableFeeAdapter(getActivity(),false);
        feeDueSummaryAdapter.setData(monthslist);
        listView.setAdapter(feeDueSummaryAdapter);
        listLayout.setVisibility(View.VISIBLE);
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int position, long id) {
                currentClickedGroupPosition = position;
                String monthKey = feeMonthList.get(position);
                List<FeeDue> feeDueList = filteredFeeList.get(monthKey);

                // check is previous group is expanded or not
                if (listView.isGroupExpanded(previousClickedGroupPosition)) {
                    listView.collapseGroupWithAnimation(previousClickedGroupPosition);
                    previousClickedGroupPosition = -1;
                }

                // expand or fetch the list if previous and current position are not equal
                // and if current clicked position is not expanded.
                if (previousClickedGroupPosition != currentClickedGroupPosition &&
                        !(listView.isGroupExpanded(currentClickedGroupPosition))) {
                    // check if list is already in map or not
                    if (feeDueList != null) {
                        listView.setSelectedGroup(0);
                        expandChildList(feeDueList);
                    }
                }
                listView.setSelectedGroup(currentClickedGroupPosition);
                return true;
            }
        });




        loadingLayout.setVisibility(View.GONE);
        payNow.setEnabled(true);
        payNow.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (isAdded()) {
                    ERPModel.tab_titles = _activity.getResources().getStringArray(R.array.fee_due_title);
                    ERPModel.title = getString(R.string.fee_due_title);
                }
                FeeFragment1 feeDuesFragment = new FeeFragment1();
                _onNavigationListener.onShowFragment(feeDuesFragment,ERPModel.title ,true,false,false);
                /*Intent intent = new Intent(_activity, MarksActivity.class);
                intent.putExtra("showFeeDue", true);
                startActivityForResult(intent, 3);*/
            }
        });

        downloadChallan.setEnabled(true);
        downloadChallan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String uri = ERPModel.parentLoggedIn.getBranch().getBranchId() + "/fee/student/" + ERPModel.selectedKid.getId() + "/getFeeChallanDetails";
                new ServerRequestTask(FeeFragment.this, ERPModel.SERVER_URL, uri, null, true, "get").execute();
            }
        });

        if ((_activity) instanceof HomeActivity)
        {
            if (totalFeeDue != 0.0)
            {
                DecimalFormat df = new DecimalFormat("#.##");
                totalFeeDue=Double.valueOf(df.format(totalFeeDue));
                totalValueFeeDue.setText("" + ERPModel.rupeeSymbol + ERPUtil.formatAmount(totalFeeDue));
            }

        }
    }

    public void showDownloadChallanDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.download_challan_dialog);
        dialog.setCanceledOnTouchOutside(false);

        _fromDateLayout = (RelativeLayout) dialog.findViewById(R.id.from_date_layout);
        _monthsSpinner = (Spinner) dialog.findViewById(R.id.months_spinner);
        _bankSpinner = (Spinner) dialog.findViewById(R.id.bank_spinner);
        _downloadChallanButton = (Button) dialog.findViewById(R.id.download);
        _cancel = (Button) dialog.findViewById(R.id.cancel);
        fromDate = (TextView) dialog.findViewById(R.id.text_view_from_date);

        List<Bank> bankList = ERPModel.selectedKid.getBankList();
        if (bankList != null && bankList.size() > 0)
        {
            BankListAdapter bankAdapter = new BankListAdapter(getActivity(), bankList);
            _bankSpinner.setAdapter(bankAdapter);
        }else{
            _bankSpinner.setVisibility(View.GONE);
            selectedBankId ="";
        }

        _bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                Bank clickedObj = (Bank) parent.getItemAtPosition(position);
                selectedBankId = String.valueOf(clickedObj.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        List<String> monthsList = getApplicableMonthList();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(_activity, R.layout.spinner_item_downlaodchallan, monthsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _monthsSpinner.setAdapter(dataAdapter);

        _monthsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                String clickedObj = (String) parent.getItemAtPosition(position);
                selectedMonth = clickedObj;
                if (selectedMonth.equalsIgnoreCase("All Dues")) {
                    _fromDateLayout.setVisibility(View.VISIBLE);
                } else {
                    _fromDateLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        _fromDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                fromDatePickerDialog = new DatePickerDialog(_activity,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                fromDate.setTextColor(_activity.getResources().getColor(R.color.textLabelColor));
                                fromDate.setTypeface(null, Typeface.BOLD);
                                fromDate.setText(new StringBuilder()
                                        .append(dayOfMonth).append("-")
                                        .append(new DateFormatSymbols().getMonths()[monthOfYear])
                                        .append("-").append(year));
                                //selectedFromDate  = c.get().toString();
                                selectedTillDate = year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);

                Calendar c1 = Calendar.getInstance();
                fromDatePickerDialog.getDatePicker().setMaxDate(currentSessionEndDate.getTime());
                fromDatePickerDialog.getDatePicker().setMinDate(currentSessionStartDate.getTime());
                fromDatePickerDialog.show();
            }
        });

        _downloadChallanButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (validate())
                {
                    dialog.dismiss();
                    if (android.os.Build.VERSION.SDK_INT >= 23)
                    {
                        if (_activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        {
                            downloadReceipt();
                        } else
                        {
                            ((HomeActivity) _activity).isStoragePermissionGranted();
                        }
                    } else
                    {
                        downloadReceipt();
                    }
                }
            }

            private boolean validate()
            {
                if (selectedMonth.equalsIgnoreCase("Please select month")|| selectedMonth==null)
                {
                    Toast.makeText(_activity, "Please select Month", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (_bankSpinner.getVisibility() == View.VISIBLE )
                {
                    if(selectedBankId == null ) {
                        Toast.makeText(_activity, "Please select Bank", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                if (_fromDateLayout.getVisibility() == View.VISIBLE )
                {
                    if(selectedTillDate == null ) {
                        Toast.makeText(_activity, "Please select Date", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                return true;
            }
        });

        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private List<String> getApplicableMonthList() {

        List<Session> sessionData = ERPModel.selectedKid.getAcademicSession().getAcademicSessionList();
        for(Session session:sessionData)
        {
            if(session.getIfCurrent()){
                currentSessionStartDate = session.getStartDate();
                currentSessionEndDate = session.getEndDate();
            }
        }
        Calendar cal = Calendar.getInstance();
        if (currentSessionStartDate.before(currentSessionEndDate)) {
            cal.setTime(currentSessionStartDate);
        } else {
            cal.setTime(currentSessionEndDate);
            currentSessionEndDate = currentSessionStartDate;
        }
        List<String> monthsList = new ArrayList<String>();
        while (cal.getTime().before(currentSessionEndDate)) {
            String s = new SimpleDateFormat("MMM yyyy").format(cal.getTime());
            monthsList.add(s);
            cal.add(Calendar.MONTH, 1);
        }
        monthsList.add(0, "Please Select Month");
        monthsList.add(1, "All Dues");
        return monthsList;
    }

    private void downloadReceipt() {
        JSONObject jsonObject = new JSONObject();
        try {

            if(selectedMonth.equalsIgnoreCase("All Dues")) {
                jsonObject.put("startDate",new SimpleDateFormat("yyyy-MM-dd").format(currentSessionStartDate) );
                jsonObject.put("endDate",selectedTillDate);
                jsonObject.put("dueStr", "allDue");
            }
            else {
                jsonObject.put("startDate",getStartDate(selectedMonth));
                jsonObject.put("endDate",getEndDate(selectedMonth));
                jsonObject.put("dueStr", "");
            }
            if(selectedBankId.equals(""))
                jsonObject.put("bankId",null);
            else
                jsonObject.put("bankId",selectedBankId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, Object> reqMap = new HashMap<>();
        reqMap.put("KEY", jsonObject.toString());

        String uri = ERPModel.parentLoggedIn.getBranch().getBranchId() + "/fee/student/" + ERPModel.selectedKid.getId() + "/downloadFeeChallan";
        String url = ERPModel.SERVER_URL + uri;
        Bitmap largeIcon = BitmapFactory.decodeResource(_activity.getResources(), com.iconcept.pdfviewer.R.drawable.erp_launcher);
        NotificationManager mNotifyManager = (NotificationManager) _activity.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(_activity);
        mBuilder.setContentText("Downloading...")
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.download);
        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
//        new ServerRequestTask(this, ERPModel.SERVER_URL, uri, reqMap, true, "post").execute();
        new DownloadFileTask(_activity, url, reqMap, "", mNotifyManager, mBuilder, 10, null, "post").execute();
    }

    private String getStartDate(String selectedMonth) {
        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy");
        Date startDate;
        try {
            startDate = df.parse(selectedMonth);
            Calendar gc = Calendar.getInstance();
            gc.setTime(startDate);
            gc.set(Calendar.MONTH, gc.get(Calendar.MONTH));
            gc.set(Calendar.YEAR, gc.get(Calendar.YEAR));
            gc.set(Calendar.DAY_OF_MONTH, 1);
            Date monthStart = gc.getTime();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("Calculated month start date : " + df1.format(monthStart));
           /* startDate = df.parse(selectedMonth);
            String newDateString = df.format(startDate);
            Calendar gc = new GregorianCalendar();
            gc.set(Calendar.MONTH, startDate.getMonth());
            gc.set(Calendar.DAY_OF_MONTH, 1);
            gc.set(Calendar.YEAR,startDate.getYear());
            Date monthStart = gc.getTime();
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("Calculated month start date : " + df1.format(monthStart));*/
            return df1.format(monthStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getEndDate(String selectedMonth) {
        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy");
        Date startDate;
        try {
            startDate = df.parse(selectedMonth);
            Calendar gc = Calendar.getInstance();
            gc.setTime(startDate);
            gc.set(Calendar.YEAR, gc.get(Calendar.YEAR));
            gc.set(Calendar.MONTH, gc.get(Calendar.MONTH));
            gc.set(Calendar.DAY_OF_MONTH, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date monthEnd = gc.getTime();
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("Calculated month end date : " + df1.format(monthEnd));
            return df1.format(monthEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Map<String,List<FeeDue>> putFeeDueListInMap(List<FeeDue> feeDueSummaryList)
    {
        Map<String,List<FeeDue>> filteredList = new HashMap<>();
        for (FeeDue feeDue : feeDueSummaryList)
        {
            if (feeDue.getSubTypeId() != 1 && feeDue.getAmount() != 0.0)
            {
                totalFeeDue += feeDue.getTotal();
                ERPModel.scholarshipFilteredMap.put(feeDue.getFeeTypeId(), feeDue);
            } else if (feeDue.getSubTypeId() == 1 && feeDue.getAmount() != 0.0)
            {
                totalScholarShip += feeDue.getTotal();
            }

            String month = getMonthName(feeDue.getDueDate());
            if(filteredList.containsKey(month)){
                List<FeeDue> monthFeeDueList = filteredList.get(month);
                if(monthFeeDueList == null) {
                    monthFeeDueList = new ArrayList<>();
                    if(feeDue.getAmount()!=0.0)
                        monthFeeDueList.add(feeDue);
                }else {
                    if (feeDue.getAmount() != 0.0) {
                        monthFeeDueList.add(feeDue);
                    }
                }
                if(feeDue.getAmount()!=0.0)
                    filteredList.put(month, monthFeeDueList);
            }else{
                List<FeeDue> monthFeeDueList = new ArrayList<>();
                if(feeDue.getAmount()!=0.0) {
                    monthFeeDueList.add(feeDue);
                    filteredList.put(month, monthFeeDueList);
                    if(!feeMonthList.contains(month))
                        feeMonthList.add(month);
                }
            }
        }
        totalFeeDue = totalFeeDue - totalScholarShip;
        return filteredList;
    }

    private String getMonthName(Date selectedMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(selectedMonth);
        int month = cal.get(Calendar.MONTH);
        String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int yearName = cal.get(Calendar.YEAR);
        return monthName+" "+yearName;
    }

    public void showErrorLayout(String error)
    {
        listLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        textError.setText(error);
    }

    @Override
    public void onClick(Object object)
    {
//		FeeDue feeDue = (FeeDue) object;
//		changeActionTitleAndCount() method will be called for updating the contextual action bar and for the first time it will call onCreateActionMode
        /*if( _activity instanceof MarksActivity)
        {
            ((MarksActivity) _activity).changeActionTitleAndCount();
        }*/

    }

    /*
     * @see android.support.v4.app.FragmentActivity#onResume()
     * User select Fee due to pay -> Press Back , currently in feedue Fragment
     */
    @Override
    public void onResume()
    {
        super.onResume();
        _activity.getSupportActionBar().setTitle(R.string.fee_title);
        if (feeDueSummaryAdapter != null && (_activity) instanceof HomeActivity)
        {
            feeDueSummaryAdapter.totalAmount = 0.0;
            ERPModel.ischeckbox = false;
            feeDueSummaryAdapter.notifyDataSetChanged();
            layoutParams.setMargins(0, 0, 0, 0);
            listLayout.setLayoutParams(layoutParams);
            //settingTheTitleToFeeDue();

        } /*else if (feeDueSummaryAdapter != null && (_activity) instanceof MarksActivity)
        {
            setCheckedBoxesToFalse();
        }*/

    }

    public void settingTheTitleToFeeDue()
    {
        if (!isPayNowTitle)
        {
            isPayNowTitle = true;
            ((HomeActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.feeSelectErrorMsg));
        } else
        {
            isPayNowTitle = false;
            ((HomeActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.feeTitle));
        }
    }

    public void hideGoToBagButton()
    {
        goToBagButton.setVisibility(View.GONE);
        proceedLayout.setVisibility(View.VISIBLE);
        if (feeDueSummaryAdapter != null)
        {
            layoutParams.setMargins(0, 0, 0, 0);
            listLayout.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();  // Always call the superclass
        System.out.println("destroy ");
        // Stop method tracing that the activity started during onCreate()
    }

    public void setCheckedBoxesToFalse()
    {
        for (FeeDue feeDue : ERPModel.selectedKid.getFeeDueDetail().getFeeDueList())
        {
            feeDue.setChecked(false);
        }
    }

   /* public void setCheckedBoxesToTrue()
    {
        for (FeeDue feeDue : ERPModel.selectedKid.getFeeDueDetail().getFeeDueList())
        {
            feeDue.setChecked(true);
            (ERPModel.feeDueCardSelectionCount)++;
            feeDueSummaryAdapter.addFeeEntry(feeDue);
        }
        feeDueSummaryAdapter.notifyDataSetChanged();
    }*/

    public void refresh()
    {
        List<FeeDue> feeDueList = ERPModel.selectedKid.getFeeDueDetail().getFeeDueList();
        if(feeDueList == null){
            showErrorLayout("Student Fee Due Details not found");
        }
        if (feeDueList != null)
        {
            setInfo(feeDueList);
        } else if (ERPModel.responseMap.get(uri) != null && ERPModel.responseMap.get(uri) == true)
        {
            showErrorLayout("Student Fee Due Details not found");
        }
    }
    public void expandChildList(List<FeeDue> list)
    {
        feeDueChildAdapter = new ExpandableFeeChildAdapter(getActivity(),false);
        feeDueChildAdapter.setData(list, feeMonthList);
        listView = (AnimatedExpandableListView) view.findViewById(R.id.listView);
        listView.setAdapter(feeDueChildAdapter);
        listView.expandGroupWithAnimation(currentClickedGroupPosition);
        previousClickedGroupPosition = currentClickedGroupPosition;
    }

    private void setFeeDueInfo(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject jsonObject = new JSONObject(response);
            FeeDueDetail feeDueDetail = new FeeDueDetail();
            List<FeeDue> feeDueSummaryList = null;
            if (jsonObject.has("feeChallan")) {
                feeDueDetail.setFeeChallan(jsonObject.getBoolean("feeChallan"));
            }

            if (jsonObject.has("installBasedConcession")) {
                feeDueDetail.setInstallBasedConcession(jsonObject.getBoolean("installBasedConcession"));
            }

            if (jsonObject.has("installmentBasedFine")) {
                feeDueDetail.setInstallmentBasedFine(jsonObject.getBoolean("installmentBasedFine"));
            }

            if (jsonObject.has("disableFeeTypeInParentLogin")) {
                feeDueDetail.setDisableFeeTypeInParentLogin(jsonObject.getBoolean("disableFeeTypeInParentLogin"));
                System.out.print("disableFeeTypeInParentLogin : " + jsonObject.getBoolean("disableFeeTypeInParentLogin"));

            }
            if (jsonObject.has("feeDueSummary")) {
                JSONArray jsonArray = jsonObject.getJSONArray("feeDueSummary");
                if (jsonArray.length() > 0) {
                    feeDueSummaryList = objectMapper.readValue(jsonArray.toString(), new TypeReference<ArrayList<FeeDue>>() {
                    });
                }
            }

            feeDueDetail.setFeeDueList(feeDueSummaryList);
            ERPModel.selectedKid.setFeeDueDetail(feeDueDetail);
            refresh();
            Log.v("screenName", "Refreshing FeeFragment");
            /*int position = returnPositionOfModule("FeeFragment");
            if (position != -1) {
                FeeFragment feeFragment = null;
                for(int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                    if(getSupportFragmentManager().getFragments().get(i) instanceof FeeFragment) {
                        feeFragment = (FeeFragment) getSupportFragmentManager().getFragments().get(i);
                    }
                }
                if (feeFragment != null && feeFragment instanceof FeeFragment) {
                    feeFragment.refresh();
                    Log.v("screenName", "Refreshing FeePaymentFragment");
                }
            }*/
        } catch (Exception jsone) {
            CustomLogger.e("HomeActivity", "inside setFeeDueInfo()", jsone);
        }
    }

    private void setBankDetails(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            List<Bank> bankList = null;
            if (jsonObject.has("banksList") && !jsonObject.isNull("banksList")) {
                JSONArray subjectJsonArray = jsonObject.getJSONArray("banksList");
                ObjectMapper objectMapper = new ObjectMapper();
                bankList = objectMapper.readValue(subjectJsonArray.toString(), new TypeReference<ArrayList<Bank>>() {});
                ERPModel.selectedKid.setBankList(bankList);
            }

        } catch (Exception jsone) {
            CustomLogger.e("HomeActivity", "inside setBankDetails()", jsone);
        }

        showDownloadChallanDialog();
        Log.v("screenName", "opening Download Challan Dialog");
        /*int position = returnPositionOfModule("FeeFragment");
        if (position != -1) {
            FeeFragment feeFragment = (FeeFragment) adapter.fragmentsList.get(position);
            if (feeFragment != null) {
                feeFragment.showDownloadChallanDialog();
                Log.v("screenName", "opening Download Challan Dialog");
            }
        }*/
    }


    @Override
    public void onResponseReceived(String uri, String response)
    {
        if(getView() != null && isAdded())
        {
            if (uri.indexOf("fee") != -1)
            {
                if(uri.indexOf("getFeeChallanDetails") != -1)
                {
                    ERPModel.responseMap.put(uri, true);
                    setBankDetails(response);
                }
                else
                {
                    setFeeDueInfo(response);
                }
            }
        }
    }

    @Override
    public void onExceptionOccured(String uri, String msg)
    {
        if(getView() != null && isAdded())
        {
            if (msg.contains(_activity.getString(R.string.session_expired)))
                ERPUtil.showSessionExpiredDialog(getActivity(), msg);
            else {
                showErrorLayout(msg);
            }
        }
    }

    @Override
    public Map<String, String> getRequestHeaders(String url) {
        return ERPUtil.getRequestHeaders(url);
    }

    @Override
    public void showLoadingDialog() {
        ERPUtil.showLoadingDialog(getActivity(), "Loading..");
    }

    @Override
    public void dismissLoadingDialog() {
        ERPUtil.dismissLoadingDialog(getActivity());
    }


    @Override
    public void showPaymentLoadingScreen(String msg) {

    }

    @Override
    public void dismissPaymentLoadingScreen(String msg, String response) {

    }

    @Override
    public void showLoadingDialog(String msg) {

    }
}