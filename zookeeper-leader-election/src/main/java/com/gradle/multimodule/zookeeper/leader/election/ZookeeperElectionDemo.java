package com.gradle.multimodule.zookeeper.leader.election;


import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Zookeeper thread:
 *  IO Thread
 *      Handles all network commountaiation with zookeeper servers
 *      Handle zookeeper request and response
 *      Responds pings
 *      session management
 *      session timeout
 *  Event Thread:
 *      connection
 *      disconnection
 *      custome note watchers and triggers
 *
 *
 *      Once we package jar and run multiple instances of this application it will automatically decides
 *      and chooses smallest instance as leader node
 *
 *
 *  Herd Effect: when a leader node get deleted all the child node get notified by zookeeper and
 *  all child node will call getChildren() method from zookeeper if number of nodes are more it may
 *  cause issue of overloading on zookeeper node
 */
public class ZookeeperElectionDemo implements Watcher {
    private static final String ZOOKEEPER_PATH = "/Users/sandeepmishra/devtools/kafka/kafka_2.13-2.6.0/bin";
    private static final int SESSION_TIMEOUT = 3000;
    private static final String ZOOKEEPER_ADDRESS = "localhsot:2181";
    private ZooKeeper zooKeeper;
    private static final String ELECTION_NAMESPACE = "/election";
    private String currentZnodeName;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZookeeperElectionDemo zookeeperElectionDemo=new ZookeeperElectionDemo();
        zookeeperElectionDemo.connectToZookeeper();
        zookeeperElectionDemo.volunteerForLeader();
        zookeeperElectionDemo.electLeader();
        zookeeperElectionDemo.run();
        zookeeperElectionDemo.close();
        System.out.println("Disconnected from zookeeper, exiting application");
    }

    public void volunteerForLeader() throws KeeperException, InterruptedException {
        String znodePrefix = ELECTION_NAMESPACE+"/c_";
        String zNodeFullPath = zooKeeper.create(znodePrefix, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("znode name "+zNodeFullPath);
        this.currentZnodeName=zNodeFullPath.replace(ELECTION_NAMESPACE+"/", "");
    }
    public void electLeader() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(ELECTION_NAMESPACE, false);
        Collections.sort(children);
        String smallChiled = children.get(0);
        if(smallChiled.equals(currentZnodeName)){
            System.out.println("I am the leader");
            return;
        }
        System.out.println("I am not the leader, "+ smallChiled+" is the leader.");
    }
    public void connectToZookeeper() throws IOException {
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, this);
    }

    public void close() throws InterruptedException {
        this.zooKeeper.close();
    }

    public void run() throws InterruptedException {
        synchronized (zooKeeper){
            zooKeeper.wait();
        }
    }
    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()){
            case None:
                if(event.getState() ==Event.KeeperState.SyncConnected){
                    System.out.println("Successfully connected to Zookeeper.");
                }else{
                    synchronized (zooKeeper){
                        System.out.println("Disconnected from zookeeper event");
                        zooKeeper.notifyAll();
                    }
                }
        }
    }
}
