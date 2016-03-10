package rest.service.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

@Path("/service")
public class RestService {

	/*
	 * simple HTTP get method example
	 */
	@GET
	@Path("/web")
	public Response testService() {
		return Response.status(200)
				.entity("Web method called successfully ! HTTP GET Request!")
				.build();
	}

	/*
	 * getDevicesList() accepts HTTP Get method
	 * 
	 * This method is responsible for sending the all devices list.
	 */
	@GET
	@Path("/devicesList")
	@Produces("text/html")
	public Response getDevicesList() {
		List deviceDataList = new ArrayList<String>();
		Mongo mongo;
		DBCollection collection;
		DB db;
		try {
			mongo = new Mongo("localhost", 27017);
			db = mongo.getDB("aricent");
			collection = db.getCollection("transactions");
			BasicDBObject t = new BasicDBObject();
			DBCursor cursor = collection.find(t).limit(10);
			while (cursor.hasNext()) {
				deviceDataList.add(cursor.next());
			}
		} catch (Exception e) {
			return Response.status(500)
					.entity("Exception Occured while getting device slist")
					.build();
		}

		if (deviceDataList == null)
			return Response.status(204).entity("No content Found!").build();
		else
			return Response.status(200).entity(deviceDataList.toString())
					.build();

	}
	
	@GET
	@Path("/aricent")
	@Produces("text/html") 
	public Response getDevicesList1() {
		List deviceDataList = new ArrayList<String>();
		Mongo mongo;
		DBCollection collection;
		int i=0;
		
		DB db;
		try {
			mongo = new Mongo("localhost", 27017);
			db = mongo.getDB("aricent");
			collection = db.getCollection("transactions");
			BasicDBObject t = new BasicDBObject();
			t.put("temperature", 23);
			int cursor = collection.find(t).count();
			//while (cursor.hasNext()) {
			//++;
			//	deviceDataList.add(cursor.next());
				
			//}
			System.out.println(cursor);
		} catch (Exception e) {
			return Response.status(500)
					.entity("Exception Occured while getting device slist")
					.build();
		}

		if (deviceDataList == null)
			return Response.status(204).entity("No content Found!").build();
		else
			return Response.status(200).entity(deviceDataList.toString())
					.build();

	}

	/*
	 * getDevicesList() accepts HTTP Get method
	 * 
	 * This method is responsible for sending the all devices list.
	 */
	@GET
	@Path("deviceNo/{deviceId}")
	@Produces("text/html") 
	public Response getDeviceData(@PathParam("deviceId") String deviceId) { 
		List deviceDataList = new ArrayList<String>();
		Mongo mongo;
		DBCollection collection;
		DB db;
		try {
			mongo = new Mongo("localhost", 27017);
			db = mongo.getDB("aricent");
			collection = db.getCollection("transactions");
			BasicDBObject t = new BasicDBObject();
			t.put("device_no", deviceId);
			DBCursor cursor = collection.find(t).limit(10);
			while (cursor.hasNext()) {
				deviceDataList.add(cursor.next());
			}
		} catch (Exception e) {
			return Response.status(500)
					.entity("Exception Occured while getting device slist")
					.build();
		}

		if (deviceDataList == null)
			return Response.status(204).entity("No content Found!").build();
		else
			return Response.status(200).entity(deviceDataList.toString())
					.build();

	}

	/*
	 * storeJson() method accepts HTTP Post request This method consume JSON
	 * formated String as the input This method stores the JSON data in mongoDB
	 */
	@POST
	@Path("/storeData")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeJson(String jsonData) throws JsonParseException,
			JsonMappingException, IOException {
		try {
			// connecting to MONGODB
			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("aricent");
			// connecting to 'axelta' collection
			DBCollection dbCollection = db.getCollection("transactions");
			DBObject dbObject = (DBObject) JSON.parse(jsonData.toString());
			// storing
			WriteResult writeResult = dbCollection.insert(dbObject);
			// if data inserted into MongoDb
			if (writeResult.getError() == null) {
				return Response.status(200).entity("data stored successfully")
						.build();
			} else {
				return Response
						.status(400)
						.entity("The request cannot be fulfilled due to bad syntax")
						.build();
			}
		} catch (Exception e) {
			return Response.status(500)
					.entity("Exception Occured while getting device list")
					.build();
		}

	}
	

}
