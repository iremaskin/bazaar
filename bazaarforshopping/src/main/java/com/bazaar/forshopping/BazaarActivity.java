package com.bazaar.forshopping;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by gsoganci on 12.09.2017.
 */

public class BazaarActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle onSavedInstance) {
        super.onCreate(onSavedInstance);
        //   ModuleManager.setContext(this);
        fragmentManager = getSupportFragmentManager();
    }

    public void subFragmentReplaceOnContainer(int viewContainerId, Fragment selectionFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(viewContainerId, selectionFragment);
        fragmentTransaction.show(selectionFragment);
        fragmentTransaction.commit();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(BazaarActivity.this);
            mProgressDialog.setMessage("Yükleniyor...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
