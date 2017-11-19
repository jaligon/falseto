/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_tours.li.jaligon.falseto.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julien
 */
public class TransactionTools {

    public static HashMap<String, Integer> dictionnary = new HashMap<String, Integer>();
    public static HashMap<String, Integer> wordFrequences = new HashMap<String, Integer>();
    public static int idWord = 0;

    public static void convert(String fileName) {
        BufferedReader reader = null;

        try {
            PrintWriter file = new PrintWriter(new FileWriter("transactions-converted.txt"));

            reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null && !"".equals(line)) {
                StringTokenizer st = new StringTokenizer(line, " ");

                String convertedConfig = "";

                while (st.hasMoreTokens()) {
                    String word = st.nextToken();
                    if (!dictionnary.containsKey(word)) {
                        idWord++;
                        dictionnary.put(word, idWord);
                    }
                    convertedConfig += dictionnary.get(word) + " ";

                    //compute the frequences of each word of the transactions (for instance, used to compute lift scores)
                    if (!wordFrequences.containsKey(word)) {
                        wordFrequences.put(word, 1);
                    } else {
                        int freq = wordFrequences.get(word);
                        wordFrequences.put(word, freq + 1);
                    }

                }

                convertedConfig = convertedConfig.substring(0, convertedConfig.length() - 1);
                file.println(convertedConfig);
            }
            file.close();
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

    public static void translation(String fileName) throws FileNotFoundException, IOException {
        File dico = new File("dico.txt");

        HashMap<String, String> dicoMap = new HashMap<>();

        BufferedReader reader = new BufferedReader(new FileReader(dico));
        String line;
        while ((line = reader.readLine()) != null && !"".equals(line)) {
            StringTokenizer st = new StringTokenizer(line, " ");

            String config = st.nextToken();
            String id = st.nextToken();

            dicoMap.put(id, config);

        }

        reader.close();

        File input = new File(fileName);

        reader = new BufferedReader(new FileReader(input));
        line = " ";

        PrintWriter file = new PrintWriter(new FileWriter("translation-FrequentItemSets.txt"));

        while ((line = reader.readLine()) != null && !"".equals(line)) {
            StringTokenizer st = new StringTokenizer(line, " ");
            String translatedLine = "";
            while (st.hasMoreTokens()) {
                String item = st.nextToken();
                if (!item.contains("(")) {
                    translatedLine += dicoMap.get(item) + " ";
                } else {
                    translatedLine += item + " ";
                }
            }
            file.println(translatedLine);
        }

        file.close();
        reader.close();
    }

    public static void saveDictionnary() {
        try {
            PrintWriter file = new PrintWriter(new FileWriter("dico.txt"));
            for (String s : TransactionTools.dictionnary.keySet()) {
                file.println(s + " " + TransactionTools.dictionnary.get(s));
            }
            file.close();
        } catch (IOException ex) {
            Logger.getLogger(TransactionTools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
