package com.kanhui.laowulao.locker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.locker.model.Config;
import com.kanhui.laowulao.locker.model.ContactModel;
import com.kanhui.laowulao.utils.LogUtils;
import com.kanhui.laowulao.utils.StringUtils;
import com.kanhui.laowulao.widget.IconView;
import com.kanhui.laowulao.widget.Md5HeaderView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    public static final int[] colors = {R.color.header_bg1,R.color.header_bg2,R.color.header_bg3,
            R.color.header_bg4,R.color.header_bg5,R.color.header_bg6,R.color.header_bg7,R.color.header_bg8};

    public static final int CALL_PHONE = 1;
    public static final int CALL_VIDEO = 2;

    private List<ContactModel> list = new ArrayList<>();
    private Context context;
    private int type = Config.TYPE_LIST;

    private ItemClickListener listener;

    public ItemClickListener getListener() {
        return listener;
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public ContactAdapter(Context c,int type){
        this.context = c;
        this.type = type;
    }

    public void setData(List<ContactModel> l){
        this.list = l;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(type == Config.TYPE_GRIDE){
            view = LayoutInflater.from(context).inflate(R.layout.item_gride_contact,null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_contact,null);
        }


        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ContactModel model = list.get(position);
        holder.tvName.setText(model.getName());
        holder.tvPhone.setText(model.getPhone());
        Config config = Config.getConfig();
        holder.tvName.setTextSize(config.getScaleSize());
//        switch (config.getScaleSize()){
//            case Config.SCALE_BIG:
//                holder.tvName.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.font_32));
//                break;
//            case Config.SCALE_MIDDLE:
//                holder.tvName.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.font_28));
//                break;
//            case Config.SCALE_SMALL:
//                holder.tvName.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.font_24));
//                break;
//        }
        String date = model.getDateStr();
        if(!StringUtils.isEmpty(date)){
            holder.tvHistory.setText(date + "有通话");
        } else {
            holder.tvHistory.setText("");
        }
        if(type == Config.TYPE_GRIDE){
            int bgColor = getCurrentBgResId(model.getName());
            holder.itemGride.setBackgroundColor(context.getResources().getColor(bgColor));
            holder.tvHistory.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvPhone.setTextColor(context.getResources().getColor(R.color.white));
        }
        holder.ivHeader.setVisibility(View.GONE);
        holder.md5HeaderView.setVisibility(View.VISIBLE);
        holder.md5HeaderView.setText(model.getName());
        holder.btnCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClick(model,position,CALL_PHONE);
                }
            }
        });
//        holder.btnCallVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(listener != null){
//                    listener.onItemClick(model,position,CALL_VIDEO);
//                }
//            }
//        });
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClick(model,position,CALL_PHONE);
                }
            }
        });
    }

    private int getCurrentBgResId(String text){
        int number = Md5HeaderView.getMd5Index(text);
        LogUtils.elog("adapter","name: " + text + ", number:" + number);
        int color = colors[0];
        int i = number%colors.length;
        color = colors[i];
        return color;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View rootView;
        TextView tvName,tvPhone,tvHistory;
        IconView btnCallPhone;//btnCallVideo;
        ImageView ivHeader;
        Md5HeaderView md5HeaderView;
        View itemGride;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            ivHeader = itemView.findViewById(R.id.iv_header);
            btnCallPhone = itemView.findViewById(R.id.iv_call_phone);
            //btnCallVideo = itemView.findViewById(R.id.iv_call_video);
            md5HeaderView = itemView.findViewById(R.id.md5_header_view);
            tvHistory = itemView.findViewById(R.id.tv_history);
            itemGride = itemView.findViewById(R.id.rl_layout);
            rootView = itemView;
        }
    }



    public static interface ItemClickListener{
        void onItemClick(ContactModel model,int position,int type);
    }
}
