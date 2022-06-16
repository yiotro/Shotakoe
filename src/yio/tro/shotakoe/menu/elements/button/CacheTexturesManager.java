package yio.tro.shotakoe.menu.elements.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.menu.elements.LabelElement;
import yio.tro.shotakoe.menu.elements.keyboard.CbPage;
import yio.tro.shotakoe.stuff.FrameBufferYio;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RectangleYio;
import yio.tro.shotakoe.stuff.object_pool.ObjectPoolYio;

public class CacheTexturesManager {


    MenuControllerYio menuControllerYio;
    public FrameBuffer frameBuffer;
    SpriteBatch batch;
    int usedY;
    private AbstractCacheRender currentRenderer;
    ObjectPoolYio<FrameBufferYio> poolFrameBuffers;
    private CrButtonTextRender crButtonTextRender;
    private CrLabelElement crLabelElement;
    private CrKeyboard crKeyboard;


    public CacheTexturesManager(MenuControllerYio menuControllerYio) {
        this.menuControllerYio = menuControllerYio;
        batch = new SpriteBatch();
        frameBuffer = null;
        usedY = 0;
        currentRenderer = null;
        initPools();
        initRenders();
    }


    private void initPools() {
        poolFrameBuffers = new ObjectPoolYio<FrameBufferYio>() {
            @Override
            public FrameBufferYio makeNewObject() {
                return FrameBufferYio.getInstance(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
            }
        };
        for (int k = 0; k < 10; k++) {
            FrameBufferYio next = poolFrameBuffers.getNext();
            poolFrameBuffers.add(next);
        }
    }


    private void initRenders() {
        crButtonTextRender = new CrButtonTextRender();
        crLabelElement = new CrLabelElement();
        crKeyboard = new CrKeyboard();
    }


    public TextureRegion perform(ButtonYio buttonYio) {
        setCurrentRenderer(crButtonTextRender, buttonYio);
        return performMainPart();
    }


    public TextureRegion perform(LabelElement labelElement) {
        setCurrentRenderer(crLabelElement, labelElement);
        return performMainPart();
    }


    public TextureRegion perform(CbPage page) {
        setCurrentRenderer(crKeyboard, page);
        return performMainPart();
    }


    public void setCurrentRenderer(AbstractCacheRender currentRenderer, Object target) {
        this.currentRenderer = currentRenderer;
        currentRenderer.setTarget(target);
    }


    private TextureRegion performMainPart() {
        prepareFrameBuffer();
        currentRenderer.perform(batch, usedY);
        TextureRegion result = extractTexture(currentRenderer.framePosition);
        usedY += currentRenderer.framePosition.height + 2 * GraphicsYio.borderThickness;
        return result;
    }


    private void prepareFrameBuffer() {
        if (frameBuffer != null && !needToCreateNewFrameBuffer()) {
            frameBuffer.begin();
            return;
        }

        frameBuffer = poolFrameBuffers.getNext();
        frameBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        frameBuffer.begin();
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        usedY = 0;
    }


    public void onAppPause() {
        usedY = (int) (1.1 * GraphicsYio.height); // to force buffer recreate
    }


    public void onAppResume() {

    }


    private boolean needToCreateNewFrameBuffer() {
        return usedY + currentRenderer.framePosition.height >= GraphicsYio.height - 2 * GraphicsYio.borderThickness;
    }


    private TextureRegion extractTexture(RectangleYio pos) {
        Texture texture = frameBuffer.getColorBufferTexture();
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        float f = ((FrameBufferYio) frameBuffer).f;
        int y = (int) (usedY * f);
        int width = (int) (pos.width * f);
        int height = (int) (pos.height * f);
        TextureRegion resultTexture = new TextureRegion(texture, 0, y, width, height);
        frameBuffer.end();
        return resultTexture;
    }
}
