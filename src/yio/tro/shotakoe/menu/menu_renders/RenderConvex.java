package yio.tro.shotakoe.menu.menu_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.Masking;
import yio.tro.shotakoe.stuff.RectangleYio;

public class RenderConvex extends RenderInterfaceElement{

    private RectangleYio mskBottomPos;
    private RectangleYio mskTopPos;
    private TextureRegion bottomMiddleTexture;
    private TextureRegion topMiddleTexture;
    private RectangleYio topMiddlePosition;
    private RectangleYio bottomMiddlePosition;
    private CircleYio[] corners;
    private TextureRegion bottomLeftTexture;
    private TextureRegion bottomRightTexture;
    private TextureRegion topLeftTexture;
    private TextureRegion topRightTexture;
    private float cr;
    private RectangleYio position;


    public RenderConvex() {
        mskBottomPos = new RectangleYio();
        mskTopPos = new RectangleYio();
        topMiddlePosition = new RectangleYio();
        bottomMiddlePosition = new RectangleYio();
        corners = new CircleYio[4];
        for (int i = 0; i < corners.length; i++) {
            corners[i] = new CircleYio();
        }
    }


    @Override
    public void loadTextures() {
        bottomMiddleTexture = loadTexture("bottom_side", false);
        topMiddleTexture = loadTexture("top_side", false);
        bottomLeftTexture = loadTexture("bottom_left", false);
        bottomRightTexture = loadTexture("bottom_right", false);
        topLeftTexture = loadTexture("top_left", false);
        topRightTexture = loadTexture("top_right", false);
    }


    private TextureRegion loadTexture(String name, boolean antialias) {
        return GraphicsYio.loadTextureRegion("menu/convex/" + name + ".png", antialias);
    }


    public void renderConvex(RectangleYio position) {
        renderConvex(position, GraphicsYio.defCornerRadius);
    }


    public void renderConvex(RectangleYio position, float cornerRadius) {
        renderConvex(batch, position, cornerRadius);
    }


    public void renderConvex(SpriteBatch argBatch, RectangleYio position, float cornerRadius) {
        cr = cornerRadius / 2;
        this.position = position;
        updateMiddlePositions();
        updateCorners();

        GraphicsYio.drawByRectangle(argBatch, bottomMiddleTexture, bottomMiddlePosition);
        GraphicsYio.drawByRectangle(argBatch, topMiddleTexture, topMiddlePosition);
        GraphicsYio.drawByCircle(argBatch, bottomLeftTexture, corners[0]);
        GraphicsYio.drawByCircle(argBatch, topLeftTexture, corners[1]);
        GraphicsYio.drawByCircle(argBatch, topRightTexture, corners[2]);
        GraphicsYio.drawByCircle(argBatch, bottomRightTexture, corners[3]);
    }


    private void updateCorners() {
        corners[0]
                .setRadius(cr)
                .setAngle(0)
                .center.set(position.x + cr, position.y + cr);

        corners[1]
                .setRadius(cr)
                .setAngle(0)
                .center.set(position.x + cr, position.y + position.height - cr);

        corners[2]
                .setRadius(cr)
                .setAngle(0)
                .center.set(position.x + position.width - cr, position.y + position.height - cr);

        corners[3]
                .setRadius(cr)
                .setAngle(0)
                .center.set(position.x + position.width - cr, position.y + cr);

    }


    private void updateMiddlePositions() {
        bottomMiddlePosition.x = position.x + 2 * cr;
        bottomMiddlePosition.y = position.y;
        bottomMiddlePosition.width = position.width - 4 * cr;
        bottomMiddlePosition.height = 2 * cr;
        topMiddlePosition.setBy(bottomMiddlePosition);
        topMiddlePosition.y = position.y + position.height - topMiddlePosition.height;
    }


    private void renderWithMasking(RectangleYio position, float cornerRadius) {
        // this should only be used for debug
        mskBottomPos.setBy(position);
        mskBottomPos.height = cornerRadius;
        mskTopPos.setBy(mskBottomPos);
        mskTopPos.y = position.y + position.height - mskTopPos.height;
        batch.end();
        Masking.begin();
        prepareShapeRenderer();
        drawRoundRectShape(position, cornerRadius);
        shapeRenderer.end();
        batch.begin();
        Masking.continueAfterBatchBegin();
        GraphicsYio.drawByRectangle(batch, bottomMiddleTexture, mskBottomPos);
        GraphicsYio.drawByRectangle(batch, topMiddleTexture, mskTopPos);
        Masking.end(batch);
    }


    @Override
    public void render(InterfaceElement element) {

    }
}
