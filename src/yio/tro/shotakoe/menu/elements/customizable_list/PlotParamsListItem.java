package yio.tro.shotakoe.menu.elements.customizable_list;

import yio.tro.shotakoe.menu.elements.plot_view.PlotParameters;

public class PlotParamsListItem extends ScrollListItem {

    public PlotParameters parameters;


    public void setParameters(PlotParameters parameters) {
        this.parameters = parameters;
        setTitle(parameters.name);
        setCentered(true);
        setColored(false);
    }
}
