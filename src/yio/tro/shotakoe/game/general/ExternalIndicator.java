package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.Direction;
import yio.tro.shotakoe.stuff.DirectionWorker;
import yio.tro.shotakoe.stuff.PointYio;
import yio.tro.shotakoe.stuff.object_pool.ReusableYio;

public class ExternalIndicator implements ReusableYio {

    ExternalIndicatorsManager manager;
    public CircleYio position;
    public IndicatorType type;
    private Direction[] checkedDirections;
    PointYio launchPoint;


    public ExternalIndicator(ExternalIndicatorsManager manager) {
        this.manager = manager;
        position = new CircleYio();
        type = null;
        launchPoint = new PointYio();
        initCheckedDirections();
    }


    private void initCheckedDirections() {
        checkedDirections = new Direction[]{
                Direction.down,
                Direction.right,
                Direction.left,
                Direction.up,
        };
    }


    @Override
    public void reset() {
        position.reset();
    }


    public void setValues(IndicatorType indicatorType, Cell cell) {
        if (!cell.active) {
            System.out.println("ExternalIndicator.setValues: problem, cell is not active");
            return;
        }
        type = indicatorType;
        for (Direction direction : checkedDirections) {
            Cell adjacentCell = cell.getAdjacentCell(direction);
            if (adjacentCell.active) continue;
            position.setRadius(0.9f * cell.position.radius);
            Direction oppositeDirection = DirectionWorker.getOpposite(direction);
            double angle = DirectionWorker.getAngle(oppositeDirection);
            position.setAngle(angle);
            position.center.setBy(adjacentCell.position.center);
            position.center.relocateRadial(-0.05f * cell.position.radius, angle);
            launchPoint.setBy(position.center);
            break;
        }
    }


    public PointYio getLaunchPoint() {
        return launchPoint;
    }
}
