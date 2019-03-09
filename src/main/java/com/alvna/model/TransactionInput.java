package com.alvna.model;


public class TransactionInput {
    private String transactionOutputId; //Reference to TransactionOutputs -> transactionId
    private TransactionOutput unspentTransOut; //Contains the Unspent transaction output

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    public void setTransactionOutputId(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public TransactionOutput getUnspentTransOut() {
        return unspentTransOut;
    }

    public void setUnspentTransOut(TransactionOutput unspentTransOut) {
        this.unspentTransOut = unspentTransOut;
    }
}