package yio.tro.shotakoe.menu.elements.button;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import yio.tro.shotakoe.stuff.RectangleYio;

public abstract class AbstractCacheRender {

    public RectangleYio framePosition;


    public AbstractCacheRender() {
        framePosition = new RectangleYio();
        loadTextures();
    }


    public abstract void loadTextures();


    public abstract void setTarget(Object object);


    protected abstract void render(SpriteBatch batch);


    public void perform(SpriteBatch batch, float usedY) {
        batch.begin();
        framePosition.y = usedY;
        render(batch);
        batch.end();
    }

}
