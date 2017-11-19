/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_tours.li.jaligon.falseto.test;

import fr.univ_tours.li.jaligon.falseto.Generics.Connection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.Log;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.Qfset;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.QuerySession;
import fr.univ_tours.li.jaligon.falseto.Recommendation.ASRA;
import fr.univ_tours.li.jaligon.falseto.Similarity.Query.QueryComparisonByJaccardAndStructureThresholdWithSeveralSelectionPerLevel;
import fr.univ_tours.li.jaligon.falseto.Similarity.Session.CalculateGap;
import fr.univ_tours.li.jaligon.falseto.Similarity.Session.Matrix;
import fr.univ_tours.li.jaligon.falseto.Similarity.Session.SmithWaterman;
import fr.univ_tours.li.jaligon.falseto.logParsing.gpsj.StatisticalStudentSessionLogParsing;
import fr.univ_tours.li.jaligon.falseto.summaryBrowsingDesign.SummaryGUI;

/**
 *
 * @author julien
 */
public class TestDOLAP {

    private static double avgSizeReco = 0;
    private static double avgSizeReco2 = 0;
    private static double nbSizeReco = 0;
    private static double nbSizeReco2 = 0;
    
    private static int flagNovelty = 0;

    private static double avgSize = 0;
    private static double nbSearchGeneration = 0;

    private static double nbNoRecommendation = 0;

    private static double avgRecallQ1 = 0;
    private static double avgRecallQ4 = 0;
    private static double avgRecallQ21 = 0;
    private static double avgRecallQ36 = 0;

    private static double avgPrecisionQ1 = 0;
    private static double avgPrecisionQ4 = 0;
    private static double avgPrecisionQ21 = 0;
    private static double avgPrecisionQ36 = 0;

    private static double avgRecallRelevantQ1 = 0;
    private static double avgRecallRelevantQ4 = 0;
    private static double avgRecallRelevantQ21 = 0;
    private static double avgRecallRelevantQ36 = 0;

    private static double avgPrecisionRelevantQ1 = 0;
    private static double avgPrecisionRelevantQ4 = 0;
    private static double avgPrecisionRelevantQ21 = 0;
    private static double avgPrecisionRelevantQ36 = 0;

    private static double avgSimQ1_Q1 = 0;
    private static double avgSimQ1_Q4 = 0;
    private static double avgSimQ1_Q21 = 0;
    private static double avgSimQ1_Q36 = 0;

    private static double avgSimQ4_Q1 = 0;
    private static double avgSimQ4_Q4 = 0;
    private static double avgSimQ4_Q21 = 0;
    private static double avgSimQ4_Q36 = 0;

    private static double avgSimQ21_Q1 = 0;
    private static double avgSimQ21_Q4 = 0;
    private static double avgSimQ21_Q21 = 0;
    private static double avgSimQ21_Q36 = 0;

    private static double avgSimQ36_Q1 = 0;
    private static double avgSimQ36_Q4 = 0;
    private static double avgSimQ36_Q21 = 0;
    private static double avgSimQ36_Q36 = 0;

    private static double avgNoveltyQ1 = 0;
    private static double avgNoveltyQ4 = 0;
    private static double avgNoveltyQ21 = 0;
    private static double avgNoveltyQ36 = 0;

    private static double avgCoverageQ1 = 0;
    private static double avgCoverageQ4 = 0;
    private static double avgCoverageQ21 = 0;
    private static double avgCoverageQ36 = 0;

    private static double avgRecallQ1Mixed = 0;
    private static double avgRecallQ4Mixed = 0;
    private static double avgRecallQ21Mixed = 0;
    private static double avgRecallQ36Mixed = 0;

    private static double avgPrecisionQ1Mixed = 0;
    private static double avgPrecisionQ4Mixed = 0;
    private static double avgPrecisionQ21Mixed = 0;
    private static double avgPrecisionQ36Mixed = 0;

    private static double avgRecallRelevantQ1Mixed = 0;
    private static double avgRecallRelevantQ4Mixed = 0;
    private static double avgRecallRelevantQ21Mixed = 0;
    private static double avgRecallRelevantQ36Mixed = 0;

    private static double avgPrecisionRelevantQ1Mixed = 0;
    private static double avgPrecisionRelevantQ4Mixed = 0;
    private static double avgPrecisionRelevantQ21Mixed = 0;
    private static double avgPrecisionRelevantQ36Mixed = 0;

    private static double avgSimQ1_Q1Mixed = 0;
    private static double avgSimQ1_Q4Mixed = 0;
    private static double avgSimQ1_Q21Mixed = 0;
    private static double avgSimQ1_Q36Mixed = 0;

    private static double avgSimQ4_Q1Mixed = 0;
    private static double avgSimQ4_Q4Mixed = 0;
    private static double avgSimQ4_Q21Mixed = 0;
    private static double avgSimQ4_Q36Mixed = 0;

    private static double avgSimQ21_Q1Mixed = 0;
    private static double avgSimQ21_Q4Mixed = 0;
    private static double avgSimQ21_Q21Mixed = 0;
    private static double avgSimQ21_Q36Mixed = 0;

    private static double avgSimQ36_Q1Mixed = 0;
    private static double avgSimQ36_Q4Mixed = 0;
    private static double avgSimQ36_Q21Mixed = 0;
    private static double avgSimQ36_Q36Mixed = 0;

    private static double avgNoveltyQ1Mixed = 0;
    private static double avgNoveltyQ4Mixed = 0;
    private static double avgNoveltyQ21Mixed = 0;
    private static double avgNoveltyQ36Mixed = 0;

    private static double avgCoverageQ1Mixed = 0;
    private static double avgCoverageQ4Mixed = 0;
    private static double avgCoverageQ21Mixed = 0;
    private static double avgCoverageQ36Mixed = 0;

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

        System.out.println("Nb of sessions in full log: " + l.getSessions().size());
        System.out.println("Avg Similarity in the Full Log: " + avgSimilarity(l.getSessions()));

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
        System.out.println();

        System.out.println("Nb of sessions in question 1: " + question1_annote.size());
        System.out.println("Similarity in question 1: " + avgSimilarity(question1_annote));

        System.out.println("Nb of sessions in question 4: " + question4_annote.size());
        System.out.println("Similarity in question 4: " + avgSimilarity(question4_annote));

        System.out.println("Nb of sessions in question 21: " + question21_annote.size());
        System.out.println("Similarity in question 21: " + avgSimilarity(question21_annote));

        System.out.println("Nb of sessions in question 36: " + question36_annote.size());
        System.out.println("Similarity in question 36: " + avgSimilarity(question36_annote));

        avgSimQ1_Q4 = 0;

        for (QuerySession qs1 : question1_annote) {
            for (QuerySession qs2 : question4_annote) {
                avgSimQ1_Q4 += compareTwoSessions(qs1, qs2);
            }
        }

        avgSimQ1_Q4 /= ((double) question1_annote.size() * (double) question4_annote.size());

        avgSimQ1_Q21 = 0;

        for (QuerySession qs1 : question1_annote) {
            for (QuerySession qs2 : question21_annote) {
                avgSimQ1_Q21 += compareTwoSessions(qs1, qs2);
            }
        }

        avgSimQ1_Q21 /= ((double) question1_annote.size() * (double) question21_annote.size());

        avgSimQ1_Q36 = 0;

        for (QuerySession qs1 : question1_annote) {
            for (QuerySession qs2 : question36_annote) {
                avgSimQ1_Q36 += compareTwoSessions(qs1, qs2);
            }
        }

        avgSimQ1_Q36 /= ((double) question1_annote.size() * (double) question36_annote.size());

        avgSimQ4_Q21 = 0;

        for (QuerySession qs1 : question4_annote) {
            for (QuerySession qs2 : question21_annote) {
                avgSimQ4_Q21 += compareTwoSessions(qs1, qs2);
            }
        }

        avgSimQ4_Q21 /= ((double) question4_annote.size() * (double) question4_annote.size());

        avgSimQ4_Q36 = 0;

        for (QuerySession qs1 : question4_annote) {
            for (QuerySession qs2 : question36_annote) {
                avgSimQ4_Q36 += compareTwoSessions(qs1, qs2);
            }
        }

        avgSimQ4_Q36 /= ((double) question4_annote.size() * (double) question36_annote.size());

        avgSimQ21_Q36 = 0;

        for (QuerySession qs1 : question21_annote) {
            for (QuerySession qs2 : question36_annote) {
                avgSimQ21_Q36 += compareTwoSessions(qs1, qs2);
            }
        }

        avgSimQ21_Q36 /= ((double) question21_annote.size() * (double) question36_annote.size());

        System.out.println("Matrix of simliarities");

        System.out.println("1;" + avgSimQ1_Q4 + ";" + avgSimQ1_Q21 + ";" + avgSimQ1_Q36);
        System.out.println(avgSimQ1_Q4 + ";1;" + avgSimQ4_Q21 + ";" + avgSimQ4_Q36);
        System.out.println(avgSimQ1_Q21 + ";" + avgSimQ4_Q21 + ";1;" + avgSimQ21_Q36);
        System.out.println(avgSimQ1_Q36 + ";" + avgSimQ4_Q36 + ";" + avgSimQ21_Q36 + ";1");

        double nbLoop = 100;

        for (int i = 0; i < nbLoop; i++) {
            QuerySession qs = sessionGenerationLogSearch(question1_annote, l.getSessions());

            double recall = recall(qs, question1_annote);
            double precision = precision(qs, question1_annote);

            avgRecallQ1 += recall;
            avgPrecisionQ1 += precision;

            recall = recallRelevant(qs, question1_annote);
            precision = precisionRelevant(qs, question1_annote);

            avgRecallRelevantQ1 += recall;
            avgPrecisionRelevantQ1 += precision;

            qs = sessionGenerationLogSearch(question4_annote, l.getSessions());

            recall = recall(qs, question4_annote);
            precision = precision(qs, question4_annote);

            avgRecallQ4 += recall;
            avgPrecisionQ4 += precision;

            recall = recallRelevant(qs, question4_annote);
            precision = precisionRelevant(qs, question4_annote);

            avgRecallRelevantQ4 += recall;
            avgPrecisionRelevantQ4 += precision;

            qs = sessionGenerationLogSearch(question21_annote, l.getSessions());

            recall = recall(qs, question21_annote);
            precision = precision(qs, question21_annote);

            avgRecallQ21 += recall;
            avgPrecisionQ21 += precision;

            recall = recallRelevant(qs, question21_annote);
            precision = precisionRelevant(qs, question21_annote);

            avgRecallRelevantQ21 += recall;
            avgPrecisionRelevantQ21 += precision;

            qs = sessionGenerationLogSearch(question36_annote, l.getSessions());

            recall = recall(qs, question36_annote);
            precision = precision(qs, question36_annote);

            avgRecallQ36 += recall;
            avgPrecisionQ36 += precision;

            recall = recallRelevant(qs, question36_annote);
            precision = precisionRelevant(qs, question36_annote);

            avgRecallRelevantQ36 += recall;
            avgPrecisionRelevantQ36 += precision;

            flagNovelty = 1;
            qs = sessionGenerationRecommendationProposal2(question1_annote, l.getSessions(), 0.25);

            double avgQ1 = avgSimilarityImpactReco(qs, question1_annote);
            double avgQ4 = avgSimilarityImpactReco(qs, question4_annote);
            double avgQ21 = avgSimilarityImpactReco(qs, question21_annote);
            double avgQ36 = avgSimilarityImpactReco(qs, question36_annote);

            avgSimQ1_Q1 += avgQ1;
            avgSimQ1_Q4 += avgQ4;
            avgSimQ1_Q21 += avgQ21;
            avgSimQ1_Q36 += avgQ36;

            avgCoverageQ1 += nbNoRecommendation;
            nbNoRecommendation = 0;

            flagNovelty = 2;
            qs = sessionGenerationRecommendationProposal2(question4_annote, l.getSessions(), 0.25);

            avgQ1 = avgSimilarityImpactReco(qs, question1_annote);
            avgQ4 = avgSimilarityImpactReco(qs, question4_annote);
            avgQ21 = avgSimilarityImpactReco(qs, question21_annote);
            avgQ36 = avgSimilarityImpactReco(qs, question36_annote);

            avgSimQ4_Q1 += avgQ1;
            avgSimQ4_Q4 += avgQ4;
            avgSimQ4_Q21 += avgQ21;
            avgSimQ4_Q36 += avgQ36;

            avgCoverageQ4 += nbNoRecommendation;
            nbNoRecommendation = 0;

            flagNovelty = 3;
            qs = sessionGenerationRecommendationProposal2(question21_annote, l.getSessions(), 0.25);

            avgQ1 = avgSimilarityImpactReco(qs, question1_annote);
            avgQ4 = avgSimilarityImpactReco(qs, question4_annote);
            avgQ21 = avgSimilarityImpactReco(qs, question21_annote);
            avgQ36 = avgSimilarityImpactReco(qs, question36_annote);

            avgSimQ21_Q1 += avgQ1;
            avgSimQ21_Q4 += avgQ4;
            avgSimQ21_Q21 += avgQ21;
            avgSimQ21_Q36 += avgQ36;

            avgCoverageQ21 += nbNoRecommendation;
            nbNoRecommendation = 0;

            flagNovelty = 4;
            qs = sessionGenerationRecommendationProposal2(question36_annote, l.getSessions(), 0.25);

            avgQ1 = avgSimilarityImpactReco(qs, question1_annote);
            avgQ4 = avgSimilarityImpactReco(qs, question4_annote);
            avgQ21 = avgSimilarityImpactReco(qs, question21_annote);
            avgQ36 = avgSimilarityImpactReco(qs, question36_annote);

            avgSimQ36_Q1 += avgQ1;
            avgSimQ36_Q4 += avgQ4;
            avgSimQ36_Q21 += avgQ21;
            avgSimQ36_Q36 += avgQ36;

            avgCoverageQ36 += nbNoRecommendation;
            nbNoRecommendation = 0;

            flagNovelty = 5;
            qs = sessionGenerationLogSearchAndRecommendation(question1_annote, l.getSessions());

            recall = recall(qs, question1_annote);
            precision = precision(qs, question1_annote);

            avgRecallQ1Mixed += recall;
            avgPrecisionQ1Mixed += precision;

            recall = recallRelevant(qs, question1_annote);
            precision = precisionRelevant(qs, question1_annote);

            avgRecallRelevantQ1Mixed += recall;
            avgPrecisionRelevantQ1Mixed += precision;

            avgQ1 = avgSimilarityImpactReco(qs, question1_annote);
            avgQ4 = avgSimilarityImpactReco(qs, question4_annote);
            avgQ21 = avgSimilarityImpactReco(qs, question21_annote);
            avgQ36 = avgSimilarityImpactReco(qs, question36_annote);

            avgSimQ1_Q1Mixed += avgQ1;
            avgSimQ1_Q4Mixed += avgQ4;
            avgSimQ1_Q21Mixed += avgQ21;
            avgSimQ1_Q36Mixed += avgQ36;

            avgCoverageQ1Mixed += nbNoRecommendation;

            nbNoRecommendation = 0;

            flagNovelty = 6;
            qs = sessionGenerationLogSearchAndRecommendation(question4_annote, l.getSessions());

            recall = recall(qs, question4_annote);
            precision = precision(qs, question4_annote);

            avgRecallQ4Mixed += recall;
            avgPrecisionQ4Mixed += precision;

            recall = recallRelevant(qs, question4_annote);
            precision = precisionRelevant(qs, question4_annote);

            avgRecallRelevantQ4Mixed += recall;
            avgPrecisionRelevantQ4Mixed += precision;

            avgQ1 = avgSimilarityImpactReco(qs, question1_annote);
            avgQ4 = avgSimilarityImpactReco(qs, question4_annote);
            avgQ21 = avgSimilarityImpactReco(qs, question21_annote);
            avgQ36 = avgSimilarityImpactReco(qs, question36_annote);

            avgSimQ4_Q1Mixed += avgQ1;
            avgSimQ4_Q4Mixed += avgQ4;
            avgSimQ4_Q21Mixed += avgQ21;
            avgSimQ4_Q36Mixed += avgQ36;

            avgCoverageQ4Mixed += nbNoRecommendation;
            nbNoRecommendation = 0;

            flagNovelty = 7;
            qs = sessionGenerationLogSearchAndRecommendation(question21_annote, l.getSessions());

            recall = recall(qs, question21_annote);
            precision = precision(qs, question21_annote);

            avgRecallQ21Mixed += recall;
            avgPrecisionQ21Mixed += precision;

            recall = recallRelevant(qs, question21_annote);
            precision = precisionRelevant(qs, question21_annote);

            avgRecallRelevantQ21Mixed += recall;
            avgPrecisionRelevantQ21Mixed += precision;

            avgQ1 = avgSimilarityImpactReco(qs, question1_annote);
            avgQ4 = avgSimilarityImpactReco(qs, question4_annote);
            avgQ21 = avgSimilarityImpactReco(qs, question21_annote);
            avgQ36 = avgSimilarityImpactReco(qs, question36_annote);

            avgSimQ21_Q1Mixed += avgQ1;
            avgSimQ21_Q4Mixed += avgQ4;
            avgSimQ21_Q21Mixed += avgQ21;
            avgSimQ21_Q36Mixed += avgQ36;

            avgCoverageQ21Mixed += nbNoRecommendation;
            nbNoRecommendation = 0;

            flagNovelty = 8;
            qs = sessionGenerationLogSearchAndRecommendation(question36_annote, l.getSessions());

            recall = recall(qs, question36_annote);
            precision = precision(qs, question36_annote);

            avgRecallQ36Mixed += recall;
            avgPrecisionQ36Mixed += precision;

            recall = recallRelevant(qs, question36_annote);
            precision = precisionRelevant(qs, question36_annote);

            avgRecallRelevantQ36Mixed += recall;
            avgPrecisionRelevantQ36Mixed += precision;

            avgQ1 = avgSimilarityImpactReco(qs, question1_annote);
            avgQ4 = avgSimilarityImpactReco(qs, question4_annote);
            avgQ21 = avgSimilarityImpactReco(qs, question21_annote);
            avgQ36 = avgSimilarityImpactReco(qs, question36_annote);

            avgSimQ36_Q1Mixed += avgQ1;
            avgSimQ36_Q4Mixed += avgQ4;
            avgSimQ36_Q21Mixed += avgQ21;
            avgSimQ36_Q36Mixed += avgQ36;

            avgCoverageQ36Mixed += nbNoRecommendation;
            nbNoRecommendation = 0;

        }

        System.out.println();
        System.out.println("Average Size of Log Search Generation:" + (avgSize / nbSearchGeneration));
        System.out.println();

        System.out.println("Log Search Generation");
        System.out.println("---------------------");

        System.out.println("Q1;" + avgRecallQ1 / nbLoop + ";" + avgPrecisionQ1 / nbLoop + ";" + avgRecallRelevantQ1 / nbLoop + ";" + avgPrecisionRelevantQ1 / nbLoop);
        System.out.println("Q4;" + avgRecallQ4 / nbLoop + ";" + avgPrecisionQ4 / nbLoop + ";" + avgRecallRelevantQ4 / nbLoop + ";" + avgPrecisionRelevantQ4 / nbLoop);
        System.out.println("Q21;" + avgRecallQ21 / nbLoop + ";" + avgPrecisionQ21 / nbLoop + ";" + avgRecallRelevantQ21 / nbLoop + ";" + avgPrecisionRelevantQ21 / nbLoop);
        System.out.println("Q36;" + avgRecallQ36 / nbLoop + ";" + avgPrecisionQ36 / nbLoop + ";" + avgRecallRelevantQ36 / nbLoop + ";" + avgPrecisionRelevantQ36 / nbLoop);

        System.out.println();
        System.out.println("Average Size of Reco:" + (avgSizeReco / nbSizeReco));
        System.out.println();
        
        System.out.println("Reco Generation");
        System.out.println("---------------");

        System.out.println("Q1;" + avgSimQ1_Q1 / nbLoop + ";" + avgSimQ1_Q4 / nbLoop + ";" + avgSimQ1_Q21 / nbLoop + ";" + avgSimQ1_Q36 / nbLoop + ";" + avgNoveltyQ1 / nbLoop + ";" + (1.0 - (avgCoverageQ1 / nbLoop)));
        System.out.println("Q4;" + avgSimQ4_Q1 / nbLoop + ";" + avgSimQ4_Q4 / nbLoop + ";" + avgSimQ4_Q21 / nbLoop + ";" + avgSimQ4_Q36 / nbLoop + ";" + avgNoveltyQ4 / nbLoop + ";" + (1.0 - (avgCoverageQ4 / nbLoop)));
        System.out.println("Q21;" + avgSimQ21_Q1 / nbLoop + ";" + avgSimQ21_Q4 / nbLoop + ";" + avgSimQ21_Q21 / nbLoop + ";" + avgSimQ21_Q36 / nbLoop + ";" + avgNoveltyQ21 / nbLoop + ";" + (1.0 - (avgCoverageQ21 / nbLoop)));
        System.out.println("Q36;" + avgSimQ36_Q1 / nbLoop + ";" + avgSimQ36_Q4 / nbLoop + ";" + avgSimQ36_Q21 / nbLoop + ";" + avgSimQ36_Q36 / nbLoop + ";" + avgNoveltyQ36 / nbLoop + ";" + (1.0 - (avgCoverageQ36 / nbLoop)));

        System.out.println();
        System.out.println("Average Size of Reco:" + (avgSizeReco2 / nbSizeReco2));
        System.out.println();
        
        System.out.println("Mixed Generation");
        System.out.println("----------------");
        
        System.out.println("Q1;" + avgRecallQ1Mixed / nbLoop + ";" + avgPrecisionQ1Mixed / nbLoop + ";" + avgRecallRelevantQ1Mixed / nbLoop + ";" + avgPrecisionRelevantQ1Mixed / nbLoop + ";" + avgSimQ1_Q1Mixed / nbLoop + ";" + avgSimQ1_Q4Mixed / nbLoop + ";" + avgSimQ1_Q21Mixed / nbLoop + ";" + avgSimQ1_Q36Mixed / nbLoop + ";" + avgNoveltyQ1Mixed / nbLoop + ";" + (1.0 - (avgCoverageQ1Mixed / nbLoop)));
        System.out.println("Q4;" + avgRecallQ4Mixed / nbLoop + ";" + avgPrecisionQ4Mixed / nbLoop + ";" + avgRecallRelevantQ4Mixed / nbLoop + ";" + avgPrecisionRelevantQ4Mixed / nbLoop + ";" + avgSimQ4_Q1Mixed / nbLoop + ";" + avgSimQ4_Q4Mixed / nbLoop + ";" + avgSimQ4_Q21Mixed / nbLoop + ";" + avgSimQ4_Q36Mixed / nbLoop + ";" + avgNoveltyQ4Mixed / nbLoop + ";" + (1.0 - (avgCoverageQ4Mixed / nbLoop)));
        System.out.println("Q21;" + avgRecallQ21Mixed / nbLoop + ";" + avgPrecisionQ21Mixed / nbLoop + ";" + avgRecallRelevantQ21Mixed / nbLoop + ";" + avgPrecisionRelevantQ21Mixed / nbLoop + ";" + avgSimQ21_Q1Mixed / nbLoop + ";" + avgSimQ21_Q4Mixed / nbLoop + ";" + avgSimQ21_Q21Mixed / nbLoop + ";" + avgSimQ21_Q36Mixed / nbLoop + ";" + avgNoveltyQ21Mixed / nbLoop + ";" + (1.0 - (avgCoverageQ21Mixed / nbLoop)));
        System.out.println("Q36;" + avgRecallQ36Mixed / nbLoop + ";" + avgPrecisionQ36Mixed / nbLoop + ";" + avgRecallRelevantQ36Mixed / nbLoop + ";" + avgPrecisionRelevantQ36Mixed / nbLoop + ";" + avgSimQ36_Q1Mixed / nbLoop + ";" + avgSimQ36_Q4Mixed / nbLoop + ";" + avgSimQ36_Q21Mixed / nbLoop + ";" + avgSimQ36_Q36Mixed / nbLoop + ";" + avgNoveltyQ36Mixed / nbLoop + ";" + (1.0 - (avgCoverageQ36Mixed / nbLoop)));

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

    private static double compareTwoSessions(QuerySession session1, QuerySession session2) {
        double thre = 0.7;//threshold of comparison for queries and sessions (have to be the same for a normalized result)
        Matrix matrix = new Matrix(session1, session2, 0.33, 0.33, 0.33);//the matrix used in Smith Waterman (based on Edit Distance if i remember well); 0.33 values should always be the same
        matrix.fillMatrix_MatchMisMatch(thre);//set the threshold for comparing queries
        //matrix.applySymmetricIncreasingFunction();//for considering that the last queries of the sessions are more important for the similarity than the others
        double gap = new CalculateGap(matrix).calculateExtGap_AvgMatch();//the gap that is allowed between two sessions
        SmithWaterman sw = new SmithWaterman(matrix, 0, gap, session1, session2, thre, 0.35, 0.5, 0.15);//0.35 -> the weigth for the projections; 0.5 -> the weight for the selections; 0.15 -> the weight for the measures 
        return sw.computeSimilarity().getSimilarity();
    }

    private static double avgSimilarity(HashSet<QuerySession> sessions) {
        List<QuerySession> list = new ArrayList<>(sessions);

        double sum = 0;
        double nb = 0;

        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                sum += compareTwoSessions(list.get(i), list.get(j));
                nb++;
            }
        }

        return sum / nb;
    }

    private static QuerySession sessionGenerationLogSearch(HashSet<QuerySession> question, HashSet<QuerySession> log) {
        QuerySession result = new QuerySession("synthetic");

        List<QuerySession> questionList = new ArrayList<>(question);
        Random r = new Random();
        
        QuerySession randomSession = questionList.get(r.nextInt(questionList.size()));
        
        Qfset initialQuery = randomSession.get(0);
        result.add(initialQuery);

        boolean stop = false;

        int probability = 12;

        Log l = new Log(log);
        
        while (!stop) {
            int size = result.size();

            HashSet<QuerySession> subsetLog = l.select(result.get(result.size() - 1));

            if (!subsetLog.isEmpty()) {
                double maxSim = 0;
                Qfset closestQuery = null;

                for (QuerySession qs : subsetLog) {
                    for (Qfset q : qs.getQueries()) {
                        Qfset currentQuery = result.get(result.size() - 1);

                        boolean next = false;
                        for (Qfset qR : result.getQueries()) {
                            if (qR.isEqual(q)) {
                                next = true;
                            }
                        }

                        if (next) {
                            continue;
                        }

                        QueryComparisonByJaccardAndStructureThresholdWithSeveralSelectionPerLevel comp = new QueryComparisonByJaccardAndStructureThresholdWithSeveralSelectionPerLevel(q, currentQuery, 0.35, 0.5, 0.15);
                        double sim = comp.computeSimilarity().getSimilarity();
                        if (sim > maxSim) {
                            maxSim = sim;
                            closestQuery = q;
                        }
                    }
                }

                if(closestQuery != null)
                result.add(closestQuery);
            }

            if (result.size() == size || 0 == r.nextInt(probability)) {
                stop = true;
            } else {
                probability--;
            }
        }

        avgSize += (double) result.size();
        nbSearchGeneration++;
        return result;
    }

    /*private static QuerySession sessionGenerationRecommendationProposal1(HashSet<QuerySession> question, HashSet<QuerySession> log) {
     QuerySession result = new QuerySession("synthetic");

     Iterator<QuerySession> it = question.iterator();
     QuerySession randomSession = it.next();
     Qfset initialQuery = randomSession.get(0);
     result.add(initialQuery);

     boolean stop = false;

     int probability = 10;

     while (!stop) {
     int size = result.size();

     ASRA recoSys = new ASRA(log, result.getQueries());
     QuerySession reco = null;
     try {
     reco = recoSys.computeASRA();
     } catch (OutOfMemoryError e) {
     reco = null;
     } catch (Exception e) {
     reco = null;
     }

     if (reco != null && !reco.getQueries().isEmpty()) {
     Qfset maxSimilarityQuery = null;
     double maxValue = 0;
     for (Qfset q : reco.getQueries()) {
     QueryComparisonByJaccardAndStructureThresholdWithSeveralSelectionPerLevel comp = new QueryComparisonByJaccardAndStructureThresholdWithSeveralSelectionPerLevel(q, result.get(result.size() - 1), 0.35, 0.5, 0.15);

     double sim = comp.computeSimilarity().getSimilarity();
     if (sim > maxValue) {
     maxValue = sim;
     maxSimilarityQuery = q;
     }
     }

     result.add(maxSimilarityQuery);
     }

     Random r = new Random();
     if (result.size() == size || 0 == r.nextInt(probability)) {
     stop = true;
     } else {
     probability--;
     }
     }

     return result;
     }*/
    private static QuerySession sessionGenerationRecommendationProposal2(HashSet<QuerySession> question, HashSet<QuerySession> log, double cutPercentage) {
        HashSet<QuerySession> logWithoutRandomSession = new HashSet<>(log);

        QuerySession result = new QuerySession("synthetic");

        List<QuerySession> questionList = new ArrayList<>(question);
        Random r = new Random();
        
        QuerySession randomSession = questionList.get(r.nextInt(questionList.size()));
        int currentQueryPosition = (int) Math.round(randomSession.size() * cutPercentage) - 1;

        result.addAll(randomSession.getQueries().subList(0, currentQueryPosition + 1));
        //System.out.println("Result size "+result.size());

        logWithoutRandomSession.remove(randomSession);

        ASRA recoSys = new ASRA(logWithoutRandomSession, result.getQueries());
        QuerySession reco = null;
        try {
            reco = recoSys.computeASRA();
        } catch (OutOfMemoryError e) {
            reco = null;
        } catch (Exception e) {
            reco = null;
        }

        if (reco != null && !reco.getQueries().isEmpty()) {
            result.addAll(reco.getQueries());
            QuerySession recoTest = new QuerySession(reco.getQueries(), "10000");

            avgSizeReco+=recoTest.size();
            nbSizeReco++;
            
            if (flagNovelty == 1) {
                avgNoveltyQ1 += novelty(recoTest, log);
            } else if (flagNovelty == 2) {
                avgNoveltyQ4 += novelty(recoTest, log);
            } else if (flagNovelty == 3) {
                avgNoveltyQ21 += novelty(recoTest, log);
            } else if (flagNovelty == 4) {
                avgNoveltyQ36 += novelty(recoTest, log);
            }
        } else {
            nbNoRecommendation++;
        }

        return result;
    }

    private static QuerySession sessionGenerationLogSearchAndRecommendation(HashSet<QuerySession> question, HashSet<QuerySession> log) {
        QuerySession result = sessionGenerationLogSearch(question, log);

        ASRA recoSys = new ASRA(log, result.getQueries());
        QuerySession reco = null;
        try {
            reco = recoSys.computeASRA();
        } catch (OutOfMemoryError e) {
            reco = null;
        } catch (Exception e) {
            reco = null;
        }

        if (reco != null && !reco.getQueries().isEmpty()) {
            result.addAll(reco.getQueries());
            QuerySession recoTest = new QuerySession(reco.getQueries(), "10000");
            avgSizeReco2+=recoTest.size();
            nbSizeReco2++;
            
            if (flagNovelty == 5) {
                avgNoveltyQ1Mixed += novelty(recoTest, log);
            } else if (flagNovelty == 6) {
                avgNoveltyQ4Mixed += novelty(recoTest, log);
            } else if (flagNovelty == 7) {
                avgNoveltyQ21Mixed += novelty(recoTest, log);
            } else if (flagNovelty == 8) {
                avgNoveltyQ36Mixed += novelty(recoTest, log);
            }
            
            
        }

        return result;
    }

    private static double recall(QuerySession simulation, HashSet<QuerySession> question) {
        double commonQueries = 0;

        List<Qfset> uniqueQueriesInSimulation = new ArrayList<>();
        for (Qfset q : simulation.getQueries()) {
            boolean add = true;
            for (Qfset q2 : uniqueQueriesInSimulation) {
                if (q.isEqual(q2)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                uniqueQueriesInSimulation.add(q);
            }
        }

        List<Qfset> uniqueQueriesInQuestion = new ArrayList<>();
        for (QuerySession qs : question) {

            for (Qfset q : qs.getQueries()) {
                boolean add = true;
                for (Qfset q2 : uniqueQueriesInQuestion) {
                    if (q.isEqual(q2)) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    uniqueQueriesInQuestion.add(q);
                }
            }
        }

        for (Qfset q : uniqueQueriesInSimulation) {
            for (Qfset q2 : uniqueQueriesInQuestion) {
                if (q.isEqual(q2)) {
                    commonQueries++;
                }
            }
        }

        return commonQueries / (double) uniqueQueriesInQuestion.size();

    }

    private static double precision(QuerySession simulation, HashSet<QuerySession> question) {
        double commonQueries = 0;

        List<Qfset> uniqueQueriesInSimulation = new ArrayList<>();
        for (Qfset q : simulation.getQueries()) {
            boolean add = true;
            for (Qfset q2 : uniqueQueriesInSimulation) {
                if (q.isEqual(q2)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                uniqueQueriesInSimulation.add(q);
            }
        }

        List<Qfset> uniqueQueriesInQuestion = new ArrayList<>();
        for (QuerySession qs : question) {

            for (Qfset q : qs.getQueries()) {
                boolean add = true;
                for (Qfset q2 : uniqueQueriesInQuestion) {
                    if (q.isEqual(q2)) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    uniqueQueriesInQuestion.add(q);
                }
            }
        }

        for (Qfset q : uniqueQueriesInSimulation) {
            for (Qfset q2 : uniqueQueriesInQuestion) {
                if (q.isEqual(q2)) {
                    commonQueries++;
                }
            }
        }

        return commonQueries / (double) uniqueQueriesInSimulation.size();
    }

    private static double recallRelevant(QuerySession simulation, HashSet<QuerySession> question) {
        List<Qfset> relevantQueriesInQuestion = new ArrayList<>();

        for (QuerySession qs : question) {
            for (Qfset q : qs.getRelevantQueries()) {
                boolean add = true;
                for (Qfset q2 : relevantQueriesInQuestion) {
                    if (q.isEqual(q2)) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    relevantQueriesInQuestion.add(q);
                }
            }
        }

        double nbCommonRelevantQueries = 0;

        List<Qfset> uniqueQueriesInSimulation = new ArrayList<>();

        for (Qfset q : simulation.getQueries()) {
            boolean add = true;
            for (Qfset q2 : uniqueQueriesInSimulation) {
                if (q.isEqual(q2)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                uniqueQueriesInSimulation.add(q);
            }
        }

        for (Qfset q : uniqueQueriesInSimulation) {
            for (Qfset q2 : relevantQueriesInQuestion) {
                if (q.isEqual(q2)) {
                    nbCommonRelevantQueries++;
                }
            }
        }

        return nbCommonRelevantQueries / (double) relevantQueriesInQuestion.size();

    }

    private static double precisionRelevant(QuerySession simulation, HashSet<QuerySession> question) {
        List<Qfset> relevantQueriesInQuestion = new ArrayList<>();

        for (QuerySession qs : question) {
            for (Qfset q : qs.getRelevantQueries()) {
                boolean add = true;
                for (Qfset q2 : relevantQueriesInQuestion) {
                    if (q.isEqual(q2)) {
                        add = false;
                        break;
                    }
                }
                if (add) {
                    relevantQueriesInQuestion.add(q);
                }
            }

        }

        double nbCommonRelevantQueries = 0;

        List<Qfset> uniqueQueriesInSimulation = new ArrayList<>();
        for (Qfset q : simulation.getQueries()) {
            boolean add = true;
            for (Qfset q2 : uniqueQueriesInSimulation) {
                if (q.isEqual(q2)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                uniqueQueriesInSimulation.add(q);
            }
        }

        for (Qfset q : uniqueQueriesInSimulation) {
            for (Qfset q2 : relevantQueriesInQuestion) {
                if (q.isEqual(q2)) {
                    nbCommonRelevantQueries++;
                }
            }
        }

        return nbCommonRelevantQueries / (double) uniqueQueriesInSimulation.size();
    }

    private static double novelty(QuerySession simulation, HashSet<QuerySession> log) {

        double nbExistingQueries = 0;

        for (Qfset q : simulation.getQueries()) {
            boolean exist = false;
            for (QuerySession qs : log) {
                for (Qfset q2 : qs.getQueries()) {
                    if (q.isEqual(q2)) {
                        nbExistingQueries++;
                        exist = true;
                    }

                    if (exist) {
                        break;
                    }
                }
                if (exist) {
                    break;
                }
            }
        }

        return ((double) simulation.getQueries().size() - nbExistingQueries) / (double) simulation.getQueries().size();
    }

    private static double avgSimilarityImpactReco(QuerySession session, HashSet<QuerySession> question) {

        double sum = 0;

        for (QuerySession qs : question) {
            sum += compareTwoSessions(qs, session);
        }

        return sum / (double) question.size();
    }
}
