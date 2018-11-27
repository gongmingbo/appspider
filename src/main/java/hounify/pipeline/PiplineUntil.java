package hounify.pipeline;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hounify.entity.BaseContent;

/**
 * 
 * @author gongmingbo
 *获取时间
 */
public class PiplineUntil {
 public static Timestamp getTime(){
	 try {
		   String timeStr="1970-01-01";
		   SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
		   Date date=  format.parse(timeStr);
		   return new Timestamp(date.getTime());
	} catch (Exception e) {
		 return new Timestamp(new Date().getTime());
	}
	
 }
	//查看是否存在
	public static boolean distinct(List<BaseContent> lists,String title){
		for (BaseContent b : lists) {
			if (title.equals(b.getContentTitle())) {
				return true;
			}
		}
		
		return false;
	}

}
