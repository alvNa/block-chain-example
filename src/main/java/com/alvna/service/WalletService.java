package com.alvna.service;

import com.alvna.model.Transaction;
import com.alvna.model.TransactionInput;
import com.alvna.model.TransactionOutput;
import com.alvna.model.Wallet;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Map;

public class WalletService {

    //returns balance and stores the unspentTransOut's owned by this wallet
    public float getBalance(Wallet w, Map<String, TransactionOutput> unspentTransMap) {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: unspentTransMap.entrySet()){
            TransactionOutput unspentTrans = item.getValue();
            if(unspentTrans.isMine(w.publicKey)) { //if output belongs to me ( if coins belong to me )
                w.addTransaction(unspentTrans.id,unspentTrans); //add it to our list of unspent transactions.
                total += unspentTrans.value ;
            }
        }
        return total;
    }

    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(Wallet w, Map<String, TransactionOutput> unspentTxOutMap, PublicKey _recipient, float value ) {
        if(getBalance(w,unspentTxOutMap) < value) { //gather balance and check funds.
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        //create array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: w.getUnspentTxOutMap().entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total > value) break;
        }

        Transaction newTransaction = new Transaction(w.publicKey, _recipient , value, inputs);
        newTransaction.generateSignature(w.privateKey);

        for(TransactionInput input: inputs){
            w.getUnspentTxOutMap().remove(input.getTransactionOutputId());
        }
        return newTransaction;
    }
}
