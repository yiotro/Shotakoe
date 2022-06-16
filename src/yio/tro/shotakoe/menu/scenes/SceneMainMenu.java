package yio.tro.shotakoe.menu.scenes;

import com.badlogic.gdx.Input;
import yio.tro.shotakoe.OneTimeInfo;
import yio.tro.shotakoe.PaidStatus;
import yio.tro.shotakoe.PlatformType;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.general.CampaignManager;
import yio.tro.shotakoe.game.loading.LoadingParameters;
import yio.tro.shotakoe.game.loading.LoadingType;
import yio.tro.shotakoe.menu.MenuParams;
import yio.tro.shotakoe.menu.elements.AnimationYio;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.CircleButtonYio;
import yio.tro.shotakoe.menu.elements.ConditionYio;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class SceneMainMenu extends SceneYio {

    public CircleButtonYio exitButton;
    public CircleButtonYio settingsButton;
    public CircleButtonYio playButton;
    private double verticalPosition;
    private double iconSize;
    private double playButtonSize;
    private ButtonYio logoButton;
    private double logoWidth;
    private double iconOffset;
    private double touchOffset;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    public void initialize() {
        initMetrics();

        createRightButton();
        createSettingsButton();
        createPlayButton();
        createLogo();
        createMyGamesButton();
    }


    private void createMyGamesButton() {
        uiFactory.getButton()
                .setSize(0.3, 0.03)
                .alignBottom(0.01)
                .alignRight(GraphicsYio.convertToWidth(0.01))
                .setTouchOffset(0.03)
                .setBackground(BackgroundYio.yellow)
                .setShadow(false)
                .setConvex(false)
                .setAnimation(AnimationYio.down)
                .applyText("my_games")
                .setAllowedToAppear(getMyGamesCondition())
                .setReaction(getOpenSceneReaction(Scenes.myGames));
    }


    private ConditionYio getMyGamesCondition() {
        return new ConditionYio() {
            @Override
            public boolean get() {
                if (YioGdxGame.platformType == PlatformType.ios) return false;
                if (OneTimeInfo.getInstance().myGames) return false;
                if (CampaignManager.getInstance().completedLevels.size() < 50) return false;
                return true;
            }
        };
    }


    private void createRightButton() {
        if (YioGdxGame.platformType == PlatformType.ios) {
            createInfoButton();
            return;
        }
        createExitButton();
    }


    private void createInfoButton() {
        uiFactory.getCircleButton()
                .setSize(iconSize)
                .alignBottom(verticalPosition - GraphicsYio.convertToHeight(iconSize) / 2)
                .alignRight(iconOffset)
                .setTouchOffset(touchOffset)
                .loadTexture("menu/main_menu/mm_info_icon.png")
                .setAppearParameters(MenuParams.APPEAR_MOVEMENT, 0.8 * MenuParams.ANIM_SPEED)
                .setReaction(getOpenSceneReaction(Scenes.aboutGame));
    }


    private void createLogo() {
        double logoHeight = GraphicsYio.convertToHeight(logoWidth) / 2;
        double logoY = 0.5;

        uiFactory.getButton()
                .setSize(0.7, GraphicsYio.convertToHeight(0.005))
                .centerHorizontal()
                .alignBottom(logoY + 0.01)
                .loadCustomTexture("menu/main_menu/black_line.png")
                .setAppearParameters(MenuParams.APPEAR_MOVEMENT, 0.9 * MenuParams.ANIM_SPEED)
                .setTouchable(false);

        logoButton = uiFactory.getButton()
                .setSize(logoWidth, logoHeight)
                .centerHorizontal()
                .alignBottom(logoY)
                .loadCustomTexture("menu/main_menu/mm_logo.png")
                .setTouchable(false);
    }


    private void createPlayButton() {
        playButton = uiFactory.getCircleButton()
                .loadTexture("menu/main_menu/play_button.png")
                .setPosition((1 - playButtonSize) / 2, verticalPosition - GraphicsYio.convertToHeight(playButtonSize) / 2)
                .setSize(playButtonSize)
                .setTouchOffset(touchOffset)
                .setHotkeyKeycode(Input.Keys.ENTER)
                .setReaction(getPlayReaction());
    }


    private Reaction getPlayReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onPlayButtonPressed();
            }
        };
    }


    private void createSettingsButton() {
        settingsButton = uiFactory.getCircleButton()
                .loadTexture("menu/main_menu/settings_icon.png")
                .clone(previousElement)
                .alignBottom(verticalPosition - GraphicsYio.convertToHeight(iconSize) / 2)
                .alignLeft(iconOffset)
                .setAppearParameters(MenuParams.APPEAR_MOVEMENT, 0.9 * MenuParams.ANIM_SPEED)
                .setReaction(getOpenSceneReaction(Scenes.settings));
    }


    private void createExitButton() {
        exitButton = uiFactory.getCircleButton()
                .setSize(iconSize)
                .alignBottom(verticalPosition - GraphicsYio.convertToHeight(iconSize) / 2)
                .alignRight(iconOffset)
                .setTouchOffset(touchOffset)
                .loadTexture("menu/main_menu/quit_icon.png")
                .setHotkeyKeycode(Input.Keys.BACK)
                .setAppearParameters(MenuParams.APPEAR_MOVEMENT, 0.9 * MenuParams.ANIM_SPEED)
                .setReaction(getExitReaction());
    }


    private Reaction getExitReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                yioGdxGame.exitApp();
            }
        };
    }


    private void initMetrics() {
        verticalPosition = 0.4;
        iconSize = 0.16;
        playButtonSize = 0.32;
        logoWidth = 0.5;
        iconOffset = 0.07;
        touchOffset = 0.05;
    }


    private void onPlayButtonPressed() {
        if (checkForTutorial()) return;
        if (PaidStatus.PRO_VERSION) {
            Scenes.chooseGameMode.create();
            return;
        }
        Scenes.campaign.create();
    }


    private boolean checkForTutorial() {
        if (OneTimeInfo.getInstance().tutorial) return false;
        OneTimeInfo.getInstance().tutorial = true;
        OneTimeInfo.getInstance().save();
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.levelIndex = 0;
        System.out.println("SceneMainMenu.checkForTutorial");
        yioGdxGame.loadingManager.startInstantly(LoadingType.campaign, loadingParameters);
        return true;
    }

}
