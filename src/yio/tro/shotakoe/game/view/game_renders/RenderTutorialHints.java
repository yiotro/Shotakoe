package yio.tro.shotakoe.game.view.game_renders;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.game.general.TutorialHint;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;

public class RenderTutorialHints extends GameRender{

    @Override
    protected void loadTextures() {

    }


    @Override
    public void render() {
        for (TutorialHint tutorialHint : getObjectsLayer().tutorialHints) {
            for (RenderableTextYio renderableTextYio : tutorialHint.visualTextContainer.viewList) {
                BitmapFont font = Fonts.buttonFont;
                font.getData().setScale(0.65f);
                GraphicsYio.renderText(
                        batchMovable,
                        font,
                        renderableTextYio.string,
                        renderableTextYio.position
                );
                font.getData().setScale(1);
            }
        }
    }


    @Override
    protected void disposeTextures() {

    }
}
