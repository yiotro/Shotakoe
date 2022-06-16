package yio.tro.shotakoe.menu.scenes.gameplay;

import yio.tro.shotakoe.PaidStatus;
import yio.tro.shotakoe.game.general.CampaignManager;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.menu.LanguagesManager;
import yio.tro.shotakoe.menu.MenuParams;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.ScrollHelperElement;
import yio.tro.shotakoe.menu.elements.customizable_list.*;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.SceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class SceneCampaign extends SceneYio {

    public CustomizableListYio customizableListYio;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        createCampaignView();
        spawnBackButton(getBackReaction());
    }


    private void createCampaignView() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05)
                .setAppearParameters(MovementType.inertia, 0.8 * MenuParams.ANIM_SPEED)
                .setDestroyParameters(MovementType.lighty, 2 * MenuParams.ANIM_SPEED);

        loadValues();
    }


    private void loadValues() {
        int colorIndex = 0;
        customizableListYio.clearItems();
        addSeparator("-", true, 0.02f * GraphicsYio.height, null);
        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle(LanguagesManager.getInstance().getString("campaign"));
        customizableListYio.addItem(titleListItem);

        int n = 1 + CampaignManager.getLastLevelIndex() / CampaignCustomItem.ROW;
        for (int i = 0; i < n; i++) {
            CampaignCustomItem campaignCustomItem = new CampaignCustomItem();
            campaignCustomItem.set(i * CampaignCustomItem.ROW, Math.min((i + 1) * CampaignCustomItem.ROW - 1, CampaignManager.getLastLevelIndex()));
            campaignCustomItem.setColor(ColorYio.values()[colorIndex]);
            customizableListYio.addItem(campaignCustomItem);
            if (checkForFreeVersionCutOff(i)) break;

            if (i > 2 && (i + 1) % 5 == 0) {
                colorIndex = getNextColorIndex(colorIndex);
                addSeparator("" + (i + 1) * CampaignCustomItem.ROW, false, 0.06f * GraphicsYio.height, ColorYio.values()[colorIndex]);
            }
        }
    }


    private boolean checkForFreeVersionCutOff(int i) {
        if (PaidStatus.PRO_VERSION) return false;
        if (i != 16) return false;
        addSeparator("", true, 0.02f * GraphicsYio.height, ColorYio.cyan);
        customizableListYio.addItem(new WantMoreItem());
        addSeparator("", true, 0.03f * GraphicsYio.height, ColorYio.cyan);
        return true;
    }


    private int getNextColorIndex(int colorIndex) {
        int nextColorIndex = colorIndex + 1;
        if (nextColorIndex >= ColorYio.values().length) {
            nextColorIndex = 0;
        }
        return nextColorIndex;
    }


    private void addSeparator(String key, boolean empty, float height, ColorYio color) {
        SectionStartListItem sectionStartListItem = new SectionStartListItem();
        sectionStartListItem.setValues(key, empty, height);
        sectionStartListItem.setColor(color);
        customizableListYio.addItem(sectionStartListItem);
    }


    @Override
    protected void onEndCreation() {
        super.onEndCreation();
        scrollToFirstUnlockedLevel();
    }


    private void scrollToFirstUnlockedLevel() {
        customizableListYio.resetScroll();
        int index = CampaignManager.getInstance().getIndexOfFirstUnlockedLevel();
        CampaignCustomItem itemWithIndex = findItemWithIndex(index);
        if (itemWithIndex == null) return;

        customizableListYio.scrollToItem(itemWithIndex);
    }


    private CampaignCustomItem findItemWithIndex(int index) {
        for (AbstractCustomListItem item : customizableListYio.items) {
            if (!(item instanceof CampaignCustomItem)) continue;
            if (!((CampaignCustomItem) item).containsLevelIndex(index)) continue;
            return (CampaignCustomItem) item;
        }
        return null;
    }


    public void onLevelMarkedAsCompleted() {
        if (customizableListYio == null) return;
        loadValues();
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                if (PaidStatus.PRO_VERSION) {
                    Scenes.chooseGameMode.create();
                    return;
                }
                Scenes.mainMenu.create();
            }
        };
    }
}
