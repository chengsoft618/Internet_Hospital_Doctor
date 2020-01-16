package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.personalmaterial.PersonalMaterialInfo;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadState;

/**
 * Created by W·H·K on 2018/8/3.
 */
// TODO: 2019/6/13  RecyclerView.Adapter
public class PersonalMaterialAdapter extends RecyclerView.Adapter<PersonalMaterialAdapter.ViewHolder> {
    private Context mContext;
    private List<PersonalMaterialInfo> mList;
    private OnItemDeleteClickListener mOnItemDeleteClickListener;
    private OnItemRetryClickListener mOnItemRetryClickListener;

    public PersonalMaterialAdapter(Context context, List<PersonalMaterialInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setOnItemDeleteClickListener(OnItemDeleteClickListener listener) {
        this.mOnItemDeleteClickListener = listener;
    }

    public void setOnItemRetryClickListener(OnItemRetryClickListener listener) {
        this.mOnItemRetryClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_personal_material_upload_queue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        displayUploadState(holder, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void displayUploadState(ViewHolder viewHolder, final int position) {
        viewHolder.mProgressBar.setProgress((int) mList.get(position).getUploadProgress());
        viewHolder.mFileName.setText(mList.get(position).getMaterialName());
        if (mList.get(position).getUploadState() == UploadState.UPLOAD_FAILED) {
            viewHolder.mProgressRedBar.setProgress((int) mList.get(position).getUploadProgress());
            viewHolder.mUploadStateTv.setText(mContext.getString(R.string.data_upload_fail));
            viewHolder.mUploadStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff3a3a));
            viewHolder.retryIm.setVisibility(View.VISIBLE);
            viewHolder.mProgressBar.setVisibility(View.GONE);
            viewHolder.mProgressRedBar.setVisibility(View.VISIBLE);
        } else if (mList.get(position).getUploadState() == UploadState.UPLOAD_SUCCESS) {
            viewHolder.mUploadStateTv.setText(mContext.getString(R.string.data_upload_success));
            viewHolder.mUploadStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_39d210));
            viewHolder.mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.mUploadStateTv.setText(mContext.getString(R.string.upload_ing));
            viewHolder.mUploadStateTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_45aef8));
            viewHolder.retryIm.setVisibility(View.INVISIBLE);
            viewHolder.mProgressBar.setVisibility(View.VISIBLE);
            viewHolder.mProgressRedBar.setVisibility(View.GONE);
        }
        viewHolder.mItemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemDeleteClickListener != null) {
                    mOnItemDeleteClickListener.onItemDeleteClick(v, position);
                }
            }
        });
        viewHolder.retryIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemRetryClickListener != null) {
                    mOnItemRetryClickListener.onItemRetryClick(v, position);
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mFileName;
        public TextView mUploadStateTv;
        public ImageView retryIm;
        public ImageView mItemRemove;
        public ProgressBar mProgressBar;
        public ProgressBar mProgressRedBar;

        public ViewHolder(View view) {
            super(view);
            mFileName = (TextView) view.findViewById(R.id.item_personal_material_view_file_name);
            mUploadStateTv = (TextView) view.findViewById(R.id.item_personal_material_view_upload_state);
            retryIm = (ImageView) view.findViewById(R.id.item_personal_material_view_type_img);
            mItemRemove = (ImageView) view.findViewById(R.id.item_personal_material_view_remove);
            mProgressBar = (ProgressBar) view.findViewById(R.id.item_personal_material_blue_progress_bar);
            mProgressRedBar = (ProgressBar) view.findViewById(R.id.item_personal_material_red_progress_bar);

        }
    }

    public interface OnItemDeleteClickListener {
        void onItemDeleteClick(View view, int position);
    }

    public interface OnItemRetryClickListener {
        void onItemRetryClick(View view, int position);
    }
}
