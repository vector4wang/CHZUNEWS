package com.chzu.getnews;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.chzu.adapter.NewsListAdapter;
import com.chzu.bean.NewsTitle;
import com.chzu.dao.NewsTitleDao;
import com.chzu.util.AppUtil;
import com.chzu.util.Logger;
import com.chzu.util.NetUtil;
import com.chzu.util.NewsDetailUtil;
import com.chzu.util.URLDetail;


@SuppressLint("InflateParams")
public class MainFragment extends Fragment implements IXListViewRefreshListener, IXListViewLoadMore{

	private static final int LOAD_REFREASH = 0x111;
	
	private static final int TIP_ERROR_NO_NETWORK = 0X112;
	private static final int TIP_ERROR_SERVER = 0X113;
	
	/**
	 * 是否第一次进入
	 */
	private boolean isFirstIn = true;
	
	/**
	 * 是否连接网络
	 */
	private boolean isConnNet = false;
	
	/**
	 * 当前数据是否从网络获取
	 */
	@SuppressWarnings("unused")
	private boolean isLoadingDataFromNetWork;
	
	
	/**
	 * 默认的newType
	 */
	private int newsType = URLDetail.NEWS_LIST_WYYW;
	
	/**
	 * 处理新闻的工具类
	 */
	private NewsDetailUtil newsDetailUtil;
	
	/**
	 * 与数据库交互
	 */
	private NewsTitleDao mNewsTitleDao;
	
	/**
	 * 扩展的ListView
	 */
	private XListView mXListView;
	
	/**
	 * 数据适配器
	 */
	private NewsListAdapter mAdapter;
	
	/**
	 * 数据
	 */
	private List<NewsTitle> mDatas = new ArrayList<NewsTitle>();
	
	/**
	 * 获得newstype
	 * @param newsType
	 */
	public MainFragment(int newsType){
		this.newsType = newsType;
		newsDetailUtil = new NewsDetailUtil();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tab_item_fragment_main, null);	}


	@Override
	public void onActivityCreated( Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mNewsTitleDao = new NewsTitleDao(getActivity());
		mAdapter = new NewsListAdapter(getActivity(), mDatas);
		
		/**
		 * 初始化
		 */
		mXListView = (XListView) getView().findViewById(R.id.id_xlistView);
		mXListView.setAdapter(mAdapter);
		mXListView.setPullRefreshEnable(this);
		mXListView.setPullLoadEnable(this);
		mXListView.setRefreshTime(AppUtil.getRefreashTime(getActivity(), newsType));
		
		mXListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NewsTitle newsTitle = mDatas.get(position-1);
				Intent intent = new Intent(getActivity(), NewsContentActivity.class);
				intent.putExtra("url", newsTitle.getLink());
				startActivity(intent);
			}
		});
		if(isFirstIn){
			/**
			 * 进来时直接刷新
			 */
			mXListView.startRefresh();
			isFirstIn = false;
		}else{
			mXListView.NotRefreshAtBegin();
		}
	}


	@Override
	public void onLoadMore() {
		
	}

	@Override
	public void onRefresh() {
		new LoadDatasTask().execute(LOAD_REFREASH);
	}
	
	public Integer refreashData(){
		if(NetUtil.checkNet(getActivity())){
			isConnNet = true;
			//获取数据
			try{
				List<NewsTitle> newsTitles = newsDetailUtil.getNewsList(newsType);
				mAdapter.setDatas(newsTitles);
				
				isLoadingDataFromNetWork = true;
				//设置刷新时间
				AppUtil.setRefreashTime(getActivity(), newsType);
				//清楚数据库数据
				mNewsTitleDao.deleteAll(newsType);
				//存入数据库
				mNewsTitleDao.add(newsTitles);
			}catch(Exception e){
				e.printStackTrace();
				isLoadingDataFromNetWork = false;
				return TIP_ERROR_SERVER;
			}
		}else{
			Logger.e("无网络连接!");
			isConnNet = false;
			isLoadingDataFromNetWork = false;
			//从数据库加载
			List<NewsTitle> newsTitle = mNewsTitleDao.list(newsType);
			mDatas = newsTitle;
			return TIP_ERROR_NO_NETWORK;
		}
		return -1;
	}
	
	/**
	 * 记载数据的异步任务
	 * @author Administrator
	 *
	 */
	class LoadDatasTask extends AsyncTask<Integer, Void, Integer>
	{
		
		@Override
		protected void onPostExecute(Integer result)
		{
			switch (result) {
			case TIP_ERROR_NO_NETWORK:
				Toast.makeText(getActivity(), "没有网络连接!", Toast.LENGTH_SHORT).show();
				mAdapter.setDatas(mDatas);
				mAdapter.notifyDataSetChanged();
				break;
			case TIP_ERROR_SERVER:
				Toast.makeText(getActivity(), "服务器错误!", Toast.LENGTH_SHORT).show();
			default:
				break;
			}
			mXListView.setRefreshTime(AppUtil.getRefreashTime(getActivity(), newsType));
			mXListView.stopRefresh();
			mXListView.stopLoadMore();
		}
		@Override
		protected Integer doInBackground(Integer... params) {
			if(params[0] == LOAD_REFREASH){
				return refreashData();
			}
			return -1;
		}
	}
	
}
