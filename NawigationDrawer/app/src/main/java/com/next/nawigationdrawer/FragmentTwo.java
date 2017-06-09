package com.next.nawigationdrawer;

import android.app.Fragment;
import android.app.FragmentManagerNonConfig;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by next on 26/12/16.
 */
public class FragmentTwo extends Fragment {

    View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fr_2layout,container,false);
        ImageView image = (ImageView) mView.findViewById(R.id.imageview2);
        Button button = (Button) mView.findViewById(R.id.fr2button);
       image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Bundle bundle = new Bundle();
               bundle.putInt("key",2);
               FragmnetOne obj = new FragmnetOne();
               obj.setArguments(bundle);
               getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.drawer_layout,obj).commit();
           }
       });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"mesage",Toast.LENGTH_LONG).show();
                getFragmentManager().beginTransaction().replace(R.id.drawer_layout,new FragmentThree()).commit();
            }
        });
        return  mView;
    }
}
