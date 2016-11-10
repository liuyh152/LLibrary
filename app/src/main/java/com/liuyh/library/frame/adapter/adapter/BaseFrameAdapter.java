package com.liuyh.library.frame.adapter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseFrameAdapter<T> extends BaseAdapter implements BaseFrameAdapterDrawer<T> {
    protected LayoutInflater mInflater;
    private Context context;
    private List<T> list;

    private AdapterListener mAdapterListener;


    public BaseFrameAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);// iflater写在这里，是为了优化不用创建多个LayoutInflater
        this.context = context;
    }

    /**
     * 重新设置数据，一般是用于刷新adapter
     *
     * @param data
     */
    public void resetData(List<T> data) {
        clearAdapter();
        addData(data);
    }

    /**
     * 清空adapter中的数据
     */
    public void clearAdapter() {
        if (null == list) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 追加数据，一般是用于分页
     *
     * @param data
     */
    public void addData(List<T> data) {
        if (null == data || data.isEmpty()) return;

        if (list == null) {
            list = new ArrayList<>();
        }
        list.addAll(data);
        notifyDataSetChanged();

        if (null != mAdapterListener) {
            mAdapterListener.onDataBindComplete(list.size() == 0);
        }
    }

    public void addData(T[] data) {
        if (null == data) return;
        addData(Arrays.asList(data));
    }

    @Override
    public int getCount() {
        if (null == list) {
            if (null != mAdapterListener) {
                mAdapterListener.onBeginBindData();
            }
            return 0;
        } else {
            return list.size();
        }
    }

//    public <V extends View> V findViewById(View convertView, int id) {
//        return ViewHolder.get(convertView, id);
//    }

    @Override
    public T getItem(int position) {
        if (position == list.size()) return null;
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = onViewCreate(position, mInflater, parent);
        }
        onViewAttach(position, getItem(position), convertView);

        return convertView;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public Context getContext() {
        return context;
    }

    public List<T> getData() {
        if (null == list) list = new ArrayList<T>();
        return list;
    }

    public void setAdapterListener(AdapterListener listener) {
        this.mAdapterListener = listener;
    }
}
