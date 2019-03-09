package com.alvna.service;

import com.alvna.model.Block;
import com.alvna.model.Transaction;
import com.alvna.util.StringUtil;

public class BlockService {

    //Increases nonce value until hash target is reached.
    public void mineBlock(Block b, int difficulty) {
        b.setMerkleRoot(StringUtil.getMerkleRoot(b.getTransactions()));
        String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0"
        while(!b.getHash().substring( 0, difficulty).equals(target)) {
            b.setNonce(b.getNonce() + 1);
            b.setHash(b.calculateHash());
        }
        System.out.println("Block Mined!!! : " + b.getHash());
    }

    //Add transactions to this block
    public boolean addTransaction(Block b, Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) return false;
        if((b.getPreviousHash() != "0")) {
            if((transaction.processTransaction() != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        b.getTransactions().add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }

}