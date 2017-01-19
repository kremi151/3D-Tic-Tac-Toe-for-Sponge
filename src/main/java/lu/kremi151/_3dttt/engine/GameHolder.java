/*
 * Michel Kremer
 */
package lu.kremi151._3dttt.engine;

import java.util.Optional;
import java.util.Random;

/**
 *
 * @author michm
 */
public final class GameHolder {

    private final Type cube[][][] = new Type[4][4][4];
    private Position lastPosition = null;
    
    public GameHolder(){
        reset();
    }

    public void reset() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    cube[i][j][k] = Type.FREE;
                }
            }
        }
    }

    public void set(int x, int y, int z, Type type) {
        cube[x][y][z] = type;
        lastPosition = new Position(x,y,z);
    }

    public Type get(Position pos) {
        return cube[pos.x][pos.y][pos.z];
    }
    
    public Type get(int x, int y, int z){
        return cube[x][y][z];
    }
    
    public Optional<Position> getLastPosition(){
        return Optional.ofNullable(lastPosition);
    }
    
    private boolean areEqual(Position p1, Position p2, Position p3, Position p4){
        return (get(p1) == get(p2)) && (get(p2) == get(p3)) && (get(p3) == get(p4)) && get(p1) != Type.FREE;
    }

    public Optional<Winner> checkForWinner() {
        Optional<Winner> ow = checkForWinnerPlanar((x, y, z) -> new Position(x, y, z));
        if (ow.isPresent()) {
            return ow;
        }
        ow = checkForWinnerPlanar((x, y, z) -> new Position(y, z, x));
        if (ow.isPresent()) {
            return ow;
        }
        ow = checkForWinnerPlanar((x, y, z) -> new Position(z, x, y));//?
        if (ow.isPresent()) {
            return ow;
        }

        for (int x = 0; x < 4; x++) {
            Position p1, p2, p3, p4;
            p1 = new Position(x, 0, 0);
            p2 = new Position(x, 1, 1);
            p3 = new Position(x, 2, 2);
            p4 = new Position(x, 3, 3);
            if (areEqual(p1, p2, p3, p4)) {
                return Optional.of(new Winner(p1, p2, p3, p4, get(p1)));
            }
            p1 = new Position(x, 0, 3);
            p2 = new Position(x, 1, 2);
            p3 = new Position(x, 2, 1);
            p4 = new Position(x, 3, 0);
            if (areEqual(p1, p2, p3, p4)) {
                return Optional.of(new Winner(p1, p2, p3, p4, get(p1)));
            }
        }
        for (int y = 0; y < 4; y++) {
            Position p1, p2, p3, p4;
            p1 = new Position(0, y, 0);
            p2 = new Position(1, y, 1);
            p3 = new Position(2, y, 2);
            p4 = new Position(3, y, 3);
            if (areEqual(p1, p2, p3, p4)) {
                return Optional.of(new Winner(p1, p2, p3, p4, get(p1)));
            }
            p1 = new Position(0, y, 3);
            p2 = new Position(1, y, 2);
            p3 = new Position(2, y, 1);
            p4 = new Position(3, y, 0);
            if (areEqual(p1, p2, p3, p4)) {
                return Optional.of(new Winner(p1, p2, p3, p4, get(p1)));
            }
        }
        Position p1, p2, p3, p4;
        p1 = new Position(0, 0, 0);
        p2 = new Position(1, 1, 1);
        p3 = new Position(2, 2, 2);
        p4 = new Position(3, 3, 3);
        if (areEqual(p1, p2, p3, p4)) {
            return Optional.of(new Winner(p1, p2, p3, p4, get(p1)));
        }
        p1 = new Position(3, 0, 0);
        p2 = new Position(2, 1, 1);
        p3 = new Position(1, 2, 2);
        p4 = new Position(0, 3, 3);
        if (areEqual(p1, p2, p3, p4)) {
            return Optional.of(new Winner(p1, p2, p3, p4, get(p1)));
        }
        p1 = new Position(0, 3, 0);
        p2 = new Position(1, 2, 1);
        p3 = new Position(2, 1, 2);
        p4 = new Position(3, 0, 3);
        if (areEqual(p1, p2, p3, p4)) {
            return Optional.of(new Winner(p1, p2, p3, p4, get(p1)));
        }
        p1 = new Position(3, 3, 0);
        p2 = new Position(2, 2, 1);
        p3 = new Position(1, 1, 2);
        p4 = new Position(0, 0, 3);
        if (areEqual(p1, p2, p3, p4)) {
            return Optional.of(new Winner(p1, p2, p3, p4, get(p1)));
        }
        return Optional.empty();
    }

    private Optional<Winner> checkForWinnerPlanar(CubeAccessor accessor) {
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 4; x++) {
                Position p1, p2, p3, p4;
                p1 = accessor.transform(x, 0, z);
                p2 = accessor.transform(x, 1, z);
                p3 = accessor.transform(x, 2, z);
                p4 = accessor.transform(x, 3, z);
                if (areEqual(p1, p2, p3, p4)) {
                    return Optional.of(new Winner(p1, p2, p3, p4, get(p1)));
                }
            }
            for (int y = 0; y < 4; y++) {
                Position p1, p2, p3, p4;
                p1 = accessor.transform(0, y, z);
                p2 = accessor.transform(1, y, z);
                p3 = accessor.transform(2, y, z);
                p4 = accessor.transform(3, y, z);
                if (areEqual(p1, p2, p3, p4)) {
                    return Optional.of(new Winner(p1, p2, p3, p4, get(p1)));
                }
            }
            Position p1, p2, p3, p4;
            p1 = accessor.transform(0, 0, z);
            p2 = accessor.transform(1, 1, z);
            p3 = accessor.transform(2, 2, z);
            p4 = accessor.transform(3, 3, z);
            if (areEqual(p1, p2, p3, p4)) {
                return Optional.of(new Winner(p1, p2, p3, p4, get(p1)));
            }
            p1 = accessor.transform(3, 0, z);
            p2 = accessor.transform(2, 1, z);
            p3 = accessor.transform(1, 2, z);
            p4 = accessor.transform(0, 3, z);
            if (areEqual(p1, p2, p3, p4)) {
                return Optional.of(new Winner(p1, p2, p3, p4, get(p1)));
            }
        }
        return Optional.empty();
    }

    public static enum Type {
        CIRCLE('O'),
        CROSS('X'),
        FREE('■');
        
        private char symbol;
        
        private Type(char symbol){
            this.symbol = symbol;
        }
        
        public char getSymbol(){
            return symbol;
        }
        
        public Type getOpposite(){
            switch(this){
                case CIRCLE: return CROSS;
                case CROSS: return CIRCLE;
                default: throw new RuntimeException("Cannot get opposite of FREE");
            }
        }
        
        public static Type randomPlayable(Random rand){
            return (rand.nextInt(2) == 0)?CIRCLE:CROSS;
        }
    }

    private interface CubeAccessor {

        Position transform(int x, int y, int z);
    }
}
