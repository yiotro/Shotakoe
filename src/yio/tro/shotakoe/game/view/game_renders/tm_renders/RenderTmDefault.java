package yio.tro.shotakoe.game.view.game_renders.tm_renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.game.touch_modes.TmDefault;
import yio.tro.shotakoe.game.touch_modes.TouchMode;
import yio.tro.shotakoe.game.view.game_renders.GameRender;
import yio.tro.shotakoe.stuff.CircleYio;
import yio.tro.shotakoe.stuff.GraphicsYio;

import java.util.HashMap;

public class RenderTmDefault extends GameRender {

    private TmDefault tm;


    public RenderTmDefault() {

    }


    @Override
    protected void loadTextures() {

    }


    @Override
    public void render() {
        tm = TouchMode.tmDefault;
    }


    @Override
    protected void disposeTextures() {

    }
}
