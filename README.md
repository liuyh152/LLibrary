# LLibrary

基础用法

	ItemListAdapter adapter = new ItemListAdapter(this, list);
	new ViewHelper(this).id(id.listview_main).adapter(adapter).itemListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ToastUtils.showMessage(getApplicationContext(), "点击了菜单：" + position);
				LogWatcher.getInstance().putMessage("点击了菜单：" + position);

				if (position == 0) {
					push(HttpDemoActivity.class);
				} else if (position == 1) {
					push(GroupListActivity.class);
				} else {
					push(SecondActivity.class);
				}
			}
		});

		// ****分页代码
		adapter.setOnPagingListener(new PagingListener<String>() {

			@Override
			public void onNextPageRequest(final BasePagingFrameAdapter<String> adapter, final int page) {
				// 以下是模拟网络请求
				if (page == 3) {
					adapter.noMorePage();// 已经没有更多了~~
				} else {
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							List<String> list = new ArrayList<String>();
							for (int i = 0; i < 20; i++) {
								if (i == 0) {
									list.add("============第" + page + "页============");
								} else {
									list.add("菜单" + i);
								}
							}
							adapter.addData(list);// 模拟从服务器请求回数据
							adapter.mayHaveNextPage();// 设置加载完毕，可以再次翻页
						}
					}, 2000);
				}
			}
		});
