package com.avolord.core;

public class Main {

	public static void main(String[] args) {
		String path = "resources/test.txt";
		if(args.length == 1) {
			path = args[0];
		}
		
		DataAnalyzer dataAnalyzer = new DataAnalyzer();
		dataAnalyzer.start(path);
	}

}
