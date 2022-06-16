package yio.tro.shotakoe.menu.elements.gameplay;

import yio.tro.shotakoe.Fonts;
import yio.tro.shotakoe.SettingsManager;
import yio.tro.shotakoe.game.general.Cell;
import yio.tro.shotakoe.game.general.CellField;
import yio.tro.shotakoe.game.general.CellType;
import yio.tro.shotakoe.game.general.WaveWorker;
import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.RenderInterfaceElement;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;
import yio.tro.shotakoe.stuff.RepeatYio;

import java.util.ArrayList;

public class CaptureViewElement extends InterfaceElement<CaptureViewElement> {

    public RenderableTextYio title;
    RepeatYio<CaptureViewElement> repeatUpdate;
    WaveWorker waveSameColor;
    private ArrayList<Cell> tempList;


    public CaptureViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        title = new RenderableTextYio();
        title.setFont(Fonts.miniFont);
        waveSameColor = null;
        tempList = new ArrayList<>();
        setTitle("-");
        initRepeats();
    }


    private void initWavesIfNecessary() {
        if (waveSameColor != null) return;
        waveSameColor = new WaveWorker(getObjectsLayer().cellField) {
            @Override
            protected boolean condition(Cell cell, Cell parentCell) {
                return cell.color == parentCell.color;
            }
        };
    }


    private void initRepeats() {
        repeatUpdate = new RepeatYio<CaptureViewElement>(this, 15) {
            @Override
            public void performAction() {
                parent.updateTitleString();
            }
        };
    }


    public void setTitle(String string) {
        title.setString(string);
        title.updateMetrics();
    }


    @Override
    protected CaptureViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        repeatUpdate.move();
        updateTitlePosition();
    }


    private void updateTitlePosition() {
        title.centerVertical(viewPosition);
        if (SettingsManager.getInstance().roundedScreen) {
            title.position.x = 0.05f * GraphicsYio.width;
        } else {
            title.position.x = 0.02f * GraphicsYio.width;
        }
        title.updateBounds();
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onAppear() {
        waveSameColor = null; // to update references
        updateTitleString();
    }


    private void updateTitleString() {
        String string = generateTitleString();
        if (title.string.equals(string)) return;
        setTitle(string);
    }


    private String generateTitleString() {
        CellField cellField = getObjectsLayer().cellField;
        Cell humanStartCell = cellField.getCell(CellType.human_start);
        if (humanStartCell == null) return "-";
        initWavesIfNecessary();
        waveSameColor.apply(humanStartCell, tempList);
        int humanSize = tempList.size();
        Cell enemyStartCell = cellField.getCell(CellType.enemy_start);
        if (enemyStartCell != null && (humanStartCell.color != enemyStartCell.color)) {
            waveSameColor.apply(enemyStartCell, tempList);
            int enemySize = tempList.size();
            return humanSize + " - " + enemySize;
        } else {
            return "" + humanSize;
        }
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
        return MenuRenders.renderCaptureViewElement;
    }
}
