package com.liuyh.library.frame.adapter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liuyuhang on 16/5/12.
 */
public interface BaseFrameAdapterDrawer<T> {

    /**
     * 在adapter的getView中，首次创建view，初始化并返回创建的布局
     *
     * @param position
     * @param inflater
     * @param parent
     * @return
     */
    View onViewCreate(int position, LayoutInflater inflater, ViewGroup parent);

    /**
     * 在adapter的getView中，每次滑动listView会循环执行本方法
     *
     * @param position
     * @param item
     * @param convertView
     */
    void onViewAttach(int position, T item, View convertView);

}
