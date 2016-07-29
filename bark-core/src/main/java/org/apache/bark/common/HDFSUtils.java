/*
	Copyright (c) 2016 eBay Software Foundation.
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.apache.bark.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HDFSUtils {

	private static Logger logger = LoggerFactory.getLogger(HDFSUtils.class);

	public static boolean checkHDFSFolder(String folderPath) {


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
