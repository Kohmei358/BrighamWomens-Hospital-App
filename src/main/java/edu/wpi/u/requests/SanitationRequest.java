package edu.wpi.u.requests;

import edu.wpi.u.users.Role;

import java.io.Serializable;
import java.util.LinkedList;

public class SanitationRequest extends SpecificRequest {

    @Override
    public String getType() {
        return "Sanitation";
    }

    @Override
    public String[] getSpecificFields() {
        String[] res = new String[]{"hazardLevel", "spillType"};
        return res;
    }
    public String getRelevantRole(){
        return String.valueOf(Role.MAINTENANCE);
    }
}
