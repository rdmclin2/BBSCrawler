package com.mcl.crawler;

import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class BoardParser {
		// 获取bbs网站上的所有板块链接
		private    Set<String> extracLinks(String url,LinkFilter filter) {

			Set<String> links = new HashSet<String>();
			try {
				Parser parser = new Parser(url);
				parser.setEncoding("UTF-8");
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
						if(filter.accept(linkUrl)){
						//将一般模式切换成主题模式
							String stlink = linkUrl.replace("bbsdoc", "bbstdoc");
							links.add(stlink);
						}
					} 
				}
			} catch (ParserException e) {
				e.printStackTrace();
			}
			return links;
		}
		
		//获取板块链接并切换成主题模式
		public  Set<String> getBoards(String seed)
		{
			Set<String> links = extracLinks(
			seed,new LinkFilter()
			{
				//提取以 http://bbs.nju.edu.cn/bbsdoc 开头的链接
				public boolean accept(String url) {
					if(url.startsWith("http://bbs.nju.edu.cn/bbsdoc"))
						return true;
					else
						return false;
				}
			});
//			System.out.println(links.size());
//			for(String link : links){
//				System.out.println(link);
//			}
			return links;
		}
		
		//测试的 main 方法
//		public static void main(String[]args){
//			BoardParser.getBoards("http://bbs.nju.edu.cn/bbsall");
//		}
}
