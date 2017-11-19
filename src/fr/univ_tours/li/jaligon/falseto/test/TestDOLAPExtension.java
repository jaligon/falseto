/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_tours.li.jaligon.falseto.test;

import fr.univ_tours.li.jaligon.falseto.Generics.Connection;
import fr.univ_tours.li.jaligon.falseto.Generics.Generics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import mondrian.olap.Hierarchy;
import mondrian.olap.Member;
import mondrian.olap.SchemaReader;
import static fr.univ_tours.li.jaligon.falseto.Generics.MondrianObject.getLevel;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.Log;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.Qfset;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.QuerySession;
import fr.univ_tours.li.jaligon.falseto.Recommendation.ASRA;
import fr.univ_tours.li.jaligon.falseto.Similarity.Query.QueryComparisonByJaccardAndStructureThresholdWithSeveralSelectionPerLevel;
import fr.univ_tours.li.jaligon.falseto.Similarity.Session.CalculateGap;
import fr.univ_tours.li.jaligon.falseto.Similarity.Session.Matrix;
import fr.univ_tours.li.jaligon.falseto.Similarity.Session.SmithWaterman;
import fr.univ_tours.li.jaligon.falseto.Summary.Intention.Clustering.Cluster;
import fr.univ_tours.li.jaligon.falseto.Summary.Intention.Clustering.HierarchicalClustering;
import fr.univ_tours.li.jaligon.falseto.Summary.Intention.Clustering.SummarizationHierarchicalTree;
import fr.univ_tours.li.jaligon.falseto.summaryBrowsingDesign.SummaryGUI;

/**
 *
 * @author julien
 */
public class TestDOLAPExtension {

    public static void main(String args[]) throws IOException, Exception {

        Locale.setDefault(Locale.US);

        Connection c = new Connection();//connection to a database (based on the olapConnection.properties file), works only with an Oracle database for now.
        c.open();

        XmlLogParsing parsing = new XmlLogParsing("Test Evaluation/WorkloadSM.xml");//parse a log file //Workload9_fq.xml EXP2
        //XmlLogParsing parsing = new XmlLogParsing("Workload_newSZ.xml");//parse a log file //Workload9_fq.xml EXP2
        List<QuerySession> sessionList = parsing.readSessionListLogWithStAX();//a log is a set of sessions
        //System.out.println("Log Size: " + sessionList.size());
        sessionList = sessionList.subList(0, 50);

        HashSet<QuerySession> sessionSet = new HashSet<>();
        for (int i=0;i<sessionList.size();i++) {
            sessionSet.add(sessionList.get(i));
        }

        Log l = new Log(sessionSet);

        for (int i = 0; i < 1000; i++) {

            Qfset queryForFiltering = generatedFilterQuery();
            //System.out.println(queryForFiltering);
            
            int mb = 1024 * 1024; 
            long start = System.currentTimeMillis();
            HashSet<QuerySession> filteredSessions = l.select(queryForFiltering);
            if(filteredSessions.size() == sessionSet.size() || filteredSessions.isEmpty() || filteredSessions.size() == 1)
            {
                i--;
                continue;
            }
            //System.out.println(filteredSessions.size());
            
            Log filteredLog = new Log(filteredSessions);
            //long memStart = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            Cluster cl = hierarchicalClustering(filteredLog);
            long end = System.currentTimeMillis();
            //long memEnd = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            
            //long memory = memStart- memEnd;
            
            double ratio = (double) filteredSessions.size() / (double) sessionList.size();
            double time = end - start;
            
            System.out.println(ratio + ";"+time);
        }

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

    private static Qfset generatedFilterQuery() {
        Qfset q = new Qfset();
        SchemaReader schema = fr.univ_tours.li.jaligon.falseto.Generics.Connection.getCube().getSchema().getSchemaReader();
        //measure
        mondrian.olap.Level levelMeasure = getLevel(Generics.MEASURES_LEVEL, Generics.MEASURES_DIMENSION);
        List<Member> members = schema.withLocus().getLevelMembers(levelMeasure, true);

        Random r = new Random();

        //measure
        Member measure = members.get(r.nextInt(members.size()));

        //attribute
        HashSet<Hierarchy> hierarchies = fr.univ_tours.li.jaligon.falseto.Generics.Generics.getHierarchies();
        Hierarchy h = hierarchies.iterator().next();
        mondrian.olap.Level[] levels = h.getLevels();
        mondrian.olap.Level attribute = levels[r.nextInt(levels.length - 1) + 1];

        //selection
        List<Member> selections = schema.withLocus().getLevelMembers(attribute, true);
        Member selection = selections.get(r.nextInt(selections.size()-1)+1);

        //q.addMeasure(measure);
        q.addProjection(attribute);
        //q.addSelection(selection);

        return q;
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

                if (closestQuery != null) {
                    result.add(closestQuery);
                }
            }

            if (result.size() == size || 0 == r.nextInt(probability)) {
                stop = true;
            } else {
                probability--;
            }
        }

        return result;
    }

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

        } else {

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

        }

        return result;
    }
}
