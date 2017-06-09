package com.next.fragmenttransaction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by next on 19/5/17.
 */
public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyAdapter>
{
    Context mContext;

    int [] drawble = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
    public RecyclerviewAdapter(Context mContext)
    {
        this.mContext = mContext;

    }

    @Override
    public RecyclerviewAdapter.MyAdapter onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.imageview,parent,false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(RecyclerviewAdapter.MyAdapter holder, int position)
    {
        holder.imageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount()
    {
        return 10;
    }

    public class MyAdapter extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        public MyAdapter(View itemView)
        {
            super(itemView);
             imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }
}
