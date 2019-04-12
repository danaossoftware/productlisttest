package bluetoothprintertest.dn.com.productlisttest;

import java.io.Serializable;

public class Product implements Serializable {
    public String name = "";
    public String imageURL = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
