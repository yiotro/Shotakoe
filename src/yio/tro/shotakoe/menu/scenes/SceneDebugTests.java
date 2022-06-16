package yio.tro.shotakoe.menu.scenes;

import yio.tro.shotakoe.Yio;
import yio.tro.shotakoe.game.general.CampaignManager;
import yio.tro.shotakoe.game.general.LpWorker;
import yio.tro.shotakoe.game.loading.LoadingType;
import yio.tro.shotakoe.game.tests.AbstractTest;
import yio.tro.shotakoe.menu.elements.customizable_list.DtCustomItem;
import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.elements.CircleButtonYio;
import yio.tro.shotakoe.menu.elements.customizable_list.CustomizableListYio;
import yio.tro.shotakoe.menu.elements.customizable_list.TitleListItem;

import java.util.Random;

public class SceneDebugTests extends SceneYio {


    private CircleButtonYio backButton;
    CustomizableListYio customizableListYio;
    AbstractTest[] tests;


    @Override
    public BackgroundYio getBackgroundValue() {
        return BackgroundYio.magenta;
    }


    @Override
    protected void initialize() {
        createBackButton();
        createTestsArray();
        createCustomizableList();
    }


    private void createTestsArray() {
        tests = new AbstractTest[]{
                createOpenSceneTest("Test screen", Scenes.testScreen),
                createForceExceptionTest(),
                createOpenSceneTest("Research factor behavior", Scenes.researchFactorBehavior),
                createOpenSceneTest("Campaign finish", Scenes.easterEgg),
                createOpenSceneTest("About pro version", Scenes.aboutProVersion),
                createLaunchRandomMatchTest(),
                createCheckCampaignTest(),
        };
    }


    private AbstractTest createCheckCampaignTest() {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            public String getName() {
                return "Check campaign";
            }


            @Override
            protected void execute() {
                doCheckCampaign();
            }
        };
    }


    void doCheckCampaign() {
        for (int i = 0; i <= CampaignManager.LAST_LEVEL_INDEX; i++) {
            if (i > 0 && i % 100 == 0) {
                System.out.println("SceneDebugTests.doCheckCampaign: " + i);
            }
            CampaignManager.getInstance().launchCampaignLevel(yioGdxGame, i);
        }
    }


    private AbstractTest createLaunchRandomMatchTest() {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            public String getName() {
                return "Launch random match";
            }


            @Override
            protected void execute() {
                LpWorker lpWorker = new LpWorker(new Random());
                yioGdxGame.loadingManager.startInstantly(LoadingType.skirmish, lpWorker.apply());
            }
        };
    }


    private AbstractTest createForceExceptionTest() {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            public String getName() {
                return "Force exception";
            }


            @Override
            protected void execute() {
                Yio.forceException();
            }
        };
    }


    private AbstractTest createOpenSceneTest(final String name, final SceneYio sceneYio) {
        return new AbstractTest() {
            @Override
            public boolean isInstant() {
                return true;
            }


            @Override
            public String getName() {
                return name;
            }


            @Override
            protected void execute() {
                sceneYio.create();
            }
        };
    }


    private void createCustomizableList() {
        customizableListYio = uiFactory.getCustomizableListYio()
                .setSize(0.9, 0.8)
                .centerHorizontal()
                .alignBottom(0.05);

        TitleListItem titleListItem = new TitleListItem();
        titleListItem.setTitle("Tests");
        customizableListYio.addItem(titleListItem);

        for (AbstractTest test : tests) {
            DtCustomItem dtCustomItem = new DtCustomItem();
            dtCustomItem.setTest(test);
            customizableListYio.addItem(dtCustomItem);
        }
    }


    private void createBackButton() {
        backButton = spawnBackButton(getOpenSceneReaction(Scenes.mainMenu));
    }

}
