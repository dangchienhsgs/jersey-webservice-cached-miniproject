package com.adflex.internship.authen;

import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.ByteUtil;
import org.jose4j.lang.JoseException;
import org.json.JSONObject;

import java.security.Key;

/**
 * Created by dangchienhsgs on 19/08/2015.
 */
public class JWCExample {
    public static void main(String args[]) throws JoseException{
        Key key = new AesKey("adflexeway112233".getBytes());
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setPlaintext(new JSONObject().put("_id", "hycongtu").put("role","publisher").toString());
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        jwe.setKey(key);
        String serializedJwe = jwe.getCompactSerialization();
        System.out.println("Serialized Encrypted JWE: " + serializedJwe);

        
        jwe = new JsonWebEncryption();
        jwe.setKey(new AesKey("adflexeway112233".getBytes()));
        jwe.setCompactSerialization(serializedJwe);
        System.out.println("Payload: " + jwe.getPayload());
    }

}
