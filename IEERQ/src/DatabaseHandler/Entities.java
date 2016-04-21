/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseHandler;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

/**
 *
 * @author Ahmed
 */
public enum Entities implements Label, RelationshipType{
    Entity,Category,Predicate;
}
