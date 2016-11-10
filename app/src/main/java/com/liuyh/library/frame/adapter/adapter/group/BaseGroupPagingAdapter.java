package com.liuyh.library.frame.adapter.adapter.group;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuyh.library.frame.adapter.adapter.BaseAdapterViewType;
import com.liuyh.library.frame.adapter.adapter.BasePagingFrameAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Item分组（每个ItemLayout不相同），并且带有分页的adapter
 *
 * @param <T>
 * @author liuyuhang
 */
public abstract class BaseGroupPagingAdapter<T> extends BasePagingFrameAdapter<T> {
    private HashMap<String, Holder> mModelMap;// 存放model的模板，根据模板ID可以拿到当前model;
    private SparseArray<String> mModelIDMap;// 存放modelID的模板，根据当前type可以拿到modelID;

    // private HashMap<Integer, GroupModelAdapter<T>>
    public abstract String getTypeByPosition(List<T> data, int position);

    public BaseGroupPagingAdapter(Context context, List<GroupModelAdapter<T>> groupModels) {
        super(context);

        this.mModelMap = new HashMap<String, Holder>();
        this.mModelIDMap = new SparseArray<String>();

        int startPosition = getTypeCounts() + 1;
        for (int i = 0; i < groupModels.size(); i++) {// 遍历model，初始化并做记录
            GroupModelAdapter<T> groupModelAdapter = groupModels.get(i);
            groupModelAdapter.onModelCreate(getContext());// 调用每个model模板的初始化方法

            int type = startPosition + i;// 为每个模板分配type，type为目前有的typeCount+i;

            Holder holder = new Holder();
            holder.position = type;
            holder.groupModelAdapter = groupModelAdapter;
            this.mModelMap.put(groupModelAdapter.getGroupModelID(), holder);

            this.mModelIDMap.put(type, groupModelAdapter.getGroupModelID());

            // System.out.println("init type: " + type + "    model id: " +
            // groupModelAdapter.getGroupModelID());
        }
    }

    @Override
    public int getViewTypeCount() {
        return getTypeCounts() + 2;// 应该是+1，但是因为有可能有不存在的布局(返回了没有定义的布局)，所以预留出一位
    }

    /**
     * 默认有的TypeCount
     *
     * @return
     */
    private int getTypeCounts() {
        return super.getViewTypeCount() + mModelMap.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (super.getItemViewType(position) == BaseAdapterViewType.VIEW_TYPE_CONTENT) {
            // System.out.println("return type is: " +
            // mGroupPostion.get(mGroupPostion.get(getTypeByPosition(position)).groupModelAdapter.getGroupModelID()).position
            // + " getViewTypeCount is: " + getViewTypeCount() );

            BaseGroupPagingAdapter<T>.Holder holder = mModelMap.get(getTypeByPosition(getData(), position));// 查找到当前position的holder
            if (holder == null) {
                return getTypeCounts() + 1;// 返回一个不存在的ErrorType
            } else {
                return mModelMap.get(holder.groupModelAdapter.getGroupModelID()).position;
            }
        } else {
            return super.getItemViewType(position);
        }
    }

    /**
     * 根据modelID拿到type
     *
     * @param type
     * @return
     */
    private String getModelIDByType(int type) {
        return mModelIDMap.get(type);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type <= super.getViewTypeCount()) {
            // System.out.println("getView model type is: loading view");
            return super.getView(position, convertView, parent);
        } else if (type == (getTypeCounts() + 1)) {// ErrorType
            if (convertView == null) {// ，并且没有设置过布局
//				TextView errorView = new TextView(getContext());
//				errorView.setText("Wrong ModelID");
//				convertView = errorView;
                convertView = new View(getContext());
            }
        } else {
            // System.out.println("getView getModelIDByType(type)" +
            // getModelIDByType(type));
            // System.out.println("getView model type is: " +
            // mModelMap.get(getModelIDByType(type)).groupModelAdapter.getGroupModelID());
            GroupModelAdapter<T> groupModelAdapter = mModelMap.get(getModelIDByType(type)).groupModelAdapter;
            // System.out.println("groupModelAdapter:" + groupModelAdapter);

            if (convertView == null) {
                convertView = groupModelAdapter.getGroupViewCreate(position, getInflater(), parent);
            }
            groupModelAdapter.onGroupViewAttach(position, getItem(position), convertView);
        }
        return convertView;
    }

    @Override
    public View onViewCreate(int position, LayoutInflater inflater, ViewGroup parent) {
        // empty
        return null;
    }

    @Override
    public void onViewAttach(int position, T item, View convertView) {
        // empty
    }

    private class Holder {
        public int position;
        public GroupModelAdapter<T> groupModelAdapter;
    }
}
