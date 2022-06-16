package yio.tro.shotakoe.menu.scenes.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.general.Difficulty;
import yio.tro.shotakoe.game.general.LevelSize;
import yio.tro.shotakoe.game.general.TurnType;
import yio.tro.shotakoe.game.loading.LoadingParameters;
import yio.tro.shotakoe.game.loading.LoadingType;
import yio.tro.shotakoe.menu.MenuParams;
import yio.tro.shotakoe.menu.elements.AnnounceViewElement;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.CheckButtonYio;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.elements.SliderElement;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.SceneYio;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.factor_yio.MovementType;

public class SceneSetupSkirmish extends SceneYio {


    public ButtonYio startButton;
    private AnnounceViewElement panel;
    private SliderElement sliderDifficulty;
    FsItem[] fsItems;
    String[] fsStrings;
    int[] targetSizeValues;
    String[] targetSizeStrings;
    int[] turnLimitValues;
    String[] turnLimitStrings;
    private SliderElement sliderFieldSize;
    private SliderElement sliderTargetSize;
    private SliderElement sliderTurnLimit;
    private CheckButtonYio chkEnemy;
    private CheckButtonYio chkBomb;
    private CheckButtonYio chkSymmetry;
    private CheckButtonYio chkSingleCluster;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.cyan;
    }


    @Override
    protected void initialize() {
        spawnBackButton(getBackReaction());
        createMainPanel();
        initArrays();
        createInternals();
        createStartButton();
        loadValues();
    }


    private void initArrays() {
        fsItems = new FsItem[]{
                new FsItem(15, 19),
                new FsItem(17, 21),
                new FsItem(19, 21),
                new FsItem(19, 23),
                new FsItem(23, 27),
                new FsItem(25, 30),
                new FsItem(27, 31),
                new FsItem(29, 35),
                new FsItem(33, 40),
                new FsItem(43, 50),
                new FsItem(45, 55),
        };
        fsStrings = new String[fsItems.length];
        for (int i = 0; i < fsItems.length; i++) {
            fsStrings[i] = fsItems[i].width + "x" + fsItems[i].height;
        }
        targetSizeValues = new int[]{-1, 25, 33, 40, 50, 66, 75, 100, 125, 150};
        targetSizeStrings = new String[targetSizeValues.length];
        for (int i = 0; i < targetSizeValues.length; i++) {
            targetSizeStrings[i] = "" + targetSizeValues[i];
        }
        targetSizeStrings[0] = languagesManager.getString("absent").toLowerCase();
        turnLimitValues = new int[]{-1, 5, 7, 10, 12, 15};
        turnLimitStrings = new String[turnLimitValues.length];
        for (int i = 0; i < turnLimitValues.length; i++) {
            turnLimitStrings[i] = "" + turnLimitValues[i];
        }
        turnLimitStrings[0] = languagesManager.getString("absent").toLowerCase();
    }


    private void createMainPanel() {
        double h = 0.73;
        panel = uiFactory.getAnnounceViewElement()
                .setSize(0.85, h)
                .centerHorizontal()
                .alignBottom(0.45 - h / 2)
                .setText(" ");
    }


    private void createInternals() {
        sliderDifficulty = uiFactory.getSlider()
                .setParent(panel)
                .centerHorizontal()
                .alignTop(0.03)
                .setTitle("difficulty")
                .setPossibleValues(Difficulty.class);

        sliderFieldSize = uiFactory.getSlider()
                .setParent(panel)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setTitle("field_size")
                .setPossibleValues(fsStrings);

        sliderTargetSize = uiFactory.getSlider()
                .setParent(panel)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setTitle("target_size")
                .setPossibleValues(targetSizeStrings);

        sliderTurnLimit = uiFactory.getSlider()
                .setParent(panel)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setTitle("turn_limit")
                .setPossibleValues(turnLimitStrings);

        chkEnemy = uiFactory.getCheckButton()
                .setParent(panel)
                .setFont(Fonts.miniFont)
                .setHeight(0.05)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setName("rival");

        chkBomb = uiFactory.getCheckButton()
                .setParent(panel)
                .setFont(Fonts.miniFont)
                .setHeight(0.05)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setName("bomb");

        chkSymmetry = uiFactory.getCheckButton()
                .setParent(panel)
                .setFont(Fonts.miniFont)
                .setHeight(0.05)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setName("symmetry");

        chkSingleCluster = uiFactory.getCheckButton()
                .setParent(panel)
                .setFont(Fonts.miniFont)
                .setHeight(0.05)
                .centerHorizontal()
                .alignUnder(previousElement, 0)
                .setName("single_cluster");
    }


    private void loadValues() {
        Preferences prefs = getPreferences();
        sliderDifficulty.setValueIndex(prefs.getInteger("difficulty", 1));
        sliderFieldSize.setValueIndex(prefs.getInteger("field_size", 5));
        sliderTargetSize.setValueIndex(prefs.getInteger("target_size", 0));
        sliderTurnLimit.setValueIndex(prefs.getInteger("turn_limit", 0));
        chkEnemy.setChecked(prefs.getBoolean("enemy", true));
        chkBomb.setChecked(prefs.getBoolean("bomb", false));
        chkSymmetry.setChecked(prefs.getBoolean("symmetry", false));
        chkSingleCluster.setChecked(prefs.getBoolean("single_cluster", false));
    }


    private void saveValues() {
        Preferences prefs = getPreferences();
        prefs.putInteger("difficulty", sliderDifficulty.getValueIndex());
        prefs.putInteger("field_size", sliderFieldSize.getValueIndex());
        prefs.putInteger("target_size", sliderTargetSize.getValueIndex());
        prefs.putInteger("turn_limit", sliderTurnLimit.getValueIndex());
        prefs.putBoolean("enemy", chkEnemy.isChecked());
        prefs.putBoolean("bomb", chkBomb.isChecked());
        prefs.putBoolean("symmetry", chkSymmetry.isChecked());
        prefs.putBoolean("single_cluster", chkSingleCluster.isChecked());
        prefs.flush();
    }


    public Preferences getPreferences() {
        return Gdx.app.getPreferences("shotakoe.skirmish");
    }


    private void createStartButton() {
        startButton = uiFactory.getButton()
                .setParent(panel)
                .setSize(0.35, 0.055)
                .alignRight(0.03)
                .alignBottom(0.025)
                .setBackground(BackgroundYio.gray)
                .setTouchOffset(0.04)
                .applyText("start")
                .setAppearParameters(MovementType.soft_big_rubber_band, 1.3 * MenuParams.ANIM_SPEED)
                .setReaction(getStartReaction())
                .setHotkeyKeycode(Input.Keys.ENTER);
    }


    private Reaction getStartReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onStartButtonPressed();
            }
        };
    }


    private void onStartButtonPressed() {
        saveValues();
        LoadingParameters loadingParameters = new LoadingParameters();
        loadingParameters.setLevelSize(LevelSize.def);
        loadingParameters.setDifficulty(Difficulty.values()[sliderDifficulty.getValueIndex()]);
        loadingParameters.setSeed(YioGdxGame.random.nextLong());
        FsItem fsItem = fsItems[sliderFieldSize.getValueIndex()];
        loadingParameters.setFieldWidth(fsItem.width);
        loadingParameters.setFieldHeight(fsItem.height);
        loadingParameters.setTargetSize(targetSizeValues[sliderTargetSize.getValueIndex()]);
        loadingParameters.setTurnLimit(turnLimitValues[sliderTurnLimit.getValueIndex()]);
        loadingParameters.lgParameters.setEnemy(chkEnemy.isChecked());
        loadingParameters.lgParameters.setBomb(chkBomb.isChecked());
        loadingParameters.lgParameters.setSymmetry(chkSymmetry.isChecked());
        loadingParameters.lgParameters.setSameStartingCluster(chkSingleCluster.isChecked());
        loadingParameters.setFirstTurn(TurnType.human);
        yioGdxGame.loadingManager.startInstantly(LoadingType.skirmish, loadingParameters);
    }


    protected Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                saveValues();
                destroy();
                Scenes.chooseGameMode.create();
            }
        };
    }
}
