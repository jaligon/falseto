/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.univ_tours.li.jaligon.falseto.test;

import java.io.IOException;

/**
 *
 * @author julien
 */
public class TransactionsToFrequentItemSets {

    public TransactionsToFrequentItemSets(float relativeMinsupp) {
        int minSupp = Math.round((float)ItemSets.nbTransactions*relativeMinsupp);//min supp is 1% of the transactions
        String command = "fimi03b/fim_all transactions-converted.txt "+minSupp+" frequentItemSets.txt";
        System.out.println(command);
        executeUnixCommand(command);
    }
    
    
    
    private void executeUnixCommand(String command) {

        Process p;
        //BufferedReader reader = null;
        try {
            p = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
            p.waitFor();
            //reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
