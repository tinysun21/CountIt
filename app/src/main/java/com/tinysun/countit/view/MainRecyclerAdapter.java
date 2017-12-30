package com.tinysun.countit.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        public TextView itemTitle;
        public TextView itemCount;

        public ViewHolder(View itemView){
            super(itemView);
            itemTitle = (TextView)itemView.findViewById(R.id.main_recycler_item_title);
            itemCount = (TextView)itemView.findViewById(R.id.main_recycler_item_detail);

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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_recycler_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i){
        viewHolder.itemTitle.setText(mCountDataList.get(i).getTitle());
        viewHolder.itemCount.setText(Integer.toString(mCountDataList.get(i).getCountNum()));
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
}