package com.alvna.service;

import com.alvna.NoobChain2;
import com.alvna.model.Transaction;
import com.alvna.model.TransactionInput;
import com.alvna.model.TransactionOutput;

import java.util.Map;

public class TransactionService {

    //Returns true if new transaction could be created.
    public boolean processTransaction(Transaction t, Map<String, TransactionOutput> UTXOs) {

        if(t.verifySignature() == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        //gather transaction inputs (Make sure they are unspent):
        for(TransactionInput i : t.getInputs()) {
            i.setUnspentTransOut(UTXOs.get(i.getTransactionOutputId()));
        }

        //check if transaction is valid:
        if(t.getInputsValue() < NoobChain2.minimumTransaction) {
            System.out.println("#Transaction Inputs to small: " + t.getInputsValue());
            return false;
        }

        //generate transaction outputs:
        double leftOver = t.getInputsValue() - t.getValue(); //get value of inputs then the left over change:
        t.setTransactionId(t.calulateHash());
        t.getOutputs().add(new TransactionOutput( t.getRecipient(), t.getValue(), t.getTransactionId())); //send value to recipient
        t.getOutputs().add(new TransactionOutput( t.getSender(), leftOver, t.getTransactionId())); //send the left over 'change' back to sender

        //add outputs to Unspent list
        for(TransactionOutput o : t.getOutputs()) {
            UTXOs.put(o.id , o);
        }

        //remove transaction inputs from unspentTransOut lists as spent:
        for(TransactionInput i : t.getInputs()) {
            if(i.getUnspentTransOut() == null) continue; //if Transaction can't be found skip it
            UTXOs.remove(i.getUnspentTransOut().id);
        }

        return true;
    }
}
