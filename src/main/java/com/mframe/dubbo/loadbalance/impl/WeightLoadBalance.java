package com.mframe.dubbo.loadbalance.impl;

import java.util.ArrayList;
import java.util.List;

import com.mframe.dubbo.loadbalance.LoadBalance;

/**
 * 权重
 * @author wwf
 *
 */
public class WeightLoadBalance implements LoadBalance {

	private LoadBalance loadBalance;
	
	public WeightLoadBalance() {
		this.loadBalance = new LoopLoadBalance();
	}
	
	public String chooseServer(List<String> serverList) {
		return loadBalance.chooseServer(serverList);
	}
	
	public String chooseWeightServer(List<WeightEntity> weightEntityList){
		List<String> serverList = new ArrayList<String>();
		for(WeightEntity weightEntity : weightEntityList) {
			int weight = weightEntity.getWeight();
			for(int i = 0;i<weight;i++) {
				serverList.add(weightEntity.getAddress());
			}
		}
		return chooseServer(serverList);
	}
	
    public class WeightEntity {
    	
    	private String address;
    	
    	private int weight;

		public WeightEntity(String address, int weight) {
			this.address = address;
			this.weight = weight;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}
    	
    } 

}
