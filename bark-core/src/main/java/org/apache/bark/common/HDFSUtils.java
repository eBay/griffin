package org.apache.bark.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HDFSUtils {

	private static Logger logger = LoggerFactory.getLogger(HDFSUtils.class);

	public static boolean checkHDFSFolder(String folderPath) {

/*		Configuration conf = new Configuration(false);
		
		conf.set("fs.defaultFS", "hdfs://apollo-phx-nn-ha");
		conf.set("fs.default.name", conf.get("fs.defaultFS"));
		conf.set("dfs.nameservices","apollo-phx-nn-ha");
		conf.set("dfs.ha.namenodes.apollo-phx-nn-ha", "nn1,nn2");
		conf.set("dfs.namenode.rpc-address.apollo-phx-nn-ha.nn1","apollo-phx-nn.vip.ebay.com:8020");
		conf.set("dfs.namenode.rpc-address.apollo-phx-nn-ha.nn2","apollo-phx-nn-2.vip.ebay.com:8020");
		
        conf.set("dfs.client.failover.proxy.provider.apollo-phx-nn-ha","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        
	    
		FileSystem dfs = null;

		try {


			Path path = new Path(folderPath);
			dfs = FileSystem.get(conf);
			
			dfs.create(new Path(path + File.separator + "test.dat"), true);
		
			return dfs.exists(path);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}*/
		
		
		Process processMoveFolder;
		int result;
		try {
			processMoveFolder = Runtime.getRuntime().exec("hadoop fs -ls " + folderPath);
			
			result = processMoveFolder.waitFor();
			
			if(result == 0) 
			{
				return true;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
}

