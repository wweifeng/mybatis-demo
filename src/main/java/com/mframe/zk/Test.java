package com.mframe.zk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.Watcher.Event;
import org.apache.zookeeper.ZooKeeper;

public class Test {
	
	private static CountDownLatch countDownLatch = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception {
		/*ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 10000, event -> {
			if(event.getState() == Event.KeeperState.SyncConnected) {
				System.out.println("ZK连接成功");
				countDownLatch.countDown();
			}
		});
		countDownLatch.await();
		String result = zooKeeper.create("/zktest/zktest01", "zktest01".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
		System.out.println(result);
		zooKeeper.close();*/
		
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 10000);
		zkClient.subscribeChildChanges("/zktest", (parentPath, currentChilds) -> {
			currentChilds.forEach(childPath -> System.out.println(" 监听到："+ childPath) );
		});
		
		Thread.sleep(100000);
	}

}
