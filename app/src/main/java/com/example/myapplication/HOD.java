package com.example.myapplication;

public class HOD {
    String Name;
    String Id;
    String Password;
    String Branch;
    public HOD(String Name, String Id, String Password, String Branch) {
        this.Name = Name;
        this.Id = Id;

        this.Password = Password;
        this.Branch=Branch;
    }

    public String getName() {
        return Name;
    }

    public String getId() {
        return Id;
    }
    public String getPassword() {
        return Password;
    }
    public String getBranch(){return Branch;}
}

