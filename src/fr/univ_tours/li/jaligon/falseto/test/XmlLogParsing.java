package fr.univ_tours.li.jaligon.falseto.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.*;
import mondrian.olap.Level;
import fr.univ_tours.li.jaligon.falseto.Generics.MondrianObject;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.Qfset;
import fr.univ_tours.li.jaligon.falseto.QueryStructure.QuerySession;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

/**
 * Loads the log sessions from the XML file generated
 * by the OLAP Sessions Generator
 * 
 * @author enrico.gallinucci2
 */
public class XmlLogParsing {
    
    private String filePath;
    
    public XmlLogParsing(String filePath) throws IOException {
        this.filePath = filePath;
    }
    
    public List<QuerySession> readSessionListLog() {
        //XML parameters
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xPath = xpf.newXPath();
        InputSource inputSource = new InputSource(filePath);
        String sessionPath = "/Benchmark/Session";
        
        //Reading variables
        List<QuerySession> sessions = new ArrayList<QuerySession>();
        String hierarchyName, attributeName, measureName, value;
        Node sessionNode, queryNode, groupByNode, measureNode, selectionPredicateNode;
        
        try{
            NodeList sessionNodes = (NodeList) xPath.evaluate(sessionPath, inputSource, XPathConstants.NODESET);
            for (int i = 0; i < sessionNodes.getLength(); ++i)
            {
                QuerySession session = new QuerySession(Integer.toString(i));
                sessionNode = sessionNodes.item(i);

                NodeList queryNodes = (NodeList) xPath.evaluate("./Query", sessionNode, XPathConstants.NODESET);
                for (int k = 0; k < queryNodes.getLength(); ++k)
                {
                    Qfset query = new Qfset();
                    queryNode = queryNodes.item(k);

                    NodeList groupByElements = (NodeList) xPath.evaluate("./GroupBy/Element", queryNode, XPathConstants.NODESET);
                    for (int j = 0; j < groupByElements.getLength(); ++j)
                    {
                        groupByNode = groupByElements.item(j);
                        hierarchyName = xPath.evaluate("./Hierarchy/@value", groupByNode);
                        attributeName = xPath.evaluate("./Level/@value", groupByNode);

                        Level l = MondrianObject.getLevel(attributeName, hierarchyName);           
                        query.addProjection(l);
                    }

                    NodeList measureElements = (NodeList) xPath.evaluate("./Measures/Element", queryNode, XPathConstants.NODESET);
                    for (int j = 0; j < measureElements.getLength(); ++j)
                    {
                        measureNode = measureElements.item(j);
                        measureName = xPath.evaluate("./@value", measureNode);

                        query.addMeasure(MondrianObject.getMeasure(measureName));
                    }

                    NodeList selectionPredicateElements = (NodeList) xPath.evaluate("./SelectionPredicates/Element", queryNode, XPathConstants.NODESET);
                    for (int j = 0; j < selectionPredicateElements.getLength(); ++j)
                    {
                        selectionPredicateNode = selectionPredicateElements.item(j);
                        hierarchyName = xPath.evaluate("./Hierarchy/@value", selectionPredicateNode);
                        attributeName = xPath.evaluate("./Level/@value", selectionPredicateNode);
                        value = xPath.evaluate("./Predicate/@value", selectionPredicateNode);

                        query.addSelection(MondrianObject.getSelection(attributeName, hierarchyName, value));
                    }

                    session.add(query);
                }

                sessions.add(new QuerySession(session));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
            
        return sessions;
    }
    
    
    private enum CurrentElement { GROUPBY, MEASURE, SELECTIONPREDICATE }
    
    public List<QuerySession> readSessionListLogWithStAX() {
        List<QuerySession> sessions = new ArrayList<QuerySession>();
        
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(filePath);
            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);

            int sessionId = 0;
            QuerySession session = null;
            Qfset query = null;
            CurrentElement el = null;
            String hierarchyName, attributeName, value;
            boolean keepSession = true;

            while (streamReader.hasNext()) {
                if (streamReader.isStartElement()) {
                    switch (streamReader.getLocalName()) {
                        case "Session": {
                            session = new QuerySession(Integer.toString(sessionId++));
                            session.setTemplate(streamReader.getAttributeValue(2));
                            switch(session.getTemplate()){
                                case "Explorative":
                                    keepSession = true;
                                    break;
                                case "Slice and Drill":
                                    keepSession = true;
                                    break;
                                case "Just Slice":
                                case "Slice All":
                                    keepSession = true;
                                    break;
                                case "Goal Oriented":
                                    keepSession = true;
                                    break;
                            }
                            break;
                        }
                        case "Query": {
                            query = new Qfset();
                            break;
                        }
                        case "GroupBy": {
                            el = CurrentElement.GROUPBY;
                            break;
                        }
                        case "Measures" : {
                            el = CurrentElement.MEASURE;
                            break;
                        }
                        case "SelectionPredicates" : {
                            el = CurrentElement.SELECTIONPREDICATE;
                            break;
                        }
                        case "Element" : {
                            if(el.equals(CurrentElement.GROUPBY)){
                                streamReader.nextTag(); hierarchyName = streamReader.getAttributeValue(0);
                                streamReader.nextTag(); streamReader.nextTag(); attributeName = streamReader.getAttributeValue(0);
                                query.addProjection(MondrianObject.getLevel(attributeName, hierarchyName));
                            }
                            else if(el.equals(CurrentElement.MEASURE)){
                                query.addMeasure(MondrianObject.getMeasure(streamReader.getAttributeValue(0)));
                            }
                            else if(el.equals(CurrentElement.SELECTIONPREDICATE)){
                                streamReader.nextTag(); hierarchyName = streamReader.getAttributeValue(0);
                                streamReader.nextTag(); streamReader.nextTag(); attributeName = streamReader.getAttributeValue(0);
                                streamReader.nextTag(); streamReader.nextTag(); value = streamReader.getAttributeValue(0);
                                query.addSelection(MondrianObject.getSelection(attributeName, hierarchyName, value));
                            }
                            break;
                        }
                    }
                }
                else if (streamReader.isEndElement()) {
                    switch (streamReader.getLocalName()) {
                        case "Session": {
                            if(keepSession) sessions.add(new QuerySession(session));
                            break;
                        }
                        case "Query": {
                            session.add(query);
                            break;
                        }
                    }
                }
                streamReader.next();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return sessions;
    }
    
}
