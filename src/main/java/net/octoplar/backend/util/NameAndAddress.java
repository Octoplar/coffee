package net.octoplar.backend.util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by Octoplar.
 */

public class NameAndAddress implements Cloneable, Serializable{


    @Size(max = 100, message = "max name length is 100")
    private String name;


    @NotNull(message = "address required")
    @Size(min = 1, max = 200, message = "address length mus be in 1..200 range")
    private String address;

    public NameAndAddress() {
    }

    public NameAndAddress(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

