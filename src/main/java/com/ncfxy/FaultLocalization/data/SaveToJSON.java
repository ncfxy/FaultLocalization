package com.ncfxy.FaultLocalization.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SaveToJSON {

	public static void saveToJson(Object map) throws IOException {

		try (OutputStream output = new FileOutputStream(new File("result.json"));) {
			Gson gson = new Gson();
			gson = new GsonBuilder().setPrettyPrinting().create();
			String jsonString = gson.toJson(map);
			//System.out.println(jsonString);
			output.write(jsonString.getBytes());
		}
	}

	public static void convertFromJsonToCsv() throws IOException {
		try (InputStream input = new FileInputStream(new File("result.json"));
				OutputStream outputFirst = new FileOutputStream(new File("resultFirst.csv"));
				OutputStream outputLast = new FileOutputStream(new File("resultLast.csv"));) {
				
			Scanner cin = new Scanner(input);
			StringBuffer buffer = new StringBuffer();
			while(cin.hasNext()){
				buffer.append(cin.nextLine());
			}
			Gson gson = new Gson();
			gson = new GsonBuilder().create();
			List list = gson.fromJson(buffer.toString(), List.class);
			List<Map<String,List>> mapList = (List<Map<String,List>>)list;
			StringBuffer outputFirstBuffer = new StringBuffer();
			outputFirstBuffer.append("Tarantula,");
			outputFirstBuffer.append("Ochiai,");
			outputFirstBuffer.append("OP2,");
			outputFirstBuffer.append("GAMFal,");
			outputFirstBuffer.append("GAMFalLinear\n");
			StringBuffer outputLastBuffer = new StringBuffer();
			outputLastBuffer.append(outputFirstBuffer);
			for(Map<String,List> map : mapList){
				List tmp = map.get("Tarantula");
				outputFirstBuffer.append(tmp.get(0)+",");
				outputLastBuffer.append(tmp.get(1)+",");
				tmp = map.get("Ochiai");
				outputFirstBuffer.append(tmp.get(0)+",");
				outputLastBuffer.append(tmp.get(1)+",");
				tmp = map.get("OP2");
				outputFirstBuffer.append(tmp.get(0)+",");
				outputLastBuffer.append(tmp.get(1)+",");
				tmp = map.get("GAMFal");
				outputFirstBuffer.append(tmp.get(0)+",");
				outputLastBuffer.append(tmp.get(1)+",");
				tmp = map.get("GAMFalLinear");
				outputFirstBuffer.append(tmp.get(0)+"\n");
				outputLastBuffer.append(tmp.get(1)+"\n");
			}
			outputFirst.write(outputFirstBuffer.toString().getBytes());
			outputLast.write(outputLastBuffer.toString().getBytes());
			System.out.println(outputFirstBuffer.toString());
		}
	}

	public static void main(String[] args) {
		try {
			convertFromJsonToCsv();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
