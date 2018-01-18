package com.example.ishitaroychowdhury.socialapp;

import android.widget.TextView;

/**
 * Created by ishitaroychowdhury on 11/21/17.
 */

public class CheckFieldValidator {

    public static boolean checkField(TextView field){
        String fieldData=field.getText().toString();
        if(fieldData.equalsIgnoreCase("") && fieldData==null && fieldData.equalsIgnoreCase(" ")){
            field.setError("Please fill this field");
            return false;
        }
        return true;
    }

    public static boolean passwordValidation(String cPass,String rPass){
        if(cPass.equals(rPass)){
            return true;
        }
        else{
            return false;
        }
    }
}
