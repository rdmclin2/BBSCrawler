package com.mcl.crawler;

import java.util.Dictionary;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
public class Crawler {
	private final String dir = "temp";
	/* 爬取方法*/
	public void crawling(String seed)
	{
		FileParser fileParser = new FileParser();
		BoardParser boardParser = new BoardParser();
		FileDownLoader downLoader=new FileDownLoader();
		
		//第一层获取所有板块帖子
		Set<String> boardsSet = boardParser.getBoards(seed);
		for (String visitUrl : boardsSet) {
			String board = visitUrl.trim().split("=")[1];
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Map<String, String> fileMap = fileParser.getFileUrls(visitUrl);
			//第三层，下载每个帖子的内容
			for (Entry<String, String> entry : fileMap.entrySet()) {
				String id = entry.getKey().split("=")[2];
				String path = dir+"/"+board+"/"+entry.getValue().trim()+"_"+id+".txt";
				downLoader.downloadFile(entry.getKey(),path);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	//main 方法入口
	public static void main(String[]args)
	{
		Crawler crawler = new Crawler();
		crawler.crawling("http://bbs.nju.edu.cn/bbsall");
	}
}