package com.tinysun.countit.controller;

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
import com.tinysun.countit.model.CommonEnum;

import static com.tinysun.countit.model.CountDataListStatic.mCountDataListStatic;

/**
 * Created by YS CHOI on 2017-12-30.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    private Context context;

    public MainRecyclerAdapter(Context _context){
        context = _context;
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
        viewHolder.itemTitleTv.setText(mCountDataListStatic.get(i).getTitle());
        viewHolder.itemCountTv.setText(Integer.toString(mCountDataListStatic.get(i).getCountNum()));
        setImageCountView(viewHolder.imageGroupRl, mCountDataListStatic.get(i).getCountNum());
    }

    @Override
    public int getItemCount() {
        return mCountDataListStatic.size();
    }


    public void removeItem(int position) {
        mCountDataListStatic.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCountDataListStatic.size());

        //Toast.makeText(context, R.string.delete_item_complete, Toast.LENGTH_LONG).show();
    }

    public void addCount(int _position, TextView _itemCountTv, RelativeLayout _imageGroupRl){

        Integer oriCountNum = mCountDataListStatic.get(_position).getCountNum();
        if(oriCountNum >= CommonEnum.CountMaxValue.COUNT_MAX_VALUE.getCountMaxValue()){
            Toast.makeText(context, R.string.plus_item_alert, Toast.LENGTH_LONG).show();
            return;
        }

        mCountDataListStatic.get(_position).setCountNum(oriCountNum+1);
        _itemCountTv.setText( String.valueOf(mCountDataListStatic.get(_position).getCountNum()).toString() );

        setImageCountView(_imageGroupRl, oriCountNum+1);
    }

    public void minusCount(int _position, TextView _itemCountTv, RelativeLayout _imageGroupRl){

        Integer oriCountNum = mCountDataListStatic.get(_position).getCountNum();
        if(oriCountNum <= 0){
            Toast.makeText(context, R.string.minus_item_alert, Toast.LENGTH_LONG).show();
            return;
        }

        mCountDataListStatic.get(_position).setCountNum(oriCountNum-1);
        _itemCountTv.setText( String.valueOf(mCountDataListStatic.get(_position).getCountNum()).toString() );

        setImageCountView(_imageGroupRl, oriCountNum-1);
    }

    private void setImageCountView(RelativeLayout _imageGroupRl, int _imageCnt){

        _imageGroupRl.removeAllViews();

        int numOf1Line = CommonEnum.CountNumOf1Line.COUNT_NUM_OF_1_LINE.getCountNumOf1Line();
        int numOf1Set = CommonEnum.CountNumOf1Set.COUNT_NUM_OF_1_SET.getCountNumOf1Set();

        int totalListNum = 0;                               // 카운트이미지의 총 라인 수 : 1줄 2세트(10개)
        int lastLineNum = (Integer) _imageCnt%numOf1Line;   // 카운트이미지의 마지막 라인의 카운트 수 (0~9)
        if( lastLineNum == 0 ){
            totalListNum =  (Integer) _imageCnt/numOf1Line;
        }else{
            totalListNum =  (Integer) (_imageCnt/numOf1Line)+1;
        }
        int lastCountImageCnt = (Integer) _imageCnt%numOf1Set;  // 카운트이미지 마지막 이미지 값
        if (lastCountImageCnt == 0){
            lastCountImageCnt = numOf1Set;
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
                    rlLp.topMargin = CommonEnum.CountLayoutParams.RELATIVE_LAYOUT_TOP_MARGIN.getMargin();
                }

                ImageView iv1 = new ImageView(context);
                ImageView iv2 = new ImageView(context);

                if ( ((i+1) == totalListNum) && lastLineNum <= numOf1Set && (lastLineNum != 0) ) { // 마지막 라인에 마지막 이미지가 5이하일 경우 : 마지막 이미지(이미지뷰) 1개만을 삽입한다.
                    iv1.setBackgroundResource(getCountImageResource(lastCountImageCnt));
                    iv1.setId(R.id.main_card_item_content_group_rl_m_iv_+i);
                    rl.addView(iv1);
                }else{  // 마지막 라인이 아니거나, 마지막 라인인데 이미지가 6이상일 경우 : 이미지(이미지뷰) 2개를 삽입한다.
                    iv1.setBackgroundResource(getCountImageResource(numOf1Set));
                    if ((i+1) != totalListNum){ // 마지막 라인이 아니면 항상 5개짜리 셋트(2개)를 채운다
                        iv2.setBackgroundResource(getCountImageResource(numOf1Set));
                    }else{
                        iv2.setBackgroundResource(getCountImageResource(lastCountImageCnt));
                    }

                    iv1.setId(R.id.main_card_item_content_group_rl_m_iv_+i);

                    RelativeLayout.LayoutParams iv2Lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    iv2Lp.addRule(RelativeLayout.RIGHT_OF, R.id.main_card_item_content_group_rl_m_iv_+i);
                    iv2Lp.leftMargin = CommonEnum.CountLayoutParams.IMAGE_VIEW_LEFT_MARGIN.getMargin();

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
            resource = CommonEnum.CountImageResource.COUNT_IMAGE_RESOURCE_SOLID_BLACK_1.getCountImageResource();
        }else if(_countNum == 2){
            resource = CommonEnum.CountImageResource.COUNT_IMAGE_RESOURCE_SOLID_BLACK_2.getCountImageResource();
        }else if(_countNum == 3){
            resource = CommonEnum.CountImageResource.COUNT_IMAGE_RESOURCE_SOLID_BLACK_3.getCountImageResource();
        }else if(_countNum == 4){
            resource = CommonEnum.CountImageResource.COUNT_IMAGE_RESOURCE_SOLID_BLACK_4.getCountImageResource();
        }else if(_countNum == 5){
            resource = CommonEnum.CountImageResource.COUNT_IMAGE_RESOURCE_SOLID_BLACK_5.getCountImageResource();
        }else{
            resource = CommonEnum.CountImageResource.COUNT_IMAGE_RESOURCE_SOLID_BLACK_5.getCountImageResource();;
        }

        return resource;
    }

}