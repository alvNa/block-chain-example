package com.alvna.model;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;
    //only unspent transaction outputs owned by this wallet.
    private Map<String,TransactionOutput> unspentTxOutMap = new HashMap<String,TransactionOutput>();

    public Wallet(){
        generateKeyPair();
    }

    public Map<String, TransactionOutput> getUnspentTxOutMap() {
        return unspentTxOutMap;
    }

    public void addTransaction(String key, TransactionOutput value){
        unspentTxOutMap.put(key,value);
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}