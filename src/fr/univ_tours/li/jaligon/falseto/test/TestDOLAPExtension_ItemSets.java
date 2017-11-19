/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_tours.li.jaligon.falseto.test;

import fr.univ_tours.li.jaligon.falseto.Generics.Connection;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.Log;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.QuerySession;
import fr.univ_tours.li.jaligon.falseto.logParsing.gpsj.StatisticalStudentSessionLogParsing;
import fr.univ_tours.li.jaligon.falseto.summaryBrowsingDesign.SummaryGUI;

/**
 *
 * @author julien ATTENTION : CODE SUPER MOCHE, AMES SENSIBLES : S'ABSTENIR
 */
public class TestDOLAPExtension_ItemSets {

    static double AvgSize = 0;

    public static void main(String args[]) {

        Connection c = null;

        try {
            c = new Connection();
            c.open();
            //display = true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            //configurationActionPerformed();
            //display = false;
        }

        Log l = getLogs("logs-annote/");

        HashSet<QuerySession> question1_annote = new HashSet<>();
        HashSet<QuerySession> question4_annote = new HashSet<>();
        HashSet<QuerySession> question21_annote = new HashSet<>();
        HashSet<QuerySession> question36_annote = new HashSet<>();

        for (QuerySession qs : l.getSessions()) {
            if (qs.getIdQuestion() == 1) {
                question1_annote.add(qs);
            } else if (qs.getIdQuestion() == 4) {
                question4_annote.add(qs);
            } else if (qs.getIdQuestion() == 21) {
                question21_annote.add(qs);
            } else if (qs.getIdQuestion() == 36) {
                question36_annote.add(qs);
            }
        }

        List<String> itemSets = ItemSets.getItemSets(question1_annote);
        ItemSets.saveTransactions(itemSets);
        TransactionTools.convert("transactions.txt");
        TransactionTools.saveDictionnary();
        new TransactionsToFrequentItemSets((float) 0.05);
        
        try {
            TransactionTools.translation("frequentItemSets.txt");
        } catch (IOException ex) {
            Logger.getLogger(TestDOLAPExtension_ItemSets.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        FrequentItemSetsToGraphViz.convert("translation-FrequentItemSets.txt", 1);
        
        c.close();
    }

    private static Log getLogs(String path) {
        HashSet<QuerySession> result = new HashSet<>();

        File folder = new File(path);
        File[] folders = folder.listFiles();

        if (folders != null) {
            for (int i = 0; i < folders.length; i++) {
                if (folders[i].isDirectory()) {
                    File file = null;
                    try {
                        file = new File(folders[i].getCanonicalPath());
                        System.out.println("Folder " + folders[i].getCanonicalPath());
                    } catch (IOException ex) {
                        Logger.getLogger(SummaryGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    File[] files = file.listFiles();
                    if (files != null) {
                        for (int j = 0; j < files.length; j++) {
                            if (!files[j].isDirectory() && files[j].getName().endsWith(".txt")) {
                                StatisticalStudentSessionLogParsing lp = null;
                                try {
                                    lp = new StatisticalStudentSessionLogParsing(files[j].getCanonicalPath());
                                    System.out.println("File " + files[j].getCanonicalPath());
                                } catch (IOException ex) {
                                    Logger.getLogger(SummaryGUI.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                Log log = lp.ReadSessionLog();
                                result.addAll(log.getSessions());
                            }
                        }
                    }
                }
            }
        }

        if (!result.isEmpty()) {
            Log l = new Log(result);
            return l;
        } else {
            return null;
        }
    }

}
