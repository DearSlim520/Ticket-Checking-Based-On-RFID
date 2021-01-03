package com.uhf.demo;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.lang.*;
import com.uhf.detailwith.InventoryDetailWith;
import com.uhf.linkage.Linkage;
import com.uhf.structures.InventoryArea;
import com.uhf.structures.RwData;
import com.uhf.utils.StringUtils;



public class UhfDemoExit {
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		int i = Linkage.getInstance().initial("COM4");// 初始化连接设备,参数：端口号
		int count = 100;
		// function：init， parameter：The port number
		while(true) {
			if (i == 0) {

				//System.out.println("connect success");
				getInventoryArea();// 盘点区域获取 getInventoryArea
				setInventoryArea();// 盘点区域设置 setInventoryArea
				startInventory();// 开始盘点测试 startInventory
				stopInventory();// 停止盘点测试 stopInventory

				while(true){
				int n=userReadSync(0, 1, count);        //READ THE TICKET
				count=n;
				}
			} else {
				System.out.println("connect failed");
			}
		}
	}
	public static void userWriteSync(int position, int length, String data) {
		while(true){
			byte[] password = StringUtils.stringToByte("00000000");
			byte[] writeData = StringUtils.stringToByte(data);
			RwData rwData = new RwData();
			int status =
					Linkage.getInstance().writeTagSync(password, 3, position, length, writeData, 500, rwData);//调用linkage中的user写入函数 注意参数  Invoking the user writing function in linkage and note the arguments
			//添加循环验证，避免读取失败 Add loop validation to avoid write failure
			if (status == 0) {
				if (rwData.status == 0) {
					String epc = "";
					if (rwData.epcLen > 0) {
						epc = StringUtils
								.byteToHexString(rwData.epc, rwData.epcLen);
					}
					//System.out.println("epc====" + epc);
					//System.out.println("user write success");
					return;
				}
			}
			//System.out.println("user write failed");
		}
	}

	//READ TICKET FUNCTION
	public static int userReadSync(int position, int length, int count) {
		while(true){
			RwData rwData = new RwData();
			byte[] password = StringUtils.stringToByte("00000000");
			int status =
					Linkage.getInstance().readTagSync(password, 3, position, length, 3000, rwData);//调用linkage中的user读取函数 注意参数  Invoking the user reading function in linkage and note the arguments
			//添加循环验证，避免读取失败 Add loop validation to avoid read failure
			if (status == 0) {
				String result = "";
				String epc = "";
				if (rwData.status == 0) {
					if (rwData.rwDataLen > 0) {
						result = StringUtils.byteToHexString(rwData.rwData,
								rwData.rwDataLen);
					}
					if (rwData.epcLen > 0) {
							epc = StringUtils
								.byteToHexString(rwData.epc, rwData.epcLen);
					}
					int re = Integer.parseInt(result);
					if (re==1111){
						userWriteSync(0,1,"2222");
						count -= 1;
						System.out.println("Exit success.");
						System.out.println("count="+count);
					}
					else{
						System.out.println("Ticket is not available");
					}

					return count;
				}
			}
			//System.out.println("user read failed");
		}

	}



	public static void startInventory() {// 开始盘点 startInventory
		InventoryArea inventory = new InventoryArea();
		inventory.setValue(2, 0, 6);
		Linkage.getInstance().setInventoryArea(inventory);
		InventoryDetailWith.tagCount = 0;// 获取个数  Get the number
		Linkage.getInstance().startInventory(2, 0);
		InventoryDetailWith.startTime = System.currentTimeMillis();// 盘点的开始时间 Start time of Inventory

		while (InventoryDetailWith.totalCount < 100) {

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		stopInventory();// 进行停止盘点 stopInventory

		/*for (Map<String, Object> _map : InventoryDetailWith.list) {
			System.out.println(_map);
			System.out.println("天线号(antennaPort)：" + _map.get("antennaPort"));
			System.out.println("epc码：" + _map.get("epc"));
			System.out.println("TID/USER码：" + _map.get("externalData"));
			System.out.println("次数(count)：" + _map.get("count"));
			System.out.println("Rssi：" + _map.get("rssi"));
		}*/

		long m_lEndTime = System.currentTimeMillis();// 当前时间 The current time
		double Rate = Math.ceil((InventoryDetailWith.tagCount * 1.0) * 1000
				/ (m_lEndTime - InventoryDetailWith.startTime));

		long total_time = m_lEndTime - InventoryDetailWith.startTime;
		String dateStr = StringUtils.getTimeFromMillisecond(total_time);
		int tag = InventoryDetailWith.list.size();
		//System.out.println("盘点速率(Inventory rate)：" + Rate);

		/*if (tag != 0) {
			System.out.println("盘点时间(Inventory time)：" + dateStr);
		} else {
			System.out.println("盘点时间(Inventory time)：" + "0时0分0秒0毫秒");
		}
		System.out.println("标签个数(The number of tag)：" + tag);*/

	}

	public static void stopInventory() {// 停止盘点 stopInventory
		Linkage.getInstance().stopInventory();
	}

	// 盘点区域获取 getInventoryArea
	public static void getInventoryArea() {
		InventoryArea inventoryArea = new InventoryArea();
		int status = Linkage.getInstance().getInventoryArea(inventoryArea);
		if (status == 0) {
			//System.out.println("area:" + inventoryArea.area);
			//System.out.println("startAddr:" + inventoryArea.startAddr);
			//System.out.println("wordLen:" + inventoryArea.wordLen);
			//System.out.println("getInventoryArea success");
			return;
		}
		//System.out.println("getInventoryArea failed");
	}

	// 盘点区域设置 setInventoryArea
	public static void setInventoryArea() {
		InventoryArea inventoryArea = new InventoryArea();
		inventoryArea.setValue(2, 0, 6);// 2为epc+user
		int status = Linkage.getInstance().setInventoryArea(inventoryArea);
		if (status == 0) {
			//System.out.println("setInventoryArea success");
			return;
		}
		//System.out.println("setInventoryArea failed");
	}

}
