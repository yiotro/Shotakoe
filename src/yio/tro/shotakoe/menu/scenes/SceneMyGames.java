package yio.tro.shotakoe.menu.scenes;

import com.badlogic.gdx.Gdx;
import yio.tro.shotakoe.OneTimeInfo;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.info.AbstractInfoScene;
import yio.tro.shotakoe.stuff.StoreLinksYio;

public class SceneMyGames extends AbstractInfoScene {

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        createInfoMenu("my_games_article", getOpenSceneReaction(Scenes.mainMenu));
        createDownloadButton();
    }


    private void createDownloadButton() {
        uiFactory.getButton()
                .setParent(infoPanel)
                .setSize(0.6, 0.055)
                .centerHorizontal()
                .alignBottom(0.025)
                .setBackground(BackgroundYio.gray)
                .applyText("my_games")
                .setReaction(getDownloadReaction());
    }


    @Override
    protected double getHeight() {
        return 0.4;
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        OneTimeInfo.getInstance().myGames = true;
        OneTimeInfo.getInstance().save();
    }


    private Reaction getDownloadReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                Gdx.net.openURI("https://play.google.com/store/apps/developer?id=Yiotro");
            }
        };
    }
}
