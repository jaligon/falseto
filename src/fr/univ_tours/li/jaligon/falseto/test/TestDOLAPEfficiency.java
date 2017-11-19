/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_tours.li.jaligon.falseto.test;

import fr.univ_tours.li.jaligon.falseto.Generics.Connection;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.Log;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.Qfset;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.QuerySession;
import fr.univ_tours.li.jaligon.falseto.Recommendation.ASRA;
import fr.univ_tours.li.jaligon.falseto.Summary.Intention.Clustering.Cluster;
import fr.univ_tours.li.jaligon.falseto.Summary.Intention.Clustering.HierarchicalClustering;
import fr.univ_tours.li.jaligon.falseto.Summary.Intention.Clustering.SummarizationHierarchicalTree;
import fr.univ_tours.li.jaligon.falseto.summaryBrowsingDesign.SummaryGUI;

/**
 *
 * @author julien
 */
public class TestDOLAPEfficiency {

    static double avgTimeClustering = 0;
    static double avgTimeSearchLog = 0;
    static double avgTimeReco = 0;

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);

        Connection c = new Connection();//connection to a database (based on the olapConnection.properties file), works only with an Oracle database for now.
        c.open();

        XmlLogParsing parsing = new XmlLogParsing("Test Evaluation/WorkloadSM.xml");//parse a log file //Workload9_fq.xml EXP2
        //XmlLogParsing parsing = new XmlLogParsing("Workload_newSZ.xml");//parse a log file //Workload9_fq.xml EXP2
        List<QuerySession> sessionList = parsing.readSessionListLogWithStAX();//a log is a set of sessions
        System.out.println("Log Size: "+sessionList.size());
        
        int nbQueries = 0;
        for(QuerySession q:sessionList)
        {
        nbQueries+=q.getQueries().size();
        }
        
        System.out.println(nbQueries);
        
        double nbIteration = 10;

        for (int i = 0; i < nbIteration; i++) {
            long seed = 101;
            Collections.shuffle(sessionList, new Random(seed));
            List<QuerySession> sessionList2 = sessionList.subList(0, 200);

            HashSet<QuerySession> sessionSet = new HashSet<>();
            for (QuerySession qs : sessionList2) {
                sessionSet.add(qs);
            }

            Log l = new Log(sessionSet);

            //clustering
            long start = System.currentTimeMillis();
            hierarchicalClustering(l);
            long end = System.currentTimeMillis();

            avgTimeClustering += (end - start);

            //search log
            QuerySession randomSession = sessionSet.iterator().next();
            Random r = new Random();
            Qfset randomQuery = randomSession.get(r.nextInt(randomSession.size()));

            start = System.currentTimeMillis();
            l.select(randomQuery);
            end = System.currentTimeMillis();

            avgTimeSearchLog += (end - start);

            //reco
            start = System.currentTimeMillis();
            sessionGenerationRecommendationProposal2(sessionSet, 0.25);
            end = System.currentTimeMillis();

            avgTimeReco += (end - start);
        }

        System.out.println(avgTimeClustering / nbIteration + " " + avgTimeSearchLog / nbIteration + " " + avgTimeReco / nbIteration);

        c.close();
    }

    private static Cluster hierarchicalClustering(Log l) {

        if (l.getSessions().size() != 1) {
            HierarchicalClustering hc = null;

            try {
                hc = new HierarchicalClustering(l.getSessions());
            } catch (Exception ex) {
                Logger.getLogger(SummaryGUI.class.getName()).log(Level.SEVERE, null, ex);
            }

            final SummarizationHierarchicalTree a = new SummarizationHierarchicalTree(hc.getGraph(), l);
            a.defineCluster();
            try {
                a.defineSummaries();
            } catch (IOException ex) {
                Logger.getLogger(SummaryGUI.class.getName()).log(Level.SEVERE, null, ex);
            }

            return a.getRoot();
        } else {
            Cluster c = new Cluster();
            c.setLog(l);
            c.getSummaries().get(0);

            return c;
        }

    }

    private static QuerySession sessionGenerationRecommendationProposal2(HashSet<QuerySession> log, double cutPercentage) {
        HashSet<QuerySession> logWithoutRandomSession = new HashSet<>(log);

        QuerySession result = new QuerySession("synthetic");

        Iterator<QuerySession> it = log.iterator();
        QuerySession randomSession = it.next();
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
        }

        return result;
    }

}
