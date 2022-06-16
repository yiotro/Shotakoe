package yio.tro.shotakoe.menu.scenes.options;

import yio.tro.shotakoe.menu.CustomLanguageLoader;
import yio.tro.shotakoe.menu.LanguageChooseItem;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.shotakoe.menu.elements.customizable_list.ScrollListItem;
import yio.tro.shotakoe.menu.elements.customizable_list.SliReaction;
import yio.tro.shotakoe.menu.elements.customizable_list.TitleListItem;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.SceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.GraphicsYio;

import java.util.ArrayList;

public class SceneLanguages extends SceneYio {

    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    public void initialize() {
        initList();
        spawnBackButton(getBackReaction());
    }


    private void initList() {
        CustomizableListYio customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05);

        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(LanguagesManager.getInstance().getString("languages"));
        customizableListYio.addItem(titleListItem);

        SliReaction clickReaction = getClickReaction();
        ArrayList<LanguageChooseItem> chooseListItems = LanguagesManager.getInstance().getChooseListItems();
        for (LanguageChooseItem chooseListItem : chooseListItems) {
            ScrollListItem scrollListItem = new ScrollListItem();
            scrollListItem.setTitle(chooseListItem.title);
            scrollListItem.setKey(chooseListItem.name);
            scrollListItem.setClickReaction(clickReaction);
            scrollListItem.setHeight(0.08f * GraphicsYio.height);
            customizableListYio.addItem(scrollListItem);
        }
    }


    private SliReaction getClickReaction() {
        return new SliReaction() {
            @Override
            public void apply(ScrollListItem item) {
                applyLanguage(item.key);
            }
        };
    }


    private void applyLanguage(String key) {
        CustomLanguageLoader.setAndSaveLanguage(key);
        menuControllerYio.clear();
        Scenes.mainMenu.create();
    }



    private Reaction getBackReaction() {
        return getOpenSceneReaction(Scenes.settings);
    }
}
