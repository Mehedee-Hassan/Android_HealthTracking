package com.example.androidbtcontrol.utilities;

import android.util.Log;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

/**
 * Created by mhr on 28-Sep-17.
 */

public class EncryptedDataMaker {
    private static final String TAG = "EncryptedDataMaker";
    String password = ConstantValues.ENCRYPTION_PASS;

    public String encrypt(String message){


        try {
            String encryptedMsg = AESCrypt.encrypt(password, message);
            message =encryptedMsg;
        }catch (GeneralSecurityException e){
            //could not
            Log.d(TAG, "encrypt: "+e.getMessage());

        }


        return message;
    }
    public String encrypt(StringBuilder message){

        try {
            String encryptedMsg = AESCrypt.encrypt(password, message.toString());

            message = new StringBuilder(encryptedMsg);

        }catch (GeneralSecurityException e){
            //could not
            Log.d(TAG, "encrypt: "+e.getMessage());
        }


        return message.toString();
    }
}
