package yio.tro.shotakoe.game.general;

import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.object_pool.ReusableYio;

public class CornerPlug implements ReusableYio {

    public Cell cell;
    public RectangleYio position;


    public CornerPlug() {
        cell = null;
        position = new RectangleYio();
    }


    public void setCell(Cell cell) {
        this.cell = cell;
        updatePosition();
    }


    private void updatePosition() {
        position.width = 2 * cell.innerDelta;
        position.height = 2 * cell.innerDelta;
        position.x = cell.position.center.x - cell.position.radius - cell.innerDelta;
        position.y = cell.position.center.y - cell.position.radius - cell.innerDelta;
    }


    @Override
    public void reset() {
        cell = null;
        position.reset();
    }
}
