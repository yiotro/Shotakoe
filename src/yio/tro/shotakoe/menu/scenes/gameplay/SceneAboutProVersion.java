package yio.tro.shotakoe.menu.scenes.gameplay;

import com.badlogic.gdx.Gdx;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.menu.scenes.info.AbstractInfoScene;
import yio.tro.shotakoe.stuff.StoreLinksYio;

public class SceneAboutProVersion extends AbstractInfoScene {

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        createInfoMenu("about_pro_version", getOpenSceneReaction(Scenes.campaign));
        createDownloadButton();
    }


    private void createDownloadButton() {
        uiFactory.getButton()
                .setParent(infoPanel)
                .setSize(0.6, 0.055)
                .centerHorizontal()
                .alignBottom(0.025)
                .setBackground(BackgroundYio.gray)
                .applyText("download")
                .setReaction(getDownloadReaction());
    }


    private Reaction getDownloadReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Gdx.net.openURI(StoreLinksYio.getInstance().getProLink("shotakoe"));
            }
        };
    }
}
