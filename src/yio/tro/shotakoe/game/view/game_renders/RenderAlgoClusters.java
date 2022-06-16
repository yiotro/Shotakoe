package yio.tro.shotakoe.game.view.game_renders;

import yio.tro.shotakoe.game.debug.DebugFlags;
import yio.tro.shotakoe.game.general.AlgoCluster;
import yio.tro.shotakoe.game.general.Cell;
import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.PointYio;

public class RenderAlgoClusters extends GameRender{

    CircleYio circleYio;


    public RenderAlgoClusters() {
        circleYio = new CircleYio();
    }


    @Override
    protected void loadTextures() {

    }


    @Override
    public void render() {
        if (!DebugFlags.showClusters) return;
        for (AlgoCluster cluster : getObjectsLayer().aiManager.algoClusters) {
            circleYio.center.setBy(cluster.geometricCenter);
            circleYio.setRadius(4 * GraphicsYio.borderThickness);
            GraphicsYio.drawByCircle(batchMovable, getBlackPixel(), circleYio);
            for (AlgoCluster adjacentCluster : cluster.adjacentClusters) {
                if (adjacentCluster.geometricCenter.x > cluster.geometricCenter.x) continue;
                GraphicsYio.drawLine(
                        batchMovable,
                        getBlackPixel(),
                        cluster.geometricCenter,
                        adjacentCluster.geometricCenter,
                        2 * GraphicsYio.borderThickness
                );
            }
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
