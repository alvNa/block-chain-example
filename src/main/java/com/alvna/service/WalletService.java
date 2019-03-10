package com.alvna.service;

import com.alvna.model.Transaction;
import com.alvna.model.TransactionInput;
import com.alvna.model.TransactionOutput;
import com.alvna.model.Wallet;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class WalletService {

    //returns balance and stores the unspentTransOut's owned by this wallet
    public double getBalance(Wallet w, Map<String, TransactionOutput> unspentTransMap) {
        //if output belongs to me ( if coins belong to me )
        Supplier<Stream<TransactionOutput>> sup = () -> unspentTransMap.values().stream().filter(t -> t.isMine(w.getPublicKey()));
        sup.get().forEach(t -> w.addTransaction(t.id,t));

        return sup.get().mapToDouble(t -> t.value).sum();
    }

    //Generates and returns a new transaction from this wallet.
    public Transaction sendFunds(Wallet w, Map<String, TransactionOutput> unspentTxOutMap, PublicKey _recipient, float value ) {
        if(getBalance(w,unspentTxOutMap) < value) { //gather balance and check funds.
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        //create array list of inputs
        List<TransactionInput> inputs = new ArrayList<TransactionInput>();

        AtomicLong counter = new AtomicLong(0);

        w.getUnspentTxOutMap().values().stream().forEach(
                t -> {
                    counter.getAndAdd((long)t.value);

                    if (counter.get() <= t.value){
                        inputs.add(new TransactionInput(t.id));
                    }
                }
        );

        Transaction newTransaction = new Transaction(w.getPublicKey(), _recipient , value, inputs);
        newTransaction.generateSignature(w.getPrivateKey());

        inputs.stream().forEach(i -> w.getUnspentTxOutMap().remove(i.getTransactionOutputId()));

        return newTransaction;
    }
}
