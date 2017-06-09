package com.next.nawigationdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by next on 26/12/16.
 */
public class FragmnetOne extends Fragment {
    View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int key = getArguments().getInt("key");
        if(key==1) {
            mView = inflater.inflate(R.layout.fragement, container, false);
        }
        else{
            mView = inflater.inflate(R.layout.item2layout,container,false);
            ImageView image = (ImageView) mView.findViewById(R.id.backimage);
            Button nextPage = (Button) mView.findViewById(R.id.fr_button);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                }
            });
            nextPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                    FragmentTwo obj = new FragmentTwo();
                    ft.addToBackStack(null);
                    ft.replace(R.id.drawer_layout,obj);
                    ft.commit();

                }
            });
        }

        return mView;
    }

}
