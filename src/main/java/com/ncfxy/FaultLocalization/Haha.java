package com.ncfxy.FaultLocalization;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Haha {
	public static void haha() throws FileNotFoundException, IOException {
		
		String str = "abc";
		Pattern pattern = Pattern.compile("[[:lower:]!]");
		Matcher matcher = pattern.matcher(str);
		while(matcher.find()){
			System.out.println(matcher.group());
		}
	}
	
	public static void main(String[] args) {
		try {
			haha();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
