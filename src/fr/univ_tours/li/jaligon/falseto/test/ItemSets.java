/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_tours.li.jaligon.falseto.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.MeasureFragment;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.ProjectionFragment;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.Qfset;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.QuerySession;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.SelectionFragment;

/**
 *
 * @author julien
 */
public class ItemSets {

    public static int nbTransactions = 0;

    public static List<String> getItemSets(HashSet<QuerySession> sessions) {
        List<String> itemsets = new ArrayList<>();

        for (QuerySession qs : sessions) {
            for (Qfset q : qs.getQueries()) {
                String s = "";
                for (ProjectionFragment pf : q.getAttributes()) {
                    if (!pf.getLevel().isAll()) {
                        s += pf.getLevel().getName() + " ";
                    }
                }
                for (SelectionFragment sf : q.getSelectionPredicates()) {
                    s += sf.getLevel().getName()+"='"+sf.getValue().getName() + "' ";
                }
                for (MeasureFragment mf : q.getMeasures()) {
                    s += mf.getAttribute().getName() + " ";
                }
                s = s.substring(0, s.length() - 1);
                itemsets.add(s);
            }
        }
        System.out.println(itemsets.size());
        nbTransactions = itemsets.size();

        return itemsets;
    }

    public static void saveTransactions(List<String> transactions) {
        Set<String> stopWords = new HashSet<>();

        String line = "";

        try {
            PrintWriter file = new PrintWriter(new FileWriter("transactions.txt"));

            for (String s : transactions) {
                file.write(s + "\n");
            }

            file.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(line);
        }

        //System.out.println(output.toString());
    }

}
