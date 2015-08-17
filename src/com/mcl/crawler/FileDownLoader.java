package com.mcl.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class FileDownLoader {
	public static boolean createFile(String destFileName) {  
        File file = new File(destFileName);  
        if(file.exists()) {  
            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
            return false;  
        }  
        if (destFileName.endsWith(File.separator)) {  
            System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");  
            return false;  
        }  
        //判断目标文件所在的目录是否存在  
        if(!file.getParentFile().exists()) {  
            //如果目标文件所在的目录不存在，则创建父目录  
            //System.out.println("目标文件所在目录不存在，准备创建它！");  
            if(!file.getParentFile().mkdirs()) {  
                System.out.println("创建目标文件所在目录失败！");  
                return false;  
            }  
        }  
        //创建目标文件  
        try {  
            if (file.createNewFile()) {  
                return true;  
            } else {  
                return false;  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
            System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());  
            return false;  
        }  
    } 
	

	/**保存网页字节数组到本地文件
	 * filePath 为要保存的文件的相对地址
	 */
	private void saveToLocal(String content,String filePath)
	{
		try {
			createFile(filePath);
			OutputStreamWriter osw;
			String encoding = "UTF-8";
			osw = new OutputStreamWriter(
					new FileOutputStream(filePath), encoding);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(content);

			bw.close();
			osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*下载 url 指向的网页的帖子内容并存储*/
	public String  downloadFile(String url,String filePath)
	{
		System.out.println("filePath: "+filePath);
		Parser parser;
		try {
			parser = new Parser(url);
			parser.setEncoding("UTF-8");
			NodeList tableOfPre1 = parser.extractAllNodesThatMatch( 
				    (NodeFilter) new AndFilter(new NodeClassFilter(TableTag.class), new HasAttributeFilter("class", "main")));
				if(tableOfPre1 != null && tableOfPre1.size() > 0) { 
					 // 获取指定 table 标签的子节点中的 <tbody> 节点
					 NodeList tList = tableOfPre1.elementAt(0).getChildren().extractAllNodesThatMatch (new TagNameFilter("tr"), true);
					 //第二列第一个tag：pre
					 String text = tList.elementAt(1).getFirstChild().toPlainTextString();
					 //System.out.println(text);
					 saveToLocal(text,filePath);
					 
				}
			} catch (ParserException e) {
				e.printStackTrace();
			}
		return null;
	}
	//测试的 main 方法
	public static void main(String[]args)
	{
		FileDownLoader downLoader = new FileDownLoader();
		downLoader.downloadFile("http://bbs.nju.edu.cn/bbstcon?board=Blog&file=M.1373384270.A","temp/Blog/我私人区最后一篇文章自动消失了，能找回吗？.txt");
	}
}