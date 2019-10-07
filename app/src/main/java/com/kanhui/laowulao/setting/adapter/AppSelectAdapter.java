package com.kanhui.laowulao.setting.adapter;

import android.content.Context;
import android.content.Entity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.locker.model.AppsModel;

import java.util.ArrayList;
import java.util.List;

public class AppSelectAdapter extends RecyclerView.Adapter<AppSelectAdapter.ViewHolder> {

    private List<AppEntity> list = new ArrayList<>();

    private Context context;

    public AppSelectAdapter(Context context){
        this.context = context;
    }

    public void setData(List<AppEntity> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_app_select,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final AppEntity entity = list.get(position);
        holder.tvName.setText(entity.getName());
        holder.cbChoice.setChecked(entity.isChecked);
        holder.cbChoice.setClickable(false);
        holder.ivIcon.setImageDrawable(entity.getIcon());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onClick(position,entity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private OnClickListner listener;

    public OnClickListner getListener() {
        return listener;
    }

    public void setListener(OnClickListner listner) {
        this.listener = listner;
    }

    public interface OnClickListner{
        void onClick(int index,AppEntity model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivIcon;
        TextView tvName;
        CheckBox cbChoice;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            cbChoice = itemView.findViewById(R.id.cb_select);
            view = itemView;
        }
    }

    public static class AppEntity{
        String name;
        Drawable icon;
        String packageName;
        boolean isChecked = false;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }
    }
}
