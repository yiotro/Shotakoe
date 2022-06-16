package yio.tro.shotakoe.stuff;

import yio.tro.shotakoe.YioGdxGame;

public class DirectionWorker {


    public static Direction getRandomDirection() {
        Direction[] values = Direction.values();
        int index = YioGdxGame.random.nextInt(values.length);
        return values[index];
    }


    public static Direction rotate(Direction direction, int delta) {
        int ordinal = direction.ordinal();
        ordinal += delta;
        int length = Direction.values().length;
        while (ordinal < 0) {
            ordinal += length;
        }
        while (ordinal > length - 1) {
            ordinal -= length;
        }
        return Direction.values()[ordinal];
    }


    public static Direction getOpposite(Direction direction) {
        switch (direction) {
            default:
                System.out.println("DirectionWorker.getOpposite: problem");
                return null;
            case up:
                return Direction.down;
            case right:
                return Direction.left;
            case down:
                return Direction.up;
            case left:
                return Direction.right;
        }
    }


    public static double getAngle(Direction direction) {
        switch (direction) {
            default:
                System.out.println("DirectionWorker.getAngle: problem");
                return 0;
            case up:
                return Math.PI / 2;
            case right:
                return 0;
            case down:
                return 1.5 * Math.PI;
            case left:
                return Math.PI;
        }
    }

}
