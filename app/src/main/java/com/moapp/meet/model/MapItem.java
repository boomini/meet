package com.moapp.meet.model;

public class MapItem {
    public String AddressName;
    private String posx;
    private String posy;
    public String Id;
    public String Category;
    public MapItem() {
    }

    public MapItem(String id,String addressName, String posx, String posy,String category) {
        Id=id;
        AddressName = addressName;
        this.Category=category;
        this.posx = posx;
        this.posy = posy;
    }

    public String getAddressName() {
        return AddressName;
    }

    public void setAddressName(String addressName) {
        AddressName = addressName;
    }

    public String getPosx() {
        return posx;
    }

    public void setPosx(String posx) {
        this.posx = posx;
    }

    public String getPosy() {
        return posy;
    }

    public void setPosy(String posy) {
        this.posy = posy;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
