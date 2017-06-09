package com.next.matchthefollowing;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by next on 5/5/17.
 */
public class Point implements Parcelable
{
    float startPoint_Q;
    float endPoint_Q;
    float startPoint_A;
    float endPoint_A;

    public Point(float startPoint_Q, float endPoint_Q, float startPoint_A, float endPoint_A)
    {
        this.startPoint_Q = startPoint_Q;
        this.endPoint_Q = endPoint_Q;
        this.startPoint_A = startPoint_A;
        this.endPoint_A = endPoint_A;
    }

    protected Point(Parcel in)
    {
        startPoint_Q = in.readFloat();
        endPoint_Q = in.readFloat();
        startPoint_A = in.readFloat();
        endPoint_A = in.readFloat();
    }

    public static final Creator<Point> CREATOR = new Creator<Point>()
    {
        @Override
        public Point createFromParcel(Parcel in)
        {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size)
        {
            return new Point[size];
        }
    };

    public float getStartPoint_Q()
    {
        return startPoint_Q;
    }

    public void setStartPoint_Q(float startPoint_Q)
    {
        this.startPoint_Q = startPoint_Q;
    }

    public float getEndPoint_Q()
    {
        return endPoint_Q;
    }

    public void setEndPoint_Q(float endPoint_Q)
    {
        this.endPoint_Q = endPoint_Q;
    }

    public float getStartPoint_A()
    {
        return startPoint_A;
    }

    public void setStartPoint_A(float startPoint_A)
    {
        this.startPoint_A = startPoint_A;
    }

    public float getEndPoint_A()
    {
        return endPoint_A;
    }

    public void setEndPoint_A(float endPoint_A)
    {
        this.endPoint_A = endPoint_A;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeFloat(startPoint_Q);
        dest.writeFloat(endPoint_Q);
        dest.writeFloat(startPoint_A);
        dest.writeFloat(endPoint_A);
    }
}
