package com.tinysun.countit.view;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.tinysun.countit.R;
import com.tinysun.countit.controller.MainRecyclerAdapter;
import com.tinysun.countit.model.CommonEnum;
import com.tinysun.countit.model.CountDataModel;
import com.tinysun.countit.model.CountDataModelRealm;
import com.tinysun.countit.model.CountDataSavedTimeModel;
import com.tinysun.countit.model.CountDataSavedTimeModelRealm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.tinysun.countit.model.CountDataListStatic.mCountDataListStatic;
import static com.tinysun.countit.model.CountDataListStatic.mCountDataSavedTimeStatic;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    TextView savedTimeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Realm.init(this);

        savedTimeTv = (TextView) findViewById(R.id.toolbar_save_time_tv);

        // Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                final CustomCreateItemDialog createDialog = new CustomCreateItemDialog(MainActivity.this);
                createDialog.show();

                createDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                        if(!createDialog.isCancel()){
                            String inputString = createDialog.getInputStr();
                            if(inputString.isEmpty()){
                                Toast.makeText(MainActivity.this, R.string.no_input_title_error, Toast.LENGTH_LONG).show();
                            }else{
                                addCountList(inputString);
                            }
                        }

                    }
                });
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.main_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mCountDataListStatic = new ArrayList<CountDataModel>();

        showUserCountDataInUIThread(CommonEnum.GET_REALM_DATA_INITIAL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){

            case R.id.menu_save :
                saveCountItem();
                break;

            case R.id.menu_restore :
                restoreCountItem();
                break;

            case R.id.menu_language :
                Toast.makeText(MainActivity.this, "언어변경 기능은 준비중입니다.", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addCountList(String _str){
        CountDataModel mCountData = new CountDataModel();
        mCountData.setTitle(_str);
        mCountData.setCountNum(0);
        mCountData.setIdx(mCountDataListStatic.size());

        mCountDataListStatic.add(mCountData);

        adapter = new MainRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
    }


    private void saveCountItem() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Realm mRealm = Realm.getDefaultInstance();
                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        bgRealm.delete(CountDataModelRealm.class);

                        CountDataModelRealm cdmr = new CountDataModelRealm();
                        for(int i=0; i<mCountDataListStatic.size(); i++){
                            cdmr.setIdx(i);
                            cdmr.setTitle(mCountDataListStatic.get(i).getTitle());
                            cdmr.setCountNum(mCountDataListStatic.get(i).getCountNum());
                            bgRealm.copyToRealmOrUpdate(cdmr);
                        }

                        CountDataSavedTimeModel savedTime = new CountDataSavedTimeModel();
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String savedTimeNow = sdfNow.format(new Date());
                        savedTime.setSavedTime(savedTimeNow);

                        mCountDataSavedTimeStatic = savedTimeNow;
                        CountDataSavedTimeModelRealm cdstmr = new CountDataSavedTimeModelRealm();
                        cdstmr.setIdx(0);
                        cdstmr.setSavedTime(savedTimeNow);

                        bgRealm.copyToRealmOrUpdate(cdstmr);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        showUserCountDataInUIThread(CommonEnum.GET_REALM_DATA_SAVE);
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(MainActivity.this, R.string.realm_user_data_save_error + " : " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    private void restoreCountItem() {
        showUserCountDataInUIThread(CommonEnum.GET_REALM_DATA_RESTORE);
    }

    private void showUserCountDataInUIThread(final int _showType){

        mCountDataListStatic.clear();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Realm mRealm = Realm.getDefaultInstance();
                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {

                        CountDataModel cdm;
                        RealmResults<CountDataModelRealm> results = bgRealm.where(CountDataModelRealm.class).findAll();

                        if (results.size() > 0){
                            ArrayList<CountDataModel> tmpData = new ArrayList<CountDataModel>();

                            for(int i=0; i<results.size(); i++){
                                cdm = new CountDataModel();

                                cdm.setIdx(results.get(i).getIdx());
                                cdm.setTitle(results.get(i).getTitle());
                                cdm.setCountNum(results.get(i).getCountNum());

                                tmpData.add(cdm);
                            }

                            mCountDataListStatic.addAll(tmpData);

                        }

                        RealmResults<CountDataSavedTimeModelRealm> savedTimeResult = bgRealm.where(CountDataSavedTimeModelRealm.class).findAll();

                        if (savedTimeResult.size() > 0){
                            mCountDataSavedTimeStatic = savedTimeResult.get(0).getSavedTime().toString();
                        }

                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                        switch (_showType){

                            case CommonEnum.GET_REALM_DATA_INITIAL :
                                mainHandler.sendEmptyMessage(CommonEnum.GET_REALM_DATA_INITIAL);
                                break;

                            case CommonEnum.GET_REALM_DATA_SAVE :
                                mainHandler.sendEmptyMessage(CommonEnum.GET_REALM_DATA_SAVE);
                                break;

                            case CommonEnum.GET_REALM_DATA_RESTORE :
                                mainHandler.sendEmptyMessage(CommonEnum.GET_REALM_DATA_RESTORE);
                                break;

                            default:
                                mainHandler.sendEmptyMessage(CommonEnum.GET_REALM_DATA_INITIAL);
                                break;
                        }

                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(MainActivity.this, R.string.realm_user_data_read_error + " : " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private Handler mainHandler = new Handler(){
      @Override
      public void handleMessage(Message msg){

          if( msg.what == CommonEnum.GET_REALM_DATA_INITIAL
                  || msg.what == CommonEnum.GET_REALM_DATA_SAVE
                  || msg.what == CommonEnum.GET_REALM_DATA_RESTORE ){

              savedTimeTv.setText(mCountDataSavedTimeStatic);

              adapter = new MainRecyclerAdapter(MainActivity.this);
              recyclerView.setAdapter(adapter);

              switch (msg.what){
                  case CommonEnum.GET_REALM_DATA_INITIAL :
                      //Toast.makeText(MainActivity.this, R.string.realm_user_data_save_success, Toast.LENGTH_LONG).show();
                      break;

                  case CommonEnum.GET_REALM_DATA_SAVE :
                      Toast.makeText(MainActivity.this, R.string.realm_user_data_save_success, Toast.LENGTH_LONG).show();
                      break;

                  case CommonEnum.GET_REALM_DATA_RESTORE :
                      Toast.makeText(MainActivity.this, R.string.realm_user_data_restore_success, Toast.LENGTH_LONG).show();
                      break;

                  default:
                      break;
              }
          }
      }

    };

}
