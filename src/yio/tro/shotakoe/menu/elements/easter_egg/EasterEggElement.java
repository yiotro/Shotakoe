package yio.tro.shotakoe.menu.elements.easter_egg;

import yio.tro.shotakoe.RefreshRateDetector;
import yio.tro.shotakoe.YioGdxGame;
import yio.tro.shotakoe.game.general.ColorYio;
import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.RenderInterfaceElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RepeatYio;
import yio.tro.shotakoe.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class EasterEggElement extends InterfaceElement<EasterEggElement> {

    public static final int MAX_SIZE = 100;
    public ArrayList<EeItem> items;
    private ObjectPoolYio<EeItem> poolItems;
    RepeatYio<EasterEggElement> repeatRemoveDead;
    long nextSpawnTime;
    long delay;
    float currentSpeed;
    float speedLimit;


    public EasterEggElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        items = new ArrayList<>();
        nextSpawnTime = 0;
        delay = 500;
        currentSpeed = 0.01f * GraphicsYio.height;
        speedLimit = 0.4f * GraphicsYio.height;
        initPools();
        initRepeats();
    }


    private void initRepeats() {
        repeatRemoveDead = new RepeatYio<EasterEggElement>(this, 15) {
            @Override
            public void performAction() {
                parent.removeDeadItems();
            }
        };
    }


    private void initPools() {
        poolItems = new ObjectPoolYio<EeItem>(items) {
            @Override
            public EeItem makeNewObject() {
                return new EeItem(EasterEggElement.this);
            }
        };
    }


    @Override
    protected EasterEggElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        moveItems();
        checkToSpawnNewItem();
        repeatRemoveDead.move();
    }


    private void removeDeadItems() {
        if (items.size() == 0) return;
        EeItem firstItem = items.get(0);
        if (!firstItem.isReadyToBeRemoved()) return;
        poolItems.removeFromExternalList(firstItem);
    }


    private void checkToSpawnNewItem() {
        if (System.currentTimeMillis() < nextSpawnTime) return;
        if (items.size() == MAX_SIZE) return;
        spawnItem();
        delay = Math.max(50, delay - 25);
        nextSpawnTime = System.currentTimeMillis() + delay;
        currentSpeed += 0.001f * GraphicsYio.height;
        if (currentSpeed > speedLimit) {
            currentSpeed = speedLimit;
        }
    }


    private void spawnItem() {
        EeItem next = poolItems.getNext();
        items.add(0, next);
        next.colorIndex = getRandomColorIndex();
        next.speed = currentSpeed * RefreshRateDetector.getInstance().multiplier;
        next.position.width = 0.1f * GraphicsYio.width;
        next.position.height = (0.3f + 0.2f * YioGdxGame.random.nextFloat()) * GraphicsYio.height;
        next.position.y = GraphicsYio.height + 1;
        next.position.x = YioGdxGame.random.nextFloat() * (GraphicsYio.width - next.position.width);
    }


    private int getRandomColorIndex() {
        ColorYio[] values = ColorYio.values();
        return YioGdxGame.random.nextInt(values.length);
    }


    private void moveItems() {
        for (EeItem eeItem : items) {
            eeItem.move();
        }
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {

    }


    @Override
    public boolean checkToPerformAction() {
        return false;
    }


    @Override
    public boolean touchDown() {
        return false;
    }


    @Override
    public boolean touchDrag() {
        return false;
    }


    @Override
    public boolean touchUp() {
        return false;
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderEasterEggElement;
    }
}
