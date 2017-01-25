package com.example.administrator.skiptheline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class CommuninateWithFile {
	private String filename;
	private BufferedReader br = null;
	private FileReader fr = null;
	private FileWriter fWriter = null;
	private BufferedWriter bufferedWriter = null;
	private Context contextThis;

	public CommuninateWithFile(String initalFilename, Context context) {
		filename = initalFilename;
		contextThis = context;
	}

	// 获得文件路径
	public String getFilename() {
		return filename;
	}

	// 确定文件路径
	public void setFilename(String newFilename) {
		filename = newFilename;
	}

	// 读文件（从头开始把文件全部读出）
	public String readDataFromFile(Context contextThis) throws Exception {
		String text = null;
		try {
			FileInputStream fis = contextThis.openFileInput(filename); // 获得输入流
			Log.i("CommunicateWithFile", "readDataFromFile method:filename: "+filename);


			// 用来获得内存缓冲区的数据，转换成字节数组
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = fis.read(buffer)) != -1) {
				stream.write(buffer, 0, length);// 获取内存缓冲区中的数据
			}
			stream.close(); // 关闭
			fis.close();
			text=stream.toString();
		} catch (FileNotFoundException e) {
			Log.i("CommunicateWithFile", "file doesn't exist");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("CommunicateWithFile", "readDataFromFile successfully run");
		Log.i("CommunicateWithFile", "read file result is:"+text);
		return text;
	}


	// 写入文件（append为true从头开始写入,append为false从末尾开始写）
	public void writeDataToFile(String text,Context contextThis, boolean append) {
		try {

			Log.i("CommunicateWithFile", "writeDataToFile method ready to run(rewrite),filename is:"+filename);
			// 通过openFileOutput方法得到一个输出流，方法参数为创建的文件名（不能有斜杠），操作模式
			FileOutputStream fos = contextThis.openFileOutput(filename,
					Context.MODE_APPEND);
			fos.write(text.getBytes());// 写入
			fos.close(); // 关闭输出流
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 从头开始覆盖输入

	public void writeDataToFile(String text,Context contextThis) {


		try {
			Log.i("CommunicateWithFile", "writeDataToFile method ready to run(rewrite),filename is:"+filename);
			// 通过openFileOutput方法得到一个输出流，方法参数为创建的文件名（不能有斜杠），操作模式
			FileOutputStream fos = contextThis.openFileOutput(filename,
					Context.MODE_WORLD_READABLE);
			Log.i("CommunicateWithFile", "creat fos successfully,filename is :"+filename);
			fos.write(text.getBytes());// 写入
			Log.i("CommunicateWithFile", "write data  successfully,has write:"+text);
			fos.close(); // 关闭输出流
			// 弹出Toast消息
			//Toast.makeText(CommuninateWithFile.this, "写入到系统默认位置保存成功", Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
