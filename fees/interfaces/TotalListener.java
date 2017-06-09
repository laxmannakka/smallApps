package com.fees.interfaces;

public interface TotalListener {
    public void onTotalChanged(double sum);

    public void expandGroupEvent(int groupPosition, boolean isExpanded);
}
