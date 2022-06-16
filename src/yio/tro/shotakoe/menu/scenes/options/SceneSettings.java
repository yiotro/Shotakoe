package yio.tro.shotakoe.menu.scenes.options;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.PlatformType;
import yio.tro.shotakoe.SettingsManager;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.menu.elements.*;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.SceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class SceneSettings extends SceneYio {

    AnnounceViewElement mainLabel;
    public CheckButtonYio chkSound;
    public CheckButtonYio chkFullScreen;
    private ScrollableAreaYio scrollableAreaYio;
    private CircleButtonYio infoButton;
    private CheckButtonYio chkSimplifiedAnimation;
    private CheckButtonYio chkRoundedScreen;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    public void initialize() {
        createScrollableArea();
        createMainLabel();
        createTopCover();
        createBackButton();
        createInfoButton();
    }


    private void createTopCover() {
        uiFactory.getTopCoverElement()
                .setSize(1, 0.13)
                .alignTop(0)
                .setAnimation(AnimationYio.up)
                .setTargetScrollEngine(scrollableAreaYio.getScrollEngineYio())
                .setTitle("settings");
    }


    private void createBackButton() {
        spawnBackButton(new Reaction() {
            @Override
            protected void apply() {
                applyAndSave();
                Scenes.mainMenu.create();
            }
        });
    }


    void applyAndSave() {
        applyValues();
        SettingsManager.getInstance().saveValues();
    }


    private void createScrollableArea() {
        scrollableAreaYio = uiFactory.getScrollableAreaYio()
                .setSize(1, 0.85)
                .setAnimation(AnimationYio.none);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        loadValues();
    }


    @Override
    protected void onEndCreation() {
        super.onEndCreation();
        forceElementToTop(backButton);
        forceElementToTop(infoButton);
    }


    private void createInfoButton() {
        if (YioGdxGame.platformType == PlatformType.ios) return;

        infoButton = uiFactory.getCircleButton()
                .setSize(GraphicsYio.convertToWidth(0.09))
                .alignRight(0.04)
                .alignTop(0.02)
                .setTouchOffset(0.05)
                .setAnimation(AnimationYio.up)
                .loadTexture("menu/info_icon.png")
                .setReaction(getInfoReaction());
    }


    private Reaction getInfoReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                applyAndSave();
                Scenes.aboutGame.create();
            }
        };
    }


    private void createLangButton() {
        uiFactory.getButton()
                .setParent(mainLabel)
                .setSize(0.4, 0.05)
                .centerHorizontal()
                .alignBottom(0.02)
                .applyText("language")
                .setReaction(getLanguageReaction());
    }


    private Reaction getLanguageReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                applyAndSave();
                Scenes.languages.create();
            }
        };
    }


    private void createMainLabel() {
        double h = 0.42;
        mainLabel = uiFactory.getAnnounceViewElement()
                .setParent(scrollableAreaYio)
                .setSize(0.9, h)
                .alignBottom(0.45 - h / 2)
                .centerHorizontal()
                .setTitle("settings")
                .setText(" ")
                .setTouchable(false);

        createCheckButtons();
        createLangButton();
    }


    private void createCheckButtons() {
        chkSound = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .setHeight(0.06)
                .setFont(Fonts.miniFont)
                .setName("sound")
                .alignTop(0.06)
                .setReaction(getSoundReaction());

        chkFullScreen = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .clone(previousElement)
                .setName("full_screen")
                .alignUnder(previousElement, 0);

        chkSimplifiedAnimation = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .clone(previousElement)
                .setName("simplified_animation")
                .alignUnder(previousElement, 0);

        chkRoundedScreen = uiFactory.getCheckButton()
                .setParent(mainLabel)
                .clone(previousElement)
                .setName("rounded_screen")
                .alignUnder(previousElement, 0);
    }


    private Reaction getSoundReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                SettingsManager.getInstance().soundEnabled = chkSound.isChecked();
            }
        };
    }


    private void applyValues() {
        SettingsManager instance = SettingsManager.getInstance();

        instance.soundEnabled = chkSound.isChecked();
        instance.fullScreenMode = chkFullScreen.isChecked();
        instance.simplifiedAnimation = chkSimplifiedAnimation.isChecked();
        instance.roundedScreen = chkRoundedScreen.isChecked();

        instance.onValuesChanged();
    }


    public void loadValues() {
        SettingsManager instance = SettingsManager.getInstance();

        chkSound.setChecked(instance.soundEnabled);
        chkFullScreen.setChecked(instance.fullScreenMode);
        chkSimplifiedAnimation.setChecked(instance.simplifiedAnimation);
        chkRoundedScreen.setChecked(instance.roundedScreen);
    }

}
