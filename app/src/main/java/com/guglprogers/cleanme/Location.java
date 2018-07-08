
package com.guglprogers.cleanme;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("Address")
    @Expose
    private com.guglprogers.Address address;

    public com.guglprogers.Address getAddress() {
        return address;
    }

    public void setAddress(com.guglprogers.Address address) {
        this.address = address;
    }

}
