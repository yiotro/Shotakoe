package yio.tro.shotakoe.menu.elements.plot_view;

import yio.tro.shotakoe.Yio;
import yio.tro.shotakoe.menu.MenuControllerYio;
import yio.tro.shotakoe.menu.elements.InterfaceElement;
import yio.tro.shotakoe.menu.menu_renders.MenuRenders;
import yio.tro.shotakoe.menu.menu_renders.RenderInterfaceElement;
import yio.tro.shotakoe.menu.scenes.Scenes;
import yio.tro.shotakoe.stuff.GraphicsYio;
import yio.tro.shotakoe.stuff.RenderableTextYio;
import yio.tro.shotakoe.stuff.factor_yio.*;
import yio.tro.shotakoe.stuff.object_pool.ObjectPoolYio;

import java.util.ArrayList;

public class PlotViewElement extends InterfaceElement<PlotViewElement> {

    public ArrayList<PlotItem> items;
    boolean readyToUpdate;
    public ArrayList<PlotDescView> descViews;
    ObjectPoolYio<PlotDescView> poolDescViews;


    public PlotViewElement(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        items = new ArrayList<>();
        readyToUpdate = false;
        descViews = new ArrayList<>();
        initPools();
    }


    private void initPools() {
        poolDescViews = new ObjectPoolYio<PlotDescView>(descViews) {
            @Override
            public PlotDescView makeNewObject() {
                return new PlotDescView();
            }
        };
    }


    @Override
    protected PlotViewElement getThis() {
        return this;
    }


    @Override
    public void onMove() {
        checkToUpdate();
    }


    private void checkToUpdate() {
        if (!readyToUpdate) return;
        if (appearFactor.getProgress() < 0.5) return;
        readyToUpdate = false;
        applyUpdate();
        if (appearFactor.getProgress() < 1 || parent.getFactor().getProgress() < 1) {
            readyToUpdate = true;
        }
    }


    private void applyUpdate() {
        for (PlotItem plotItem : items) {
            plotItem.update();
        }
        updateDescViews();
    }


    private void updateDescViews() {
        poolDescViews.clearExternalList();
        for (PlotItem plotItem : items) {
            PlotDescView freshObject = poolDescViews.getFreshObject();
            freshObject.setText(plotItem.plotData.description);
            freshObject.setColor(plotItem.color);
            freshObject.setUpwards(plotItem.plotData.upwards);
        }
        float topDelta = 0.02f * GraphicsYio.height;
        float bottomDelta = 0.02f * GraphicsYio.height;
        float incOffset = 0.012f * GraphicsYio.width;
        float rightX = viewPosition.x + viewPosition.width - 0.02f * GraphicsYio.width;
        for (PlotDescView plotDescView : descViews) {
            RenderableTextYio renderableTextYio = plotDescView.renderableTextYio;
            renderableTextYio.position.x = rightX - incOffset - renderableTextYio.width;
            if (plotDescView.upwards) {
                renderableTextYio.position.y = viewPosition.y + bottomDelta + renderableTextYio.height;
                bottomDelta += 3 * incOffset + renderableTextYio.height;
            } else {
                renderableTextYio.position.y = viewPosition.y + viewPosition.height - topDelta;
                topDelta += 3 * incOffset + renderableTextYio.height;
            }
            renderableTextYio.updateBounds();
            renderableTextYio.updateBounds(); // yes, it should be called twice to update last travel distance
            plotDescView.incBounds.setBy(renderableTextYio.bounds);
            plotDescView.incBounds.increase(incOffset);
        }
    }


    @Override
    public void onDestroy() {

    }


    public void addPlotItem(PlotParameters parameters) {
        PlotData plotData = generatePlotData(parameters);
        plotData.name = parameters.name;
        plotData.description = generateDescription(parameters, plotData);
        plotData.upwards = parameters.upwards;
        PlotItem plotItem = new PlotItem(this);
        plotItem.setPlotData(plotData);
        plotItem.setColor(parameters.color);
        items.add(plotItem);
        readyToUpdate = true;
    }


    private String generateDescription(PlotParameters parameters, PlotData plotData) {
        int quantity = plotData.values.size();
        double speed = parameters.speed;
        int scale = (int) (quantity * speed);
        return quantity + " x" + speed + " (" + scale + ")";
    }


    public void clear() {
        items.clear();
    }


    private PlotData generatePlotData(PlotParameters parameters) {
        if (parameters.upwards) {
            return generateUpwardsPlotData(parameters);
        }
        return generateDownwardsPlotData(parameters);
    }


    private PlotData generateUpwardsPlotData(PlotParameters parameters) {
        FactorYio factorYio = new FactorYio();
        factorYio.appear(parameters.movementType, parameters.speed);
        PlotData plotData = new PlotData();
        int c = 0;
        while (factorYio.getProgress() < 1 && c < 1000) {
            c++;
            plotData.values.add(factorYio.getValue());
            factorYio.move();
        }
        plotData.values.add(factorYio.getValue());
        return plotData;
    }


    private PlotData generateDownwardsPlotData(PlotParameters parameters) {
        FactorYio factorYio = new FactorYio();
        factorYio.setValue(1);
        factorYio.stop();
        factorYio.destroy(parameters.movementType, parameters.speed);
        PlotData plotData = new PlotData();
        int c = 0;
        while (factorYio.getProgress() > 0 && c < 1000) {
            c++;
            plotData.values.add(factorYio.getValue());
            factorYio.move();
        }
        plotData.values.add(factorYio.getValue());
        return plotData;
    }


    @Override
    public void onAppear() {
        readyToUpdate = true;
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
        if (isClicked()) {
            onClick();
            return true;
        }
        return false;
    }


    private void onClick() {
        float x = (currentTouch.x - viewPosition.x) / viewPosition.width;
        float y = (currentTouch.y - viewPosition.y) / viewPosition.height;
        String id = "";
        if (y < 1) {
            MbSoftRubberBand.x1 = x;
            MbSoftRubberBand.y1 = y;
            id = "1";
        } else {
            MbSoftRubberBand.x2 = x;
            MbSoftRubberBand.y2 = y;
            id = "2";
        }
        System.out.println("PlotViewElement.onClick, " + id + ": " + Yio.roundUp(x) + ", " + Yio.roundUp(y));
        MbFactoryYio.getInstance().initMap();
        Scenes.researchFactorBehavior.resetParamsList();
        Scenes.researchFactorBehavior.onParamsChanged();
    }


    @Override
    public RenderInterfaceElement getRenderSystem() {
        return MenuRenders.renderPlotViewElement;
    }
}
