package com.tinysun.countit.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tinysun.countit.R;
import com.tinysun.countit.model.CountDataModel;

import java.util.ArrayList;

/**
 * Created by YS CHOI on 2017-12-30.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CountDataModel> mCountDataList;

    public MainRecyclerAdapter(Context _context, ArrayList<CountDataModel> _mCountDataList){
        context = _context;

        mCountDataList = _mCountDataList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView itemTitleTv;
        public TextView itemCountTv;
        public Button addBtn;
        public Button minusBtn;

        public RelativeLayout imageGroupRl;

        public ViewHolder(View itemView){
            super(itemView);
            itemTitleTv = (TextView) itemView.findViewById(R.id.main_card_item_title);
            itemCountTv = (TextView) itemView.findViewById(R.id.main_card_item_title_num);
            addBtn = (Button) itemView.findViewById(R.id.main_card_item_add_btn);
            minusBtn = (Button) itemView.findViewById(R.id.main_card_item_minus_btn);
            imageGroupRl = (RelativeLayout) itemView.findViewById(R.id.main_card_item_image_group_rl);

            addBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    addCount(getAdapterPosition(), itemCountTv, imageGroupRl);
                }
            });

            minusBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    minusCount(getAdapterPosition(), itemCountTv, imageGroupRl);
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(R.string.delete_popup_title);
                    alert.setMessage(R.string.delete_popup_content);
                    alert.setPositiveButton(R.string.delete_popup_yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeItem(getAdapterPosition());
                            dialog.dismiss();
                        }
                    });

                    alert.setNegativeButton(R.string.delete_popup_no, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();

                    return false;
                }

            });


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i){
        viewHolder.itemTitleTv.setText(mCountDataList.get(i).getTitle());
        viewHolder.itemCountTv.setText(Integer.toString(mCountDataList.get(i).getCountNum()));
        setImageCountView(viewHolder.imageGroupRl, mCountDataList.get(i).getCountNum());
    }

    @Override
    public int getItemCount() {
        return mCountDataList.size();
    }


    public void removeItem(int position) {
        mCountDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCountDataList.size());

        Toast.makeText(context, R.string.delete_item_complete, Toast.LENGTH_LONG).show();
    }

    public void addCount(int _position, TextView _itemCountTv, RelativeLayout _imageGroupRl){

        Integer oriCountNum = mCountDataList.get(_position).getCountNum();
        if(oriCountNum > 99){
            Toast.makeText(context, R.string.plus_item_alert, Toast.LENGTH_LONG).show();
            return;
        }

        mCountDataList.get(_position).setCountNum(oriCountNum+1);
        _itemCountTv.setText( String.valueOf(mCountDataList.get(_position).getCountNum()).toString() );

        setImageCountView(_imageGroupRl, oriCountNum+1);
    }

    public void minusCount(int _position, TextView _itemCountTv, RelativeLayout _imageGroupRl){

        Integer oriCountNum = mCountDataList.get(_position).getCountNum();
        if(oriCountNum < 1){
            Toast.makeText(context, R.string.minus_item_alert, Toast.LENGTH_LONG).show();
            return;
        }

        mCountDataList.get(_position).setCountNum(oriCountNum-1);
        _itemCountTv.setText( String.valueOf(mCountDataList.get(_position).getCountNum()).toString() );

        setImageCountView(_imageGroupRl, oriCountNum-1);
    }

    private void setImageCountView(RelativeLayout _imageGroupRl, int _imageCnt){

        _imageGroupRl.removeAllViews();

        int totalListNum = 0;                       // 카운트이미지의 총 라인 수 : 1줄 2세트(10개)
        int lastLineNum = (Integer) _imageCnt%10;   // 카운트이미지의 마지막 라인의 카운트 수 (0~9)
        if( lastLineNum == 0 ){
            totalListNum =  (Integer) _imageCnt/10;
        }else{
            totalListNum =  (Integer) (_imageCnt/10)+1;
        }
        int lastCountImageCnt = (Integer) _imageCnt%5;  // 카운트이미지 마지막 이미지 값
        if (lastCountImageCnt == 0){
            lastCountImageCnt = 5;
        }

        if(_imageCnt != 0){
            for(int i=0; i<totalListNum; i++){

                RelativeLayout rl = new RelativeLayout(context);
                rl.setId(R.id.main_card_item_content_group_rl_m_rl_+i);

                RelativeLayout.LayoutParams rlLp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                if(i > 0){
                    rlLp.addRule(RelativeLayout.BELOW, R.id.main_card_item_content_group_rl_m_rl_+(i-1));
                    rlLp.topMargin = 16;
                }

                ImageView iv1 = new ImageView(context);
                ImageView iv2 = new ImageView(context);

                if ( ((i+1) == totalListNum) && lastLineNum <= 5 && (lastLineNum != 0) ) { // 마지막 라인에 마지막 이미지가 5이하일 경우 : 마지막 이미지(이미지뷰) 1개만을 삽입한다.
                    iv1.setBackgroundResource(getCountImageResource(lastCountImageCnt));
                    iv1.setId(R.id.main_card_item_content_group_rl_m_iv_+i);
                    rl.addView(iv1);
                }else{  // 마지막 라인이 아니거나, 마지막 라인인데 이미지가 6이상일 경우 : 이미지(이미지뷰) 2개를 삽입한다.
                    iv1.setBackgroundResource(getCountImageResource(5));
                    if ((i+1) != totalListNum){ // 마지막 라인이 아니면 항상 5개짜리 셋트(2개)를 채운다
                        iv2.setBackgroundResource(getCountImageResource(5));
                    }else{
                        iv2.setBackgroundResource(getCountImageResource(lastCountImageCnt));
                    }

                    iv1.setId(R.id.main_card_item_content_group_rl_m_iv_+i);

                    RelativeLayout.LayoutParams iv2Lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    iv2Lp.addRule(RelativeLayout.RIGHT_OF, R.id.main_card_item_content_group_rl_m_iv_+i);
                    iv2Lp.leftMargin = 24;

                    rl.addView(iv1);
                    rl.addView(iv2, iv2Lp);
                }


                _imageGroupRl.addView(rl, rlLp);

            }
        }

    }

    private int getCountImageResource(int _countNum){
        int resource = 0;

        if(_countNum == 1){
            resource = R.mipmap.count_line_solid_1;
        }else if(_countNum == 2){
            resource = R.mipmap.count_line_solid_2;
        }else if(_countNum == 3){
            resource = R.mipmap.count_line_solid_3;
        }else if(_countNum == 4){
            resource = R.mipmap.count_line_solid_4;
        }else if(_countNum == 5){
            resource = R.mipmap.count_line_solid_5;
        }else{
            resource = R.mipmap.count_line_solid_5;
        }

        return resource;
    }

}