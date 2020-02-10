package com.example.mrz.cabsig;

public class UploadHelper {

    private double Longitude;
    private double Latitude;
    private String Signal;
    private double disOfOutter;
    private double disOfInner;
    private double ProbLongitude;
    private double ProbLatitude;
    private String ProbSig;
    private Integer StaTyp;

    public UploadHelper(double latitude, double longitude, String signal, double disou, double disin,
                        double probLatitude, double probLongitude, String probSig, Integer staTyp ){
        Longitude = longitude;
        Latitude = latitude;
        Signal = signal;
        disOfOutter = disou;
        disOfInner = disin;
        ProbLatitude = probLatitude;
        ProbLongitude= probLongitude;
        ProbSig= probSig;
        StaTyp = staTyp;

    }

    public double getLongitude(){
        return Longitude;
    }

    public void setLongitude(double longitude){
        Longitude = longitude;
    }

    public double getLatitude(){
        return Latitude;
    }

    public void setLatitude(double latitude){
        Latitude = latitude;
    }

    public String getSignal(){
        return Signal;
    }

    public void setSignal(String signal){
        Signal = signal;
    }


    public double getDisOfOutter(){ return disOfOutter; }

    public void setDisOfOutter(double disou){
        disOfOutter = disou;
    }

    public double getDisOfInner(){
        return disOfInner;
    }

    public void setDisOfInner(double disin){
        disOfInner = disin;
    }


    public double getProbLongitude(){
        return ProbLongitude;
    }

    public void setProbLongitude(double probLongitude){
        ProbLongitude = probLongitude;
    }

    public double getProbLatitude(){
        return ProbLatitude;
    }

    public void setProbLatitude(double probLatitude){
        ProbLatitude = probLatitude;
    }

    public String getProbSig(){
        return ProbSig;
    }

    public void setProbSig(String probSig){
        ProbSig = probSig;
    }

    public Integer getStaTyp(){
        return StaTyp;
    }

    public void setStaTyp(Integer staTyp){
        StaTyp = staTyp;
    }

}
