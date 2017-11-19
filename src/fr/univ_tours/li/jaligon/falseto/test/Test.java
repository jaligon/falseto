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
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.Log;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.MeasureFragment;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.ProjectionFragment;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.Qfset;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.QuerySession;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.SelectionFragment;
import fr.univ_tours.li.jaligon.falseto.Recommendation.ASRA;
import fr.univ_tours.li.jaligon.falseto.Similarity.Query.QueryComparisonByJaccardAndStructureThresholdWithSeveralSelectionPerLevel;
import fr.univ_tours.li.jaligon.falseto.Similarity.Session.CalculateGap;
import fr.univ_tours.li.jaligon.falseto.Similarity.Session.Matrix;
import fr.univ_tours.li.jaligon.falseto.Similarity.Session.SmithWaterman;
import fr.univ_tours.li.jaligon.falseto.logParsing.gpsj.StatisticalStudentSessionLogParsing;
import fr.univ_tours.li.jaligon.falseto.summaryBrowsingDesign.SummaryGUI;

/**
 *
 * @author julien ATTENTION : CODE SUPER MOCHE, AMES SENSIBLES : S'ABSTENIR
 */
public class Test {

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

        /*Log l = getLogs("logs-annote/");

         double interval01 = 0;
         double sum_interval01 = 0;

         double interval02 = 0;
         double sum_interval02 = 0;

         double interval03 = 0;
         double sum_interval03 = 0;

         double interval04 = 0;
         double sum_interval04 = 0;

         double interval05 = 0;
         double sum_interval05 = 0;

         double interval06 = 0;
         double sum_interval06 = 0;

         double interval07 = 0;
         double sum_interval07 = 0;

         double interval08 = 0;
         double sum_interval08 = 0;

         double interval09 = 0;
         double sum_interval09 = 0;

         double interval1 = 0;
         double sum_interval1 = 0;

         for (QuerySession qs : l.getSessions()) {
         for (int i = 0; i < qs.getQueries().size() - 1; i++) {
         double relativePosition = (double) i / (double) qs.getQueries().size();

         double olap = OLAPdifference(qs.getQueries().get(i), qs.getQueries().get(i + 1));

         if (relativePosition <= 0.1) {
         interval01 += olap;
         sum_interval01++;
         } else if (relativePosition <= 0.2) {
         interval02 += olap;
         sum_interval02++;
         } else if (relativePosition <= 0.3) {
         interval03 += olap;
         sum_interval03++;
         } else if (relativePosition <= 0.4) {
         interval04 += olap;
         sum_interval04++;
         } else if (relativePosition <= 0.5) {
         interval05 += olap;
         sum_interval05++;
         } else if (relativePosition <= 0.6) {
         interval06 += olap;
         sum_interval06++;
         } else if (relativePosition <= 0.7) {
         interval07 += olap;
         sum_interval07++;
         } else if (relativePosition <= 0.8) {
         interval08 += olap;
         sum_interval08++;
         } else if (relativePosition <= 0.9) {
         interval09 += olap;
         sum_interval09++;
         } else if (relativePosition <= 1) {
         interval1 += olap;
         sum_interval1++;
         }

         }
         }

         System.out.println((interval01 / sum_interval01) + ";" + (interval02 / sum_interval02) + ";" + (interval03 / sum_interval03) + ";" + (interval04 / sum_interval04) + ";" + (interval05 / sum_interval05) + ";" + (interval06 / sum_interval06) + ";" + (interval07 / sum_interval07) + ";" + (interval08 / sum_interval08) + ";" + (interval09 / sum_interval09) + ";" + (interval1 / sum_interval1) + ";");*/
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

        HashSet<QuerySession> question1 = new HashSet<>();
        HashSet<QuerySession> question4 = new HashSet<>();
        HashSet<QuerySession> question21 = new HashSet<>();
        HashSet<QuerySession> question36 = new HashSet<>();

        Log l2 = getLogs("logs/");

        for (QuerySession qs : l2.getSessions()) {
            if (qs.getIdQuestion() == 1) {
                question1.add(qs);
            } else if (qs.getIdQuestion() == 4) {
                question4.add(qs);
            } else if (qs.getIdQuestion() == 21) {
                question21.add(qs);
            } else if (qs.getIdQuestion() == 36) {
                question36.add(qs);
            }
        }

        double averageRecall = 0;
        double averagePrecision = 0;
        for (int i = 0; i < 100; i++) {
            double[] result = relevant(question1, question1_annote);
            averageRecall += result[0];
            averagePrecision += result[1];
        }
        System.out.println("Question 1 : " + averageRecall / 100.0 + " " + averagePrecision / 100.0);

        System.out.println();

        averageRecall = 0;
        averagePrecision = 0;
        for (int i = 0; i < 100; i++) {
            double[] result = relevant(question4, question4_annote);
            averageRecall += result[0];
            averagePrecision += result[1];
        }
        System.out.println("Question 4 : " + averageRecall / 100.0 + " " + averagePrecision / 100.0);

        System.out.println();
        averageRecall = 0;
        averagePrecision = 0;
        for (int i = 0; i < 100; i++) {
            double[] result = relevant(question21, question21_annote);
            averageRecall += result[0];
            averagePrecision += result[1];
        }
        System.out.println("Question 21 : " + averageRecall / 100.0 + " " + averagePrecision / 100.0);

        System.out.println();
        averageRecall = 0;
        averagePrecision = 0;
        for (int i = 0; i < 100; i++) {
            double[] result = relevant(question36, question36_annote);
            averageRecall += result[0];
            averagePrecision += result[1];
        }
        System.out.println("Question 36 : " + averageRecall / 100.0 + " " + averagePrecision / 100.0);

        System.out.println("AvgSize: " + AvgSize / 400.0);

        c.close();
    }

    private static double[] relevant(HashSet<QuerySession> question, HashSet<QuerySession> question_annote) {

        Iterator<QuerySession> it = question_annote.iterator();
        QuerySession qs = it.next();

        while (qs.getRelevantQueries().isEmpty()) {
            qs = it.next();
        }

        Qfset begin = qs.get(0);

        HashSet<QuerySession> log = new HashSet<>(question);
        log.addAll(question_annote);

        QuerySession monkey = generateMonkeySessionAdvancedOnlyLog(begin, log);

        /*double max = 0;
         for (QuerySession qsq : question) {
         max = Math.max(max, compareTwoSessions(qsq, monkey));
         }*/
        List<Qfset> relevantQueries = qs.getRelevantQueries();
        //System.out.println(relevantQueries.size());

        double nbIdenticalRelevantQueries = 0;

        //List<Qfset> uniqRelevantQueries = new ArrayList<>();

        /*for (Qfset relevant : relevantQueries) {
         boolean add = true;
         for (Qfset q : uniqRelevantQueries) {
         if (relevant.isEqual(q)) {
         System.out.println("OUI");
         add = false;
         break;
         }
         }
         if (add) {
         uniqRelevantQueries.add(relevant);
         }
         }*/
        List<Qfset> uniqRelevantMonkeyQueries = new ArrayList<>();

        for (Qfset relevant : monkey.getQueries()) {
            boolean add = true;
            for (Qfset q : uniqRelevantMonkeyQueries) {
                if (relevant.isEqual(q)) {
                    add = false;
                    break;
                }
            }
            if (add) {
                uniqRelevantMonkeyQueries.add(relevant);
            }
        }

        for (Qfset relevant : relevantQueries) {
            for (Qfset q : uniqRelevantMonkeyQueries) {
                if (relevant.isEqual(q)) {
                    nbIdenticalRelevantQueries++;
                }
            }
        }

        //System.out.println(monkey.size()+" "+doublon);
        double[] t = new double[]{nbIdenticalRelevantQueries / (double) relevantQueries.size(), nbIdenticalRelevantQueries / ((double) monkey.size())};

        return t;

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

    private static int OLAPdifference(Qfset q1, Qfset q2) {
        int nb = 0;
        boolean isPresent;

        for (MeasureFragment mf1 : q1.getMeasures()) {
            isPresent = false;
            for (MeasureFragment mf2 : q2.getMeasures()) {
                if (mf1.getAttribute() == mf2.getAttribute()) {
                    isPresent = true;
                }
            }

            if (!isPresent) {
                nb++;
            }
        }

        for (MeasureFragment mf1 : q2.getMeasures()) {
            isPresent = false;
            for (MeasureFragment mf2 : q1.getMeasures()) {
                if (mf1.getAttribute() == mf2.getAttribute()) {
                    isPresent = true;
                }
            }

            if (!isPresent) {
                nb++;
            }
        }

        for (SelectionFragment sf1 : q1.getSelectionPredicates()) {
            isPresent = false;
            for (SelectionFragment sf2 : q2.getSelectionPredicates()) {
                if (sf1.getLevel() == sf2.getLevel() && sf1.getValue() == sf2.getValue()) {
                    isPresent = true;
                }
            }
            if (!isPresent) {
                nb++;
            }
        }

        for (SelectionFragment sf1 : q2.getSelectionPredicates()) {
            isPresent = false;
            for (SelectionFragment sf2 : q1.getSelectionPredicates()) {
                if (sf1.getLevel() == sf2.getLevel()) {//for counting 1 time if a selection value has been changed (detected just before)
                    isPresent = true;
                }
            }
            if (!isPresent) {
                nb++;
            }
        }

        for (ProjectionFragment pf1 : q1.getAttributes()) {
            for (ProjectionFragment pf2 : q2.getAttributes()) {
                if (pf1.getLevel().getHierarchy() == pf2.getLevel().getHierarchy()) {
                    nb += Math.abs(pf1.getLevel().getDepth() - pf2.getLevel().getDepth());
                }
            }
        }

        return nb;
    }

    private static QuerySession generateMonkeySession(Qfset begin, HashSet<QuerySession> sessions) {

        QuerySession result = new QuerySession("Monkey");
        result.add(begin);
        boolean stop = false;

        int probability = 15;

        while (!stop) {
            QuerySession qsLog = sessions.iterator().next();

            Random r = new Random();
            int queryPosition = r.nextInt(qsLog.getQueries().size());
            Qfset queryLog = qsLog.getQueries().get(queryPosition);

            int olapDiff = OLAPdifference(begin, queryLog);
            //System.out.println(olapDiff+" "+result.size());

            if (olapDiff <= 3 && olapDiff != 0) {
                result.add(queryLog);
            }

            if (0 == r.nextInt(probability)) {
                stop = true;
            } else {
                probability--;
            }
        }

        AvgSize += result.size();

        return result;
    }

    private static QuerySession generateMonkeySessionAdvanced(Qfset begin, HashSet<QuerySession> sessions) {

        QuerySession result = new QuerySession("Monkey");
        result.add(begin);
        boolean stop = false;

        int probability = 15;

        while (!stop) {
            int size = result.size();

            Random r = new Random();

            int advanced = r.nextInt(2);

            if (advanced == 0) {
                ASRA recoSys = new ASRA(sessions, result.getQueries());
                QuerySession reco = null;

                try {
                    reco = recoSys.computeASRA();
                } catch (OutOfMemoryError | Exception e) {
                    reco = null;
                }

                if (reco != null && !reco.getQueries().isEmpty()) {
                    Qfset maxSimilarityQuery = null;
                    double maxValue = 0;
                    for (Qfset q : reco.getQueries()) {
                        QueryComparisonByJaccardAndStructureThresholdWithSeveralSelectionPerLevel comp = new QueryComparisonByJaccardAndStructureThresholdWithSeveralSelectionPerLevel(q, result.get(result.size() - 1), 0.35, 0.5, 0.15);

                        double sim = comp.computeSimilarity().getSimilarity();
                        if (sim >= maxValue) {
                            maxValue = sim;
                            maxSimilarityQuery = q;
                        }
                    }

                    result.add(maxSimilarityQuery);
                } else {
                    Log l = new Log(sessions);
                    HashSet<QuerySession> subsetLog = l.select(result.get(result.size() - 1));

                    if (!subsetLog.isEmpty()) {
                        QuerySession randomSession = subsetLog.iterator().next();
                        Qfset randomQuery = randomSession.get(r.nextInt(randomSession.size()));

                        result.add(randomQuery);
                    }
                }

            } else {
                Log l = new Log(sessions);
                HashSet<QuerySession> subsetLog = l.select(result.get(result.size() - 1));

                if (!subsetLog.isEmpty()) {
                    QuerySession randomSession = subsetLog.iterator().next();
                    Qfset randomQuery = randomSession.get(r.nextInt(randomSession.size()));

                    result.add(randomQuery);
                } else {
                    ASRA recoSys = new ASRA(sessions, result.getQueries());
                    QuerySession reco = null;
                    try {
                        reco = recoSys.computeASRA();
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
                }
            }

            if (result.size() == size) {
                stop = true;
            }

            if (0 == r.nextInt(probability)) {
                stop = true;
            } else {
                probability--;
            }
        }
        AvgSize += result.size();
        return result;
    }

    private static QuerySession generateMonkeySessionAdvancedOnlyLog(Qfset begin, HashSet<QuerySession> sessions) {

        QuerySession result = new QuerySession("Monkey");
        result.add(begin);
        boolean stop = false;

        int probability = 15;

        while (!stop) {
            int size = result.size();

            Random r = new Random();

            Log l = new Log(sessions);
            HashSet<QuerySession> subsetLog = l.select(result.get(result.size() - 1));

            if (!subsetLog.isEmpty()) {
                QuerySession randomSession = subsetLog.iterator().next();
                Qfset randomQuery = randomSession.get(r.nextInt(randomSession.size()));

                result.add(randomQuery);
            }

            if (result.size() == size) {
                stop = true;
            }

            if (0 == r.nextInt(probability)) {
                stop = true;
            } else {
                probability--;
            }
        }
        AvgSize += result.size();
        return result;
    }

}
