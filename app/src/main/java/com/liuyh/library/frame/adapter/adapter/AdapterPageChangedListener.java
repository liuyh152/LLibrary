package com.liuyh.library.frame.adapter.adapter;

/**
 * 监听adapter分页的接口
 * 
 * @author liuyuhang
 *
 */
public interface AdapterPageChangedListener {

	/**
	 * 下面还有数据，可以继续翻页
	 */
	void mayHaveNextPage();

	/**
	 * 已加载完全部数据，停止分页
	 */
	void noMorePage();
}
