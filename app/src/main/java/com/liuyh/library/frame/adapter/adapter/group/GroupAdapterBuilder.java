package com.liuyh.library.frame.adapter.adapter.group;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建多模板adapter的工具类
 * Created by liuyuhang on 16/5/3.
 */
public abstract class GroupAdapterBuilder<T> {
    private List<GroupModelAdapter<T>> groupModels;

    public abstract String getTypeByPosition(List<T> data, int position);

    public GroupAdapterBuilder() {
    }

    public void addModel(GroupModelAdapter model) {
        if (null == groupModels) {
            groupModels = new ArrayList<GroupModelAdapter<T>>();
        }
        groupModels.add(model);
    }

    /**
     * 构建adapter
     * @param context
     * @return
     */
    public BaseGroupPagingAdapter buildAdapter(Context context) {
        BaseGroupPagingAdapter<T> adapter = new BaseGroupPagingAdapter<T>(context, groupModels) {
            @Override
            public String getTypeByPosition(List<T> data, int position) {
                return GroupAdapterBuilder.this.getTypeByPosition(data, position);
            }
        };
        return adapter;
    }
}
