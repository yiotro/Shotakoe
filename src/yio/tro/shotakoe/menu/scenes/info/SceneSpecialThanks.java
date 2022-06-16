package yio.tro.shotakoe.menu.scenes.info;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.menu.LanguageChooseItem;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.scenes.Scenes;

import java.util.ArrayList;

public class SceneSpecialThanks extends AbstractInfoScene {


    private ButtonYio infoPanel;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    public void initialize() {
        spawnBackButton(getOpenSceneReaction(Scenes.aboutGame));

        String translatorsString = getTranslatorsString();
        String topic = languagesManager.getString("special_thanks_begin") +
                getPredefinedString() +
                translatorsString;

        infoPanel = uiFactory.getButton()
                .setPosition(0.05, 0.1, 0.9, 0.7)
                .setTouchable(false)
                .setFont(Fonts.miniFont)
                .applyManyTextLines(topic);
    }


    private String getPredefinedString() {
        return "-#";
    }


    private String getTranslatorsString() {
        ArrayList<LanguageChooseItem> chooseListItems = LanguagesManager.getInstance().getChooseListItems();

        StringBuilder builder = new StringBuilder();

        for (LanguageChooseItem chooseListItem : chooseListItems) {
            if (chooseListItem.author.equals("yiotro")) continue;

            builder.append("#").append(chooseListItem.name).append(": ").append(chooseListItem.author);
        }

        return builder.toString();
    }
}
