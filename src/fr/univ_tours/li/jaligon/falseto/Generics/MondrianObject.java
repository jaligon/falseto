/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univ_tours.li.jaligon.falseto.Generics;

import java.io.IOException;
import java.util.List;
import mondrian.olap.Cube;
import mondrian.olap.Dimension;
import mondrian.olap.Hierarchy;
import mondrian.olap.Level;
import mondrian.olap.Member;
import mondrian.olap.SchemaReader;

/**
 *
 * @author Julien
 */
public class MondrianObject {

    static private Cube cube = fr.univ_tours.li.jaligon.falseto.Generics.Connection.getCube();
    static private SchemaReader schema = fr.univ_tours.li.jaligon.falseto.Generics.Connection.getCube().getSchema().getSchemaReader();

    public MondrianObject() throws IOException {
    }

    /**
     * This method reads the mondrian schema and returns the mondrian level
     * corresponding to the input attribute
     *
     * @param String attributeName: the name of the attribute we want to
     * retrieve
     * @param String hierarchyName: the name of the hierarchy which the
     * attribute belongs to
     * @return the mondrian level corresponding to the attribute we want to
     * retrieve
     */
    public static Level getLevel(String attributeName, String hierarchyName) {
        for (Hierarchy h : fr.univ_tours.li.jaligon.falseto.Generics.Generics.getHierarchies()) {
            if (h.getName().trim().toUpperCase().equals(hierarchyName.trim().toUpperCase())) {
                for (Level level : h.getLevels()) {
                    if (level.getName().trim().toUpperCase().equals(attributeName.trim().toUpperCase())) {
                        return level;
                    }
                }
                break;
            }
        }
        
        return null;
    }

    /**
     * This method reads the mondrian schema and returns the mondrian member
     * corresponding to the input measure
     *
     * @param String measureName: the name of the measure we want to retrieve
     * @return the member corresponding to the measure we want to retrieve
     */
    public static Member getMeasure(String measureName) {
        Level l = getLevel(Generics.MEASURES_LEVEL, Generics.MEASURES_DIMENSION);
        List<Member> members = schema.withLocus().getLevelMembers(l, true);
        for (Member member : members) {
            
            if (member.getName().trim().toUpperCase().equals(measureName.trim().toUpperCase())) {
                return member;
            }
        }

        return null;
    }

    public static Member getSelection(String attributeName, String hierarchyName, String value) {

        Level l = getLevel(attributeName, hierarchyName);
        List<Member> members = schema.withLocus().getLevelMembers(l, true);
        
        for (Member member : members) {
            /*if (member.getName().contains("2+")) {
                System.out.println(member);
            }*/
            if (member.getName().trim().toUpperCase().equals(value.trim().toUpperCase())) {
                return member;
            }
        }
        return null;
    }
}
