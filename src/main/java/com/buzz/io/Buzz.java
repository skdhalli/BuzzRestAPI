/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.buzz.io;


import com.buzz.buzzdata.DistanceUnits;
import com.buzz.buzzdata.IBuzzDB;
import com.buzz.buzzdata.MongoBuzz;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.bson.types.ObjectId;

/**
 * REST Web Service
 *
 * @author sdhalli
 */
@Path("buzz")
public class Buzz {

    @Context
    private UriInfo context;
    
    IBuzzDB buzzDB;
 
    /**
     * Creates a new instance of Buzz
     * @throws java.net.UnknownHostException
     */
    public Buzz() throws UnknownHostException {
       buzzDB = new MongoBuzz("162.219.245.33", 27017, "Buzz", "admin", "qNjneHcKyl");
    }

    /**
     * Retrieves representation of an instance of BuzzIO.BuzzService
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        return "Welcome to Buzz Services";
    }
    
      @GET
    @Path("Add")
    @Produces("application/json")
    public String AddBuzz(@QueryParam("userid") String userid, 
            @QueryParam("header") String header, 
            @QueryParam("content") String content,
            @QueryParam("lat") Double lat, 
            @QueryParam("lng") Double lng, 
            @QueryParam("tags") String tags,
            @QueryParam("files_cs") String files_cs) 
            throws MongoException, UnknownHostException
    {
        String retval = "Success";
        buzzDB.Insert(userid, header, content, lat, lng, tags, files_cs.split(","));
        return retval;
    }
    
    @GET
    @Path("GeoSearch")
    @Produces("application/json")
    public String SearchBuzz(@QueryParam("lat") Double lat, 
            @QueryParam("lng") Double lng,
            @QueryParam("distance") double distance, 
            @QueryParam("units") String units,
            @QueryParam("tags") String tags) 
            //throws MongoException, UnknownHostException
    {
        String retval ="";
        DistanceUnits units_api = DistanceUnits.miles;
        switch(units.toUpperCase())
        {
            case "MILES":
                units_api = DistanceUnits.miles;
                break;
            case "KILOMETERS":
                units_api = DistanceUnits.kilometers;
                break;
        }
        retval = buzzDB.SearchByLocation(lat, lng, distance, units_api, tags);
        return retval;
    }
    
    @GET
    @Path("Image")
    @Produces("image/jpg")
    public InputStream GetBuzzImage(@QueryParam("buzzid") String buzzid, 
            @QueryParam("pic_num") String pic_num) throws FileNotFoundException
    {
        InputStream retval = buzzDB.GetImgByBuzz(buzzid, Integer.parseInt(pic_num));
        return retval;
    }
}
