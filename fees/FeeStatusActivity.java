package com.fees;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.home.HomeActivity;
import com.iconcept.model.IconceptModel;
import com.resources.erp.R;

public class FeeStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IconceptModel.context = this;
        setContentView(R.layout.activity_fee_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_app);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Payment Details");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if((bundle != null) && (bundle.get("showPaymentSummary") != null)) {
            PaymentSummaryFragment newFragment = new PaymentSummaryFragment();
            Bundle args = new Bundle();
            args.putString("payment_json", bundle.getString("payment_json"));
            newFragment.setArguments(args);
            onShowFragment(newFragment, PaymentSummaryFragment.class.getName(), false, true, false);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent intent = new Intent(FeeStatusActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("ShowFeeFragment", true);
        startActivity(intent);

        finish();

    }

    public void onShowFragment(Fragment fragment, String fragmentName, boolean addToBackStack, boolean clearBackStack, boolean animationEnable)
    {
        if (!isFinishing())
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (fragment != null)
            {
                if (!isFinishing())
                {
                    if (clearBackStack && !isFinishing())
                    {
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    if (addToBackStack && !isFinishing()) {
                        fragmentTransaction.replace(R.id.content_frame, fragment, fragmentName).addToBackStack(fragmentName).commitAllowingStateLoss();
                    } else {
                        fragmentTransaction.replace(R.id.content_frame, fragment, fragmentName).commitAllowingStateLoss();
                    }
                }
            }
            fragmentManager.executePendingTransactions();
        }

    }
}
