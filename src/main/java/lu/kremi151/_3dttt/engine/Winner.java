/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.engine;

/**
 *
 * @author michm
 */
public class Winner {
    
    public final Position p1, p2, p3, p4;
    public final GameHolder.Type type;
    
    public Winner(Position p1, Position p2, Position p3, Position p4, GameHolder.Type type){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.type = type;
    }
}
