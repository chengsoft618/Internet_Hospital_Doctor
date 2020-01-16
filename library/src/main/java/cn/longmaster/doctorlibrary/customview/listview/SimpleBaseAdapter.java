package cn.longmaster.doctorlibrary.customview.listview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;


/**
 * <p>Created by yangyong on 2015-07-08.</p>
 */
public abstract class SimpleBaseAdapter<Data, ViewHolder> extends BaseAdapter {

    protected Context context;
    protected List<Data> dataList = new ArrayList<>();
    private OnAdapterItemClickListener<Data> onAdapterItemClickListener;

    public SimpleBaseAdapter(Context context) {
        this.context = context;
    }

    public void setOnAdapterItemClickListener(OnAdapterItemClickListener<Data> onAdapterItemClickListener) {
        this.onAdapterItemClickListener = onAdapterItemClickListener;
    }

    public void remove(int position) {
        dataList.remove(position);
        notifyDataSetChanged();
    }

    public void remove(Data data) {
        dataList.remove(data);
        notifyDataSetChanged();
    }

    public void addToLast(Data data) {
        dataList.add(data);
        notifyDataSetChanged();
    }

    public void addToLast(List<Data> data) {
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public void addToFirst(List<Data> data) {
        dataList.addAll(0, data);
        notifyDataSetChanged();
    }

    public void addToFirst(Data data) {
        dataList.add(0, data);
        notifyDataSetChanged();
    }

    public void clear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加要显示的数据到末尾 注意：调用本方法设置数据，listView不需要再调用：notifyDataSetChanged
     *
     * @param datas 数据列表
     */
    public void addData(List<Data> datas) {
        this.dataList.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 获取要所有的数据
     *
     * @return 适配器数据源
     */
    public List<Data> getData() {
        return new ArrayList<>(dataList);
    }

    /**
     * 设置要显示的数据，注意：调用本方法设置数据，listView不需要再调用：notifyDataSetChanged
     *
     * @param datas 数据
     */
    public void setData(List<Data> datas) {
        this.dataList.clear();
        addData(datas);
    }

    /**
     * 获取适配器的最后一项，如果适配器大小等于0，将返回null
     */
    public Data getLastItem() {
        int count = getCount();
        if (count == 0) {
            return null;
        } else {
            return getItem(count - 1);
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Data getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = onNewViewHolder();
            convertView = LayoutInflater.from(context).inflate(getLayoutId(), parent, false);
            ViewInjecter.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Data data = getItem(position);
        bindView(viewHolder, data, position);
        convertView.setOnClickListener(new OnItemClickListener(data, position));
        return convertView;
    }

    /**
     * 获得资源文件的ID
     *
     * @return 资源文件id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void bindView(ViewHolder viewHolder, Data data, int position);

    @NonNull
    protected abstract ViewHolder onNewViewHolder();

    public interface OnAdapterItemClickListener<Data> {
        void onAdapterItemClick(int position, Data data);
    }

    private class OnItemClickListener implements View.OnClickListener {
        Data data;
        int position;

        public OnItemClickListener(Data data, int position) {
            this.data = data;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (onAdapterItemClickListener != null) {
                onAdapterItemClickListener.onAdapterItemClick(position, data);
            }
        }
    }
}
