
package com.corp.tsdb.dbcore.overflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.corp.tsdb.common.exceptions.StorageException;
import com.corp.tsdb.common.exceptions.StreamException;
import com.corp.tsdb.common.model.Range;
import com.corp.tsdb.common.model.Sensor;
import com.corp.tsdb.dbcore.model.IDataPackage;
import com.corp.tsdb.dbcore.model.datapackage.PackageFactory;
import com.corp.tsdb.dbcore.overflow.OverflowMoveTest.TimeValue;
import com.corp.tsdb.dbcore.storage.cassandra.BackendReader;
import com.corp.tsdb.dbcore.storage.cassandra.BackendWriter;
import com.corp.tsdb.dbcore.storage.cassandra.CassandraCluster;
import com.corp.tsdb.dbcore.storage.cassandra.NativeWriter;
import com.corp.tsdb.dbcore.storage.cassandra.Overflow;
import com.corp.tsdb.dbcore.storm.SensorCache;

import jline.internal.Log;
import junit.framework.Assert;

public class RemoveTimeConflictTest {
	
	private String equip_test = "equip_test";
	private String sensor_fixed = "sensor_fixed";
	private String ks = "test";
	private String workTable = "cf1";
	private String backTable = "cf2";
	private Overflow of = null;
	List<Range> ranges = new ArrayList<Range>();
	private BackendWriter bwWriter = new BackendWriter();
	private BackendReader brReader = new BackendReader();
	NativeWriter nwWriter = new NativeWriter();
	
	/**
	 * @info endTime is bigger than max exist timestamp, 
	 * 		 not invoke mergeTable,
	 * 		 mergeOneItem is expected to execute background
	 * 		 endTime=8000, maxTime=7800, endTime>maxTime
	 */
	@Test
	public void removeTimeConflictTest() throws StreamException, StorageException, InterruptedException {
		
		Range r = new Range(Long.MIN_VALUE, Long.MAX_VALUE);
		ranges.add(r);
		of = Overflow.getInstance(ranges);
		
		CassandraCluster.getInstance().clearOfDatas(ks, workTable);
		CassandraCluster.getInstance().clearOfDatas(ks, backTable);
		CassandraCluster.getInstance().clearOfDatas("test", "s_sensor_fixed");
		
		Sensor tSensor = new SensorCache().getValue(equip_test, sensor_fixed).getSensor();
		IDataPackage pk = PackageFactory.getInstance().produce(tSensor);
		pk.addRange(100L, 7800L, "1.0f");
		nwWriter.write(pk);
		
//		TimeUnit.SECONDS.sleep(5);
		Long stime = new Long(5423);
		Long etime = new Long(8000);
//		of.removeRange(equip_test, sensor_fixed, stime, etime);
		of.removeRange(equip_test, sensor_fixed, stime, etime);
		
		List<TimeValue> calcAnswer = new ArrayList<TimeValue>();
		calcAnswer.add(new TimeValue(0, "1.0"));
		calcAnswer.add(new TimeValue(1000, "1.0"));
		calcAnswer.add(new TimeValue(2000, "1.0"));
		calcAnswer.add(new TimeValue(3000, "1.0"));
		calcAnswer.add(new TimeValue(4000, "1.0"));
		calcAnswer.add(new TimeValue(5000, "1.0"));
		
		IDataPackage[] packs = brReader.getPackages(equip_test, sensor_fixed, 0, 30000);
		for(IDataPackage tmp : packs) {
			Map<Long, Object> map = tmp.getDatas();
			int cnt = 0;
			Log.info("removeTimeConflictTest" + calcAnswer.size() + "==" + map.size());
			Assert.assertTrue(calcAnswer.size() == map.size());
		
			for(Entry<Long, Object> entry : map.entrySet()) {
				Log.info(entry.getKey() + "-" + entry.getValue());
				Assert.assertEquals((long)entry.getKey()-(long)entry.getKey()%1000, calcAnswer.get(cnt).t);
				Assert.assertEquals(entry.getValue().toString(), calcAnswer.get(cnt).v);
				cnt++;
			}	
		}
	}
	
	class TimeValue {
		public long t;
		public String v;
		public TimeValue(long time, String value) {
			t = time; v = value;
		}
	}
}
