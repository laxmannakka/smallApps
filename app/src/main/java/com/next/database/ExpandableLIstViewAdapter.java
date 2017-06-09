package com.next.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.next.database.modal.Dept;
import com.next.database.modal.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by next on 24/3/17.
 */
public class ExpandableLIstViewAdapter extends BaseExpandableListAdapter
{
    List<Employee> employeeList;
    List<Dept> deptList;
    Context context;
    HashMap<Integer,List<Employee>> selectdMap = new HashMap<>();
    int totalNumberOFEmployee;

    public ExpandableLIstViewAdapter(List<Employee> employeeList, List<Dept> deptList, Context context)
    {
        this.employeeList = employeeList;
        this.deptList = deptList;
        this.context = context;
    }

    @Override
    public int getGroupCount()
    {
        return deptList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        List<Employee> selectedList = new ArrayList<>();

       int deptId = deptList.get(groupPosition).getDepartmentId();
        for (int i = 0; i < employeeList.size(); i++)
        {
            Employee employee = employeeList.get(i);
            if (deptId == employee.getDepartMentId())
            {

                selectedList.add(employee);
            }
        }
        selectdMap.put(groupPosition,selectedList);
        totalNumberOFEmployee = selectedList.size();
        return selectedList.size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return 0;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {

            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_item, null);

        TextView name_text = (TextView) convertView.findViewById(R.id.deptname);
        TextView total_number = (TextView) convertView.findViewById(R.id.count);
        name_text.setText(deptList.get(groupPosition).getGetDepartmentName());
        total_number.setText(String.valueOf(getChildrenCount(groupPosition)));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.child_item, null);
        TextView empName = (TextView) convertView.findViewById(R.id.EmpName);
        TextView Empid = (TextView) convertView.findViewById(R.id.Empid);
        List<Employee> employeeList = selectdMap.get(groupPosition);
        Employee employee = employeeList.get(childPosition);
        empName.setText(employee.getEmployeeName());
        Empid.setText(String.valueOf(employee.getEmpId()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
