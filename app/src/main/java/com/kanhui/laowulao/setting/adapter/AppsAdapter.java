package com.kanhui.laowulao.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kanhui.laowulao.R;
import com.kanhui.laowulao.config.Config;
import com.kanhui.laowulao.locker.model.AppsModel;
import com.kanhui.laowulao.setting.config.AppConfig;
import com.kanhui.laowulao.utils.AppUtils;
import com.kanhui.laowulao.utils.SharedUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    public static final int TYPE_NOMAL = 1;
    public static final int TYPE_ADD = 2;

    List<AppsModel> list = new ArrayList<>();

    private View headerView;

    private Context context;

    private boolean needHeaderView = false;

    public AppsAdapter(Context context){
        this.context = context;
    }

    public AppsAdapter(Context context, boolean addHeaderView){
        this.context = context;
        this.needHeaderView = addHeaderView;
        if(addHeaderView == true){
            headerView = LayoutInflater.from(context).inflate(R.layout.contact_add,null);
            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onAdd();
                    }
                }
            });
        }
    }

    public void setData(List<AppsModel> apps){
        this.list = apps;
        notifyDataSetChanged();
    }

    private AppConfig config;

    public void refreshSize(AppConfig config){
        if(config == null){
            config = SharedUtils.getInstance().getAppConfig();
        }
        this.config = config;
        notifyDataSetChanged();
    }

    public View getHeaderView() {
        return headerView;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_ADD && headerView != null) return new ViewHolder(headerView);
        View view = LayoutInflater.from(context).inflate(R.layout.item_apps,null);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if(!needHeaderView) return TYPE_NOMAL;

        return position == list.size() ? TYPE_ADD : TYPE_NOMAL;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_ADD) return;
        final AppsModel model = list.get(position);
        holder.tvName.setText(model.getAppName());
        holder.ivIcon.setImageDrawable(AppUtils.getInstance(context).getIcon(model.getAppName()));
        if(config != null){
            holder.tvName.setTextSize(config.getNameSize());
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.ivIcon.getLayoutParams();
            int pxSize = Config.dp2px(context,config.getIconSize());
            params.width = pxSize;
            params.height = pxSize;
            holder.ivIcon.setLayoutParams(params);
        }
        if(needHeaderView){
            holder.tvDelete.setVisibility(View.VISIBLE);
        } else {
            holder.tvDelete.setVisibility(View.GONE);
        }
        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onClick(position,model);
                }
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onDelete(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(!needHeaderView) return list.size();
        return list == null ? 1 : list.size()+1;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView tvName,tvDelete;
        ImageView ivIcon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_app_name);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            ivIcon = itemView.findViewById(R.id.iv_icon);
        }
    }

    private OnItemClickListener listener;

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onDelete(int position);
        void onClick(int position,AppsModel model);
        void onAdd();
    }
}
