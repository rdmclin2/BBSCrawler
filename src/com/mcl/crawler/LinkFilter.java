package com.mcl.crawler;

public interface LinkFilter {
	public boolean accept(String url);
}