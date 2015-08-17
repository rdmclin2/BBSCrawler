package com.mcl.crawler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

//获取bbs网站上某板块的所有帖子链接
public class FileParser {
	//每个板块爬取1000个左右的帖子
	private final int LINK_UP = 1000;
	
	Map<String,String> links = new HashMap<String,String>();
	private  String extracLinks(String url,LinkFilter filter) {
		//下一页的链接
		String nextPage = null;
		try {
			Parser parser = new Parser(url);
			parser.setEncoding("gb2312");
			// linkFilter 来设置过滤 <a> 标签
			NodeClassFilter linkFilter = new NodeClassFilter(
					LinkTag.class);
			// 得到所有经过过滤的标签
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				if (tag instanceof LinkTag)// <a> 标签
				{
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();// url
					
					//获取上一页的链接，作为下一步的链接
					if(link.getLinkText().equals("上一页")){
						nextPage = linkUrl;
					}
					
					if(filter.accept(linkUrl) && links.size() < LINK_UP){
						String title = link.getLinkText();
						links.put(linkUrl, title);
					}
				} 
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return nextPage;
	}
			
			
		//获取板块链接并切换成主题模式
		public  Map<String, String> getFileUrls(String seed)
		{
			System.out.println("seed: "+ seed);
			links.clear();
			int linkCount = 0;
			final String rule = seed.replace("bbstdoc", "bbstcon");
			String list = seed;
			while(linkCount < LINK_UP){
				list = extracLinks(list,new LinkFilter()
				{
					//提取以 http://bbs.nju.edu.cn/bbstcon?board=... 开头的链接
					public boolean accept(String url) {
						if(url.startsWith(rule))
							return true;
						else
							return false;
					}
				});
				linkCount = links.size();
				//如果没有下一页的链接了
				if(list == null){
					break;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("共 "+ links.size()+" 篇帖子");
			for (Entry<String, String> entry : links.entrySet()) {
				System.out.println(entry.getKey()+" "+entry.getValue());
			}
			return links;
		}
			
		//测试的 main 方法
		public static void main(String[]args){
			FileParser parser = new FileParser();
			parser.getFileUrls("http://bbs.nju.edu.cn/bbstdoc?board=Blog");
		}
}
