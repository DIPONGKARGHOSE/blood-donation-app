package com.example.blood;

public class sign {
   public String name;
    public String bloodgroup;
    public String phone;
    public String date;
    public sign(){

    }
    public sign(String name,String bloodgroup ,String phone,String date){
        this.name=name;
        this.bloodgroup=bloodgroup;
        this.phone=phone;
        this.date=date;


    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }




}
