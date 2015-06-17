package com.chzu.util;

public class URLUtil {
	/**
	 * 蔚园要问
	 * 院部动态
	 * 通知公告
	 * 教科研信息
	 */
	public static final String NEWS_LIST_WYYW = "http://www.chzu.edu.cn/s/1/t/1152/p/2/list.htm";
	public static final String NEWS_LIST_YBDT = "http://www.chzu.edu.cn/s/1/t/1152/p/3/list.htm";
	public static final String NEWS_LIST_TZGG = "http://www.chzu.edu.cn/s/1/t/1152/p/4/list.htm";
	public static final String NEWS_LIST_JKYXX = "http://www.chzu.edu.cn/s/1/t/1152/p/5/list.htm";
	
	public static String gengrateURL(int newsType){
		String urlStr = "";
		switch (newsType)
		{
		case URLDetail.NEWS_LIST_WYYW:
			urlStr = NEWS_LIST_WYYW;
			break;
		case URLDetail.NEWS_LIST_YBDT:
			urlStr = NEWS_LIST_YBDT;
			break;
		case URLDetail.NEWS_LIST_TZGG:
			urlStr = NEWS_LIST_TZGG;
			break;
		case URLDetail.NEWS_LIST_JKYXX:
			urlStr = NEWS_LIST_JKYXX;
			break;
		default:
			urlStr = NEWS_LIST_WYYW;
			break;
		}
		return urlStr;
	}
}
