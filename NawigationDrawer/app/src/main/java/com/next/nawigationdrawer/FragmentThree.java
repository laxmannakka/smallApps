package com.next.nawigationdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by next on 26/12/16.
 */
public class FragmentThree extends Fragment {

    View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView =inflater.inflate(R.layout.fr_3layout,container,false);
        ImageView image = (ImageView) mView.findViewById(R.id.imageview3);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.drawer_layout,new FragmentTwo()).commit();
            }
        });

        return mView;
    }
}
