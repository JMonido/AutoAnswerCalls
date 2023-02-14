package com.autocall.autoanswercalls;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter1 extends RecyclerView.Adapter<DataAdapter1.ViewHolder> {


    private List<Model> carItemList=new ArrayList<>();
    Context context;
    DatabaseHelper myDb;

    public DataAdapter1(Context context, List<Model> carItemList) {
        this.carItemList = carItemList;
        this.context=context;
        myDb = new DatabaseHelper(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.

        View carItemView = layoutInflater.inflate(R.layout.item_chatroom, parent, false);



        // Create and return our custom Car Recycler View Item Holder object.
        ViewHolder ret = new ViewHolder(carItemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        if (holder instanceof ViewHolder) {
//
//            populateItemRows((ViewHolder) holder, position);
//        } else if (holder instanceof LoadingViewHolder) {
//            showLoadingView((LoadingViewHolder) holder, position);
//        }
        if(carItemList!=null) {
            // Get car item dto in list.
//            Model carItem = carItemList.get(position);


            if(carItemList.get(position) != null) {

                try {
                holder.txtstartvalue.setText(carItemList.get(position).getSTARTTIME());
                holder.txtendvalue.setText(carItemList.get(position).getENDTIME());
                holder.txtdescriptionvalue.setText(carItemList.get(position).getDESCRIPTION());
                holder.txtDate1.setText(carItemList.get(position).getSTARTDATE());
                holder.txtDate2.setText(carItemList.get(position).getENDDATE());
                File file=new File(carItemList.get(position).getAUDIO());
                holder.txtaudiovalue.setText(file.getName());
                }catch (Exception e){
                    holder.txtaudiovalue.setText(carItemList.get(position).getAUDIO());
                }

                holder.imageViewliked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DeleteData(carItemList.get(holder.getAdapterPosition()).getID(),holder.getAdapterPosition());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if(carItemList!=null)
        {
            ret = carItemList.size();
        }
        return ret;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void clear() {
        int size = carItemList.size();
        carItemList.clear();
        notifyItemRangeRemoved(0, size);
    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
                public ImageView imageViewliked;
        public TextView txtstartvalue;
        public TextView txtendvalue;
        public TextView txtaudiovalue;
        public TextView txtdescriptionvalue;
        public TextView txtDate1;
        public TextView txtDate2;
        public CardView relativeLayout1;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageViewliked = (ImageView) itemView.findViewById(R.id.imgdelete);
            this.txtstartvalue = (TextView) itemView.findViewById(R.id.txtstartvalue);
            this.txtendvalue = (TextView) itemView.findViewById(R.id.txtendvalue);
            this.txtaudiovalue = (TextView) itemView.findViewById(R.id.txtaudiovalue);
            this.txtdescriptionvalue = (TextView) itemView.findViewById(R.id.txtdescriptionvalue);
            this.txtDate1 = (TextView) itemView.findViewById(R.id.txtDate1);
            this.txtDate2 = (TextView) itemView.findViewById(R.id.txtDate2);
            relativeLayout1 = (CardView) itemView.findViewById(R.id.rel);
        }
    }

    public void DeleteData(String id,int poss) {
                        Integer deletedRows = myDb.deleteData(id);
                        if(deletedRows > 0) {
                            Toast.makeText(context, "Data Deleted", Toast.LENGTH_LONG).show();
                            carItemList.remove(poss);
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(context, "Data not Deleted", Toast.LENGTH_LONG).show();
                        }
             }

}
