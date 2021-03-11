package com.example.blood;

public class postinfo {
    public String uid;
    public  String bag,bloodgroup,place,type,name,phone,km;
    public postinfo(){

    }

    public postinfo(String name,String phone,String uid,String bag,String bloodgroup,String place,String type,String km){
        this.name=name;
        this.phone=phone;
        this.uid=uid;
        this.bag=bag;
        this.bloodgroup=bloodgroup;
        this.place=place;
        this.type=type;

        this.km=km;
    }
    public void setName(String name) {
        this.name = name;
    }
      public String getName() {
        return name;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getPhone() {
        return phone;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getBag() {
        return bag;
    }
    public void setBag(String bag) {
        this.bag = bag;
    }
    public String getBloodgroup() {
        return bloodgroup;
    }
    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }
    public String getKm(){
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }












}
