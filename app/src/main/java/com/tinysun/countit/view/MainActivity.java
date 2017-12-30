package com.tinysun.countit.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tinysun.countit.R;
import com.tinysun.countit.model.CountDataModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    private ArrayList<CountDataModel> mCountDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        setCountList();

        adapter = new MainRecyclerAdapter(this, mCountDataList);
        recyclerView.setAdapter(adapter);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setCountList(){
        mCountDataList = new ArrayList<>();

        CountDataModel mCountData1 = new CountDataModel();
        CountDataModel mCountData2 = new CountDataModel();
        CountDataModel mCountData3 = new CountDataModel();

        mCountData1.setTitle("title one");
        mCountData1.setCountNum(1);
        mCountDataList.add(0, mCountData1);

        mCountData2.setTitle("title two");
        mCountData2.setCountNum(2);
        mCountDataList.add(1, mCountData2);

        mCountData3.setTitle("title three");
        mCountData3.setCountNum(3);
        mCountDataList.add(2, mCountData3);
    }

    private void addCountList(String _str){
        CountDataModel mCountData = new CountDataModel();
        mCountData.setTitle(_str);
        mCountData.setCountNum(mCountDataList.size()+1);

        mCountDataList.add(mCountData);

        adapter = new MainRecyclerAdapter(this, mCountDataList);
        recyclerView.setAdapter(adapter);
    }
}
