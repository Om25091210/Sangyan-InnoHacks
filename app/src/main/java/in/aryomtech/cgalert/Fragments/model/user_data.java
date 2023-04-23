package in.aryomtech.cgalert.Fragments.model;

import androidx.annotation.Keep;

import java.io.Serializable;
@Keep
public class user_data implements Serializable {
    String name,phone;

    public user_data() {
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
