package com.liuyh.library.frame.adapter.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.List;

/**
 * 带有section的Adapter
 * Created by liuyuhang on 16/5/6.
 */
@SuppressWarnings("ResourceType")
public abstract class BaseSectionAdapter<T> extends BasePagingFrameAdapter<T> {
    private int mSectionDefaultID = 10001011;


    /**
     * 用来记录每个section的数量
     */
    public SparseArray<List<T>> mDataCount;
    /**
     * 记录section位置的hashmap
     */
    private HashMap<Integer, Boolean> mSectionPosition;

//    /**
//     * 返回当前position是否带有section
//     *
//     * @param position
//     * @return
//     */
//    public abstract boolean onSectionBind(int position);

    /**
     * sectionView的绘制
     *
     * @return 返回SectionView
     */
    public abstract int onSectionViewCreate();

    /**
     * 根据数据，绘制sectionView
     *
     * @param sectionView
     * @param sectionPosition
     * @param item
     * @return 返回sectionView的LayoutID
     */
    public abstract void onSectionViewAttach(View sectionView, int sectionPosition, T item);


    public BaseSectionAdapter(Context context) {
        super(context);

        mDataCount = new SparseArray<>();
        mSectionPosition = new HashMap<>();
    }

    /**
     * 返回当前position是否带有section
     * @param position
     * @return
     */
    public boolean onSectionBind(int position) {
//        if (mDataCount.get(PRODUCT_TYPE_SELF) == null || mDataCount.get(PRODUCT_TYPE_SELF).size() == 0) {
//            return position == 0;
//        } else {
//            return (position == 0 || position == mDataCount.get(PRODUCT_TYPE_SELF).size());
//        }

//        int count = mDataCount.size();
//
//        return position == 0 ||
        Boolean isSection = mSectionPosition.get(position);
        return null == isSection ? false : isSection;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        boolean isSection = onSectionBind(position);
        if (isSection) {
            return BaseAdapterViewType.VIEW_TYPE_SECTION;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case BaseAdapterViewType.VIEW_TYPE_SECTION:
                LinearLayout sectionParent = (LinearLayout) convertView;
                View sectionView, //sectionView
                        contentView; //getView中的内容

                if (null == sectionParent) {
                    sectionParent = new LinearLayout(getContext());
                    sectionParent.setOrientation(LinearLayout.VERTICAL);

                    //section
                    sectionView = getInflater().inflate(onSectionViewCreate(), null);
                    sectionView.setId(mSectionDefaultID);

                    //content
                    contentView = super.getView(position, convertView, parent);

                    sectionParent.addView(sectionView);
                    sectionParent.addView(contentView);
                } else {
                    sectionView = sectionParent.findViewById(mSectionDefaultID);
                    super.getView(position, convertView, parent);
                }
                onSectionViewAttach(sectionView, position, getItem(position));
                return sectionParent;
            default:
                return super.getView(position, convertView, parent);
        }
    }

    /**
     * 将数据加入到adapter，单独这么加入是为了计算section的位置
     *
     * @param type
     */
    public void putData(int type, List<T> data) {
        mSectionPosition.put(getData().size(), true);
        mDataCount.put(type, data);
        addData(data);
    }

    @Override
    public void clearAdapter() {
        super.clearAdapter();

        mSectionPosition.clear();
        mDataCount.clear();
    }
}
