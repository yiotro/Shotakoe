package yio.tro.shotakoe.menu.scenes.info;

import yio.tro.shotakoe.menu.elements.AnimationYio;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.customizable_list.BigTextItem;
import yio.tro.shotakoe.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.shotakoe.menu.scenes.SceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class SceneWhatToRead extends SceneYio {


    private CustomizableListYio customizableListYio;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected void initialize() {
        createList();
        spawnBackButton(getOpenSceneReaction(Scenes.aboutGame));

    }


    private void createList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(1, 1)
                .centerHorizontal()
                .alignBottom(0)
                .setShadowEnabled(false)
                .setAppearParameters(MovementType.inertia, 1.6)
                .setInternalOffset(0.04f * GraphicsYio.width)
                .setCornerRadius(0)
                .setConvexEnabled(false)
                .setBackgroundEnabled(true);

        BigTextItem bigTextItem = new BigTextItem();
        bigTextItem.applyText(customizableListYio, "# # # #" + languagesManager.getString("what_to_read_article"));
        customizableListYio.addItem(bigTextItem);
    }
}
