/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_tours.li.jaligon.falseto.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author julien
 */
public class FrequentItemSetsToGraphViz {

    //edge size : penwidth from 1 to 6
    public static void convert(String fileName, int minLift) {
        double numbreOfTransactions = ItemSets.nbTransactions;
        double penWidthNodes = 30;
        double penWidthEdges = 100;

        BufferedReader reader = null;

        List<String> nodes = new ArrayList<>();
        List<String> edges = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null && !"".equals(line)) {
                StringTokenizer st = new StringTokenizer(line, " ");

                //System.out.println(st.countTokens());
                if (st.countTokens() > 3 || st.countTokens() == 1)//we are only intersted to consider 2-itemsets (plus the support value)
                {
                    continue;
                }

                String firstItem = st.nextToken();
                //System.out.println("first "+firstItem);
                String secondItem = null;
                //System.out.println(st.countTokens());
                boolean singleton = true;
                if (st.countTokens() == 2) {//number of tokens that have to be identified yet.
                    secondItem = st.nextToken();
                    singleton = false;
                    //System.out.println("second "+secondItem);
                }
                String support = st.nextToken();
                //System.out.println("support "+support);
                double supportValue = Double.parseDouble(support.substring(1, support.length() - 1));

                if (singleton) {//we have to manage nodes
                    String dotLine = "\"" + firstItem + "\" [degree=" + (supportValue / numbreOfTransactions * penWidthNodes) + "]";
                    nodes.add(dotLine);
                } else//we have to manage edges
                {
                    double liftScore = (supportValue / numbreOfTransactions) / (((double) TransactionTools.wordFrequences.get(firstItem) / numbreOfTransactions) * ((double) TransactionTools.wordFrequences.get(secondItem) / numbreOfTransactions));
                    System.out.println("Lift : "+liftScore+" "+supportValue+" "+TransactionTools.wordFrequences.get(firstItem)+" "+TransactionTools.wordFrequences.get(secondItem));
                    if (liftScore > minLift) {
                        String dotLine = "\"" + firstItem + "\" -- \"" + secondItem + "\" [weight=" + (supportValue / numbreOfTransactions * penWidthEdges) + "]";
                        edges.add(dotLine);
                    }
                }

            }
            saveDotFile(nodes, edges);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static void saveDotFile(List<String> nodes, List<String> edges) {
        try {
            PrintWriter file = new PrintWriter(new FileWriter("graphViz.dot"));

            file.println("graph G { ");
            for (String n : nodes) {
                boolean isAlone = true;
                for (String e : edges) {
                    if (e.contains(n.substring(0, n.indexOf("\"", 1)) + "\"")) {
                        isAlone = false;
                        break;
                    }
                }

                if (!isAlone) {
                    file.println(n + ";");
                }
            }
            for (String e : edges) {
                file.println(e + ";");
            }
            file.println("}");

            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
