package yio.tro.shotakoe.menu.scenes.info;

import com.badlogic.gdx.Gdx;
import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.PlatformType;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.menu.MenuParams;
import yio.tro.shotakoe.menu.elements.AnimationYio;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.button.ButtonYio;
import yio.tro.shotakoe.menu.reactions.Reaction;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.GraphicsYio;

public class SceneAboutGame extends AbstractInfoScene {


    private ButtonYio helpButton;
    private ButtonYio specialThanksButton;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.green;
    }


    @Override
    protected double getHeight() {
        return 0.8;
    }


    @Override
    public void initialize() {
        createInfoMenu("about_game_article", getBackReaction());
        createHelpButton();
        createSpecialThanksButton();
        createDiscordButton();
        createRedditButton();
        createWhatToReadButton();
    }


    private void createWhatToReadButton() {
        uiFactory.getButton()
                .setSize(0.8, 0.05)
                .setParent(infoPanel)
                .centerHorizontal()
                .alignBottom(0.02)
                .setFont(Fonts.miniFont)
                .applyText("what_to_read")
                .setReaction(getOpenSceneReaction(Scenes.whatToRead));
    }


    private void createRedditButton() {
        uiFactory.getCircleButton()
                .setSize(GraphicsYio.convertToWidth(0.09))
                .alignLeft(previousElement, 0.02)
                .alignTop(0.02)
                .setTouchOffset(0.01)
                .loadTexture("menu/external/reddit.png")
                .setAnimation(AnimationYio.up)
                .setReaction(getRedditReaction());
    }


    private Reaction getRedditReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                String link = getRedditLink();
                Gdx.app.getClipboard().setContents(link);
                Scenes.notification.show("link_copied_to_clipboard");
                Gdx.net.openURI(link);
            }
        };
    }


    private void createDiscordButton() {
        uiFactory.getCircleButton()
                .setSize(GraphicsYio.convertToWidth(0.09))
                .alignRight(0.04)
                .alignTop(0.02)
                .setTouchOffset(0.01)
                .loadTexture("menu/external/discord.png")
                .setAnimation(AnimationYio.up)
                .setReaction(getDiscordReaction());
    }


    private Reaction getDiscordReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                String link = getDiscordLink();
                Gdx.app.getClipboard().setContents(link);
                Scenes.notification.show("link_copied_to_clipboard");
                Gdx.net.openURI(link);
            }
        };
    }


    private String getRedditLink() {
        return "https://www.reddit.com/r/yiotro_games/";
    }


    private String getDiscordLink() {
        return "https://discord.gg/Wx27T7znhn";
    }


    private Reaction getBackReaction() {
        return new Reaction() {
            @Override
            protected void apply() {
                onBackButtonPressed();
            }
        };
    }


    private void onBackButtonPressed() {
        yioGdxGame.setGamePaused(true);
        if (YioGdxGame.platformType == PlatformType.ios) {
            Scenes.mainMenu.create();
            return;
        }
        Scenes.settings.create();
    }


    private void createSpecialThanksButton() {
        if (!MenuParams.SPECIAL_THANKS) return;
        specialThanksButton = uiFactory.getButton()
                .setSize(0.7, 0.05)
                .setParent(infoPanel)
                .centerHorizontal()
                .alignBottom(0.02)
                .setFont(Fonts.miniFont)
                .applyText("special_thanks_title")
                .setReaction(getOpenSceneReaction(Scenes.specialThanks));
    }


    private void createHelpButton() {
        if (!MenuParams.HELP_SECTION) return;
        helpButton = uiFactory.getButton()
                .setPosition(0.55, 0.9, 0.4, 0.07)
                .applyText("help")
                .setReaction(new Reaction() {
                    @Override
                    protected void apply() {

                    }
                });
    }

}
