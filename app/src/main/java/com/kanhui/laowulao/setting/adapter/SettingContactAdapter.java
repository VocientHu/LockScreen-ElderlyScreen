package com.kanhui.laowulao.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.locker.adapter.ContactAdapter;
import com.kanhui.laowulao.locker.model.ContactModel;
import com.kanhui.laowulao.setting.config.ContactConfig;
import com.kanhui.laowulao.utils.LogUtils;
import com.kanhui.laowulao.utils.SharedUtils;
import com.kanhui.laowulao.widget.Md5HeaderView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SettingContactAdapter extends RecyclerView.Adapter<SettingContactAdapter.ViewHolder> {

    public static final int TYPE_NOMAL = 1;
    public static final int TYPE_ADD = 2;

    private Context context;
    private List<ContactModel> list = new ArrayList<>();

    public SettingContactAdapter(Context context){
        this.context = context;
    }

    public void setData(List<ContactModel> list){
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == TYPE_ADD) {
            View view = LayoutInflater.from(context).inflate(R.layout.contact_add,null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onAdd();
                    }
                }
            });
            return new ViewHolder(view);
        }

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gride_contact,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_ADD){
            return;
        }
        ContactModel model = list.get(position);
        holder.tvName.setText(model.getName());
        if(config != null){
            holder.tvName.setTextSize(config.getNameSize());
        }
        holder.tvPhone.setText(model.getPhone());
        holder.tvDelete.setVisibility(View.VISIBLE);
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onDelete(position);
                }
            }
        });
        int bgColor = getCurrentBgResId(model.getName());
        holder.itemGride.setBackgroundColor(context.getResources().getColor(bgColor));
        holder.tvHistory.setVisibility(View.GONE);
        holder.tvPhone.setTextColor(context.getResources().getColor(R.color.white));
    }

    @Override
    public int getItemCount() {

        return list == null ? 1 : list.size() < 4 ? list.size()+1 : 4;
    }

    @Override
    public int getItemViewType(int position) {
        int count = list.size();

        return count == 4 ? TYPE_NOMAL : position == list.size() ? TYPE_ADD : TYPE_NOMAL;
    }

    private int getCurrentBgResId(String text){
        int number = Md5HeaderView.getMd5Index(text);
        int color;
        int i = number%ContactAdapter.colors.length;
        color = ContactAdapter.colors[i];
        return color;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName,tvPhone,tvHistory,tvDelete;
        View itemGride;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvHistory = itemView.findViewById(R.id.tv_history);
            itemGride = itemView.findViewById(R.id.rl_layout);

        }
    }

    private ContactConfig config;

    public void refreshSize(ContactConfig config){
        if(config == null){
            config = SharedUtils.getInstance().getContactConfig();
        }
        this.config = config;
        notifyDataSetChanged();
    }

    private SettingContactAddListener listener;

    public SettingContactAddListener getListener() {
        return listener;
    }

    public void setListener(SettingContactAddListener listener) {
        this.listener = listener;
    }

    public interface SettingContactAddListener{
        void onAdd();

        void onDelete(int position);
    }
}
