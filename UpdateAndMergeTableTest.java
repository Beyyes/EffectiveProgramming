
package com.corp.tsdb.dbcore.overflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

public class UpdateAndMergeTableTest {
	
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
	 * @info endTime is smaller than max exist timestamp, 
	 * 		 and invoke mergeTable
	 *       endTime=6000, maxTime=7800, endTime<maxTime
	 */
	@Test
	public void updateAndMergeTableTest2() throws StreamException, StorageException, InterruptedException {
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
		
		of.updateRange(equip_test, sensor_fixed, 5023L, 7000L, 3.3f);
		of.mergeTable();
		
		List<TimeValue> calcAnswer = new ArrayList<TimeValue>();
		calcAnswer.add(new TimeValue(0L, "1.0"));
		calcAnswer.add(new TimeValue(1000L, "1.0"));
		calcAnswer.add(new TimeValue(2000L, "1.0"));
		calcAnswer.add(new TimeValue(3000L, "1.0"));
		calcAnswer.add(new TimeValue(4000L, "1.0"));
		calcAnswer.add(new TimeValue(5000L, "3.3"));
		calcAnswer.add(new TimeValue(6000L, "3.3"));
		calcAnswer.add(new TimeValue(7000L, "3.3"));
		
		IDataPackage[] packs = brReader.getPackages(equip_test, sensor_fixed, 0, 30000);
		for(IDataPackage tmp : packs) {
			Map<Long, Object> map = tmp.getDatas();
			int cnt = 0;
			Log.info("updateAndMergeTableTest:" + calcAnswer.size() + "==" + map.size());
			Assert.assertTrue(calcAnswer.size() == map.size());
		
			for(Entry<Long, Object> entry : map.entrySet()) {
				Log.info(entry.getKey() + "-" + entry.getValue());
				cnt++;
			}	
			cnt = 0;
			for(Entry<Long, Object> entry : map.entrySet()) {
				System.out.println(Long.valueOf(entry.getKey().longValue()-entry.getKey().longValue()%1000) + "=="
						+ calcAnswer.get(cnt).t);
				Assert.assertTrue(Long.valueOf(entry.getKey().longValue()-entry.getKey().longValue()%1000).equals
								(calcAnswer.get(cnt).t)
								);
				Assert.assertEquals(entry.getValue().toString(), calcAnswer.get(cnt).v);
				cnt++;
			}	
		}
	}
	
	class TimeValue {
		public Long t;
		public String v;
		public TimeValue(Long time, String value) {
			t = time; v = value;
		}
	}
}
