package com.uhf.demo;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.uhf.detailwith.InventoryDetailWith;
import com.uhf.linkage.Linkage;
import com.uhf.structures.InventoryArea;
import com.uhf.structures.RwData;
import com.uhf.utils.StringUtils;

public class UhfDemo1 {
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		int i = Linkage.getInstance().initial("COM4");// 初始化连接设备,参数：端口号
		// function：init， parameter：The port number
		if (i == 0) {
			System.out.println("connect success");
			getInventoryArea();// 盘点区域获取 getInventoryArea
			setInventoryArea();// 盘点区域设置 setInventoryArea
			startInventory();// 开始盘点测试 startInventory
			stopInventory();// 停止盘点测试 stopInventory

			System.out.println("input 1 for EPC write, 2 for User write，3 for read all");
			Scanner input=new Scanner(System.in);
			String n=input.next();
			while(true){
				if(n.equals("1")){
					System.out.println("input write data");
					String data = input.next();
					epcWriteSync(data);// epc同步写入 epcWriteSync
					epcReadSync();// epc同步读取 epcReadSync
					break;
				}
				else if(n.equals("2")){
					System.out.println("input write data");
					String data = input.next();
					userWriteSync(data);// user同步写入 userWriteSync
					userReadSync();// user同步读取 userReadSync
					break;
				}
				else if(n.equals("3")) {
					epcReadSync();// epc同步读取 epcReadSync
					userReadSync();// user同步读取 userReadSync
					tidReadSync();// tid同步读取 tidReadSync
					break;
				}
				else{
					System.out.println("wrong format");
					n=input.next();
				}
			}

			Linkage.getInstance().deinit();// 断开连接 deinit
		} else {
			System.out.println("connect failed");
		}

	}

	// epc区的同步读取
	public static void epcReadSync() {
		while(true){
			byte[] password = StringUtils.stringToByte("00000000");
			RwData rwData = new RwData();
			int status =
					Linkage.getInstance().readTagSync(password, 1, 2, 1, 3000, rwData);//调用linkage中的epc读取函数 注意参数
			// Invoking the epc reading function in linkage and note the arguments
			//添加循环验证，避免读取失败 Add loop validation to avoid read failure

			if (status == 0) {
				if (rwData.status == 0) {
					String result = "";
					String epc = "";
					if (rwData.rwDataLen > 0) {
						result = StringUtils.byteToHexString(rwData.rwData,
								rwData.rwDataLen);
					}
					if (rwData.epcLen > 0) {
						epc = StringUtils
								.byteToHexString(rwData.epc, rwData.epcLen);
					}
					System.out.println("result====" + result);// 3200
					System.out.println("epc====" + epc);// 320030007F263000DDD90140
					System.out.println("read success");
					return;
				}
			}
			System.out.println("read failed");
		}

	}

	public static void epcWriteSync(String data) {
		while(true){
			byte[] password = StringUtils.stringToByte("00000000");
			byte[] writeData = StringUtils.stringToByte(data);
			RwData rwData = new RwData();
			int status =
					Linkage.getInstance().writeTagSync(password, 1, 2, 1, writeData, 500, rwData);//调用linkage中的epc写入函数 注意参数
			// Invoking the epc writing function in linkage and note the arguments
			//添加循环验证，避免读取失败 Add loop validation to avoid write failure
			if (status == 0) {
				if (rwData.status == 0) {
					String epc = "";
					if (rwData.epcLen > 0) {
						epc = StringUtils
								.byteToHexString(rwData.epc, rwData.epcLen);
					}
					System.out.println("epc====" + epc);
					System.out.println("epc write success");
					return;
				}
			}
			System.out.println("epc write failed");
		}
	}

	public static void userReadSync() {
		while(true){
			RwData rwData = new RwData();
			byte[] password = StringUtils.stringToByte("00000000");
			int status =
					Linkage.getInstance().readTagSync(password, 3, 0, 2, 3000, rwData);//调用linkage中的user读取函数 注意参数  Invoking the user reading function in linkage and note the arguments
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
					System.out.println("userData====" + result);
					System.out.println("epc====" + epc);
					System.out.println("user read success");
					return;
				}
			}
			System.out.println("user read failed");
		}

	}

	public static void tidReadSync() {
		while(true){
			RwData rwData = new RwData();
			byte[] password = StringUtils.stringToByte("00000000");
			int status =
					Linkage.getInstance().readTagSync(password, 2, 0, 1, 3000, rwData);//调用linkage中的tid读取函数 注意参数  Invoking the tid reading function in linkage and note the arguments
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
					System.out.println("tidData====" + result);
					System.out.println("epc====" + epc);
					System.out.println("tid read success");
					return;
				}
			}
			System.out.println("tid read failed");
		}

	}

	public static void userWriteSync(String data) {
		while(true){
			byte[] password = StringUtils.stringToByte("00000000");
			byte[] writeData = StringUtils.stringToByte(data);
			RwData rwData = new RwData();
			int status =
					Linkage.getInstance().writeTagSync(password, 3, 0, 2, writeData, 500, rwData);//调用linkage中的user写入函数 注意参数  Invoking the user writing function in linkage and note the arguments
			//添加循环验证，避免读取失败 Add loop validation to avoid write failure
			if (status == 0) {
				if (rwData.status == 0) {
					String epc = "";
					if (rwData.epcLen > 0) {
						epc = StringUtils
								.byteToHexString(rwData.epc, rwData.epcLen);
					}
					System.out.println("epc" + epc);
					System.out.println("user write success");
					return;
				}
			}
			System.out.println("user write failed");
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

		for (Map<String, Object> _map : InventoryDetailWith.list) {
			System.out.println(_map);
			System.out.println("天线号(antennaPort)：" + _map.get("antennaPort"));
			System.out.println("epc码：" + _map.get("epc"));
			System.out.println("TID/USER码：" + _map.get("externalData"));
			System.out.println("次数(count)：" + _map.get("count"));
			System.out.println("Rssi：" + _map.get("rssi"));
		}

		long m_lEndTime = System.currentTimeMillis();// 当前时间 The current time
		double Rate = Math.ceil((InventoryDetailWith.tagCount * 1.0) * 1000
				/ (m_lEndTime - InventoryDetailWith.startTime));

		long total_time = m_lEndTime - InventoryDetailWith.startTime;
		String dateStr = StringUtils.getTimeFromMillisecond(total_time);
		int tag = InventoryDetailWith.list.size();
		System.out.println("盘点速率(Inventory rate)：" + Rate);

		if (tag != 0) {
			System.out.println("盘点时间(Inventory time)：" + dateStr);
		} else {
			System.out.println("盘点时间(Inventory time)：" + "0时0分0秒0毫秒");
		}
		System.out.println("标签个数(The number of tag)：" + tag);

	}

	public static void stopInventory() {// 停止盘点 stopInventory
		Linkage.getInstance().stopInventory();
	}

	// 盘点区域获取 getInventoryArea
	public static void getInventoryArea() {
		InventoryArea inventoryArea = new InventoryArea();
		int status = Linkage.getInstance().getInventoryArea(inventoryArea);
		if (status == 0) {
			System.out.println("area:" + inventoryArea.area);
			System.out.println("startAddr:" + inventoryArea.startAddr);
			System.out.println("wordLen:" + inventoryArea.wordLen);
			System.out.println("getInventoryArea success");
			return;
		}
		System.out.println("getInventoryArea failed");
	}
 
	// 盘点区域设置 setInventoryArea
	public static void setInventoryArea() {
		InventoryArea inventoryArea = new InventoryArea();
		inventoryArea.setValue(2, 0, 6);// 2为epc+user
		int status = Linkage.getInstance().setInventoryArea(inventoryArea);
		if (status == 0) {
			System.out.println("setInventoryArea success");
			return;
		}
		System.out.println("setInventoryArea failed");
	}

}
