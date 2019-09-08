package com.wedevol.xmpp.service.impl;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.wedevol.xmpp.bean.CcsInMessage;
import com.wedevol.xmpp.service.PayloadProcessor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alina on 6/21/2017.
 */
public class MidPointProcessor implements PayloadProcessor {

    private final static Gson gson = new Gson();
    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    DatabaseReference ref = FirebaseDatabase
            .getInstance()
            .getReference();

    @Override
    public void handleMessage(CcsInMessage inMessage) {
        OurLocation midpoint = getMidPoint(inMessage);
        String GroupID= inMessage.getDataPayload().get("GroupID");



        ref.child("Groups").child(GroupID).child("MiddlePoint").child("point").child("lat").setValue(midpoint.lat);
        ref.child("Groups").child(GroupID).child("MiddlePoint").child("point").child("lon").setValue(midpoint.lon);

    }

    private OurLocation getMidPoint(CcsInMessage inMessage)
    {
        String locationsJson = inMessage.getDataPayload().get("locations");
        OurLocation[] locations = gson.fromJson(locationsJson, OurLocation[].class);
        OurLocation midPoint= new OurLocation();
        if(locations.length==1)
        {
            midPoint.setLon(locations[0].lon);
            midPoint.setLat(locations[0].lat);
            return midPoint;
        }
        Double[][] distances = getDistances(locations);
        Double[] distanceAverages = getDistanceAverages(locations, distances);
        Double avgDistance = avg(distanceAverages);
        Double stdDistance = stdev(distanceAverages, avgDistance)*1.2;
        List<Integer> computeSet = getComputeSet(locations, distanceAverages, avgDistance, stdDistance);

        midPoint = getMidPoint(locations, computeSet);
        return midPoint;
    }

    private OurLocation getMidPoint(OurLocation[] locations, List<Integer> computeSet) {
        int n = computeSet.size();
        Double[] lats = new Double[n];
        Double[] lons = new Double[n];

        for(int i = 0 ; i < n ; i ++)
        {
            Integer index = computeSet.get(i);
            OurLocation location = locations[index];
            lats[i] = location.lat;
            lons[i] = location.lon;
        }

        Double latAverage = avg2(lats);
        Double lonAverage = avg2(lons);
        OurLocation midpoint = new OurLocation();
        midpoint.lat = latAverage;
        midpoint.lon = lonAverage;

        return midpoint;
    }

    private Double avg2(Double[] vals) {


        Double avgtoret=0.0;
        int n = vals.length;
        int i;
        double tmp=0;
        for (i= 0; i<n; i++)
        {
            if(vals[i]!=null)
                tmp+=vals[i];

        }


        avgtoret=tmp/(n);
        return avgtoret;
    }

    private List<Integer> getComputeSet(OurLocation[] locations, Double[] distanceAverages, Double avgDistance, Double stdDistance) {
        List<Integer> computeSet = new ArrayList<Integer>();
        int n = locations.length;
        for(int i= 0 ; i< n; i ++)
        {
            if((Math.abs(avgDistance - distanceAverages[i]))<= stdDistance)
            {
                computeSet.add(i);

            }
        }return computeSet;
    }

    private Double[] getDistanceAverages(OurLocation[] locationObject, Double[][] distances) {
        int n = locationObject.length;
        Double[] distAvg = new Double[n];
        for (int i = 0; i<n ; i++)
        {
            distAvg[i]= avg(distances[i]);
        }

        return distAvg;
    }

    private Double[][] getDistances(OurLocation[] locationObject) {
        int n = locationObject.length;
        Double[][] distances = new Double[n][n];

        for(int i = 0; i < n; i++)
        {
            for(int j = 0; j < n ; j++)
            {
                if(i==j || distances[i][j]!= null || distances[j][i]!= null)
                    continue;

                double distance  = computeDistance(locationObject[i], locationObject[j]);
                distances[i][j] = distance;
                distances[j][i] = distance;
            }
        }

        return distances;
    }

    //TODO use google apis to compute
    private double computeDistance(OurLocation ourLocation1, OurLocation ourLocation2)
    {
        double dis;
        dis= distance(ourLocation1.lat, ourLocation1.lon, ourLocation2.lat,ourLocation2.lon);

        return dis;
    }




    public static double distance(double startLat, double startLong,
                                  double endLat, double endLong) {

        double dLat  = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat   = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }

    //TODO note: consider each array has a single null item, do not consider it and compute for n-1
    private Double avg(Double[] source) {
        Double avgtoret=0.0;
        int n = source.length;
        int i;
        double tmp=0;
        for (i= 0; i<n; i++)
        {
            if(source[i]!=null)
                tmp+=source[i];

        }

        int div=n-1;
        if(n>2)
            div=n;
        avgtoret=tmp/(div);
        return avgtoret;
    }

    //TODO note: consider each array has a single null item, do not consider it and compute for n-1
    private Double stdev(Double[] source, Double avg)
    {
        int n = source.length;
        Double stdv=0.0;
        Double tmp=0.0;

        for(int i = 0; i < n; i++)
        {
            tmp=tmp+ Math.pow(source[i]-avg,2);
        }
        stdv=Math.sqrt(tmp/n);

        return stdv;
    }
}
