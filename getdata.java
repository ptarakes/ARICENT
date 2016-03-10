package com.nvd3;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
@SuppressWarnings("serial")
public class GETData extends HttpServlet {
	@SuppressWarnings("rawtypes")
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		res.setContentType("application/json"); /*set response type as json*/
		res.setCharacterEncoding("UTF-8");
		List dataList = getData(); /*calling getData2()*/
		JSONArray data = new JSONArray(dataList);/*type casting List to JSONArray*/
		res.getWriter().print(data);/*returning the data*/
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getData() throws UnknownHostException{
		
		List data = new MapReduce().mapReduce();/*call MapReduce method to get the data*/
		
		List jsonList = new ArrayList();
		Set customerSet = new HashSet();
		List valueList = new ArrayList();
		Set distX = new HashSet();
		/*
		 * get distinct customers, values, weekNo
		 */
		for(int i=0; i<data.size(); i++){
			HashMap withTripId = (HashMap) data.get(i);
			HashMap values = (HashMap) withTripId.get("value");
			if(values.size()==2){
				distX.add(values.get("weekNo"));
				customerSet.add(values.get("customer"));
				valueList.add(values);
			}
		}
		Iterator itr = customerSet.iterator();
		while(itr.hasNext()){
			String customer = (String) itr.next();
			HashMap series = new HashMap();/*format: {key:"", values:[]}*/
			List seriesValueList = new ArrayList();/*format: values:[{},{},{}.....]*/
			int counter = 1;
			for(int i=0; i<valueList.size(); i++){
				HashMap seriesValues = new HashMap();
				String custName = (String) ((HashMap)valueList.get(i)).get("customer");
				double weekNo = (double) ((HashMap)valueList.get(i)).get("weekNo");
				if(customer.equals(custName)){
					boolean flag = false;
					for(int j=0; j<seriesValueList.size(); j++){
						double x = (double) ((HashMap)seriesValueList.get(j)).get("x");
						int y = (int) ((HashMap)seriesValueList.get(j)).get("y");
						if(weekNo == x){
							((HashMap)seriesValueList.get(j)).put("y", y+1);
							flag = true;
						}
					}
					if(!flag){
						seriesValues.put("x", weekNo);
						seriesValues.put("y", counter);
						seriesValueList.add(seriesValues);
					}
				}
			}
			series.put("values", seriesValueList);
			series.put("key", customer);
			jsonList.add(series);
		}
		List distXList = new ArrayList(distX);
		Collections.sort(distXList);/*sorting distinct x values in ascending order*/
		/*
		 * inserting all x values in all records for getting correct chart
		 */
		Iterator xitr = distXList.iterator();
		while(xitr.hasNext()){
			double x1 = (double) xitr.next();
			for(int i=0;i<jsonList.size();i++){
				HashMap d1 = (HashMap) jsonList.get(i);
				List v2 = (List) d1.get("values");
				List subtime = new ArrayList();
				for(int j=0;j<v2.size();j++){
					HashMap v3 = (HashMap) v2.get(j);
					double x2 = (double) v3.get("x");
					subtime.add(x2);
				}
				if(!subtime.contains(x1)){
					HashMap v4 = new HashMap();
					v4.put("x", x1);
					v4.put("y", 0);
					v2.add(v4);
				}
				Collections.sort(v2, new MyComparator());
			}
		}
		return jsonList;
	}
	/*
	 * this class for compare list of hash maps and sorting in ascending order
	 */
	@SuppressWarnings("rawtypes")
	public class MyComparator implements Comparator<HashMap> {
		@Override
		public int compare(HashMap o1, HashMap o2) {
			double d = (double)o1.get("x") -(double)o2.get("x");
			return (int)d;
		}
	}
}