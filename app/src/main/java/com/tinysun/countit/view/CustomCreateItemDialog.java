package com.tinysun.countit.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.tinysun.countit.R;

/**
 * Created by YS CHOI on 2017-12-31.
 */

public class CustomCreateItemDialog extends Dialog {

    Context context;

    EditText mInputEt;
    Button mCancelBtn;
    Button mCreateBtn;

    Boolean isCancel = true;
    String inputString = "";

    public CustomCreateItemDialog(@NonNull Context _context) {
        super(_context);

        context = _context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.custom_create_item_dialog);

        mInputEt = (EditText)findViewById(R.id.custom_dialog_et);
        mCancelBtn = (Button)findViewById(R.id.custom_dialog_cancle_btn);
        mCreateBtn = (Button)findViewById(R.id.custom_dialog_create_btn);

        mCancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                isCancel = false;
                inputString = mInputEt.getText().toString();
                dismiss();
            }
        });
    }

    public Boolean isCancel() { return isCancel; }
    public String getInputStr() { return inputString; }

}
