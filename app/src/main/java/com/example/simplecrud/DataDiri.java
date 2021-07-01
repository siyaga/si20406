package com.example.simplecrud;

public class DataDiri {
    String id;
    String name;
    String nomor;

    public DataDiri() {

    }

    public DataDiri(String id, String name,String nomor ) {
        this.id = id;
        this.name = name;
        this.nomor = nomor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

}
