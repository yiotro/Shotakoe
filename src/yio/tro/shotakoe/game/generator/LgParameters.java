package yio.tro.shotakoe.game.generator;

import yio.tro.shotakoe.game.general.ColorYio;

public class LgParameters {

    public int colorsQuantity;
    public double activeCellsValue;
    public boolean symmetry;
    public double groupsValue;
    public double colorClustersValue;
    public boolean sameStartingCluster;
    public boolean enemy;
    public boolean bomb;


    public LgParameters() {
        applyDefaults();
    }


    public void applyDefaults() {
        setColorsQuantity(ColorYio.values().length);
        setActiveCellsValue(0.9);
        setSymmetry(false);
        setGroupsValue(0.9);
        setColorClustersValue(0.1);
        setSameStartingCluster(false);
        setEnemy(true);
        setBomb(false);
    }


    public void setBy(LgParameters src) {
        colorsQuantity = src.colorsQuantity;
        activeCellsValue = src.activeCellsValue;
        symmetry = src.symmetry;
        groupsValue = src.groupsValue;
        colorClustersValue = src.colorClustersValue;
        sameStartingCluster = src.sameStartingCluster;
        enemy = src.enemy;
        bomb = src.bomb;
    }


    public void setActiveCellsValue(double activeCellsValue) {
        this.activeCellsValue = activeCellsValue;
    }


    public void setColorsQuantity(int colorsQuantity) {
        this.colorsQuantity = colorsQuantity;
    }


    public void setSymmetry(boolean symmetry) {
        this.symmetry = symmetry;
    }


    public void setGroupsValue(double groupsValue) {
        this.groupsValue = groupsValue;
    }


    public void setColorClustersValue(double colorClustersValue) {
        this.colorClustersValue = colorClustersValue;
    }


    public void setSameStartingCluster(boolean sameStartingCluster) {
        this.sameStartingCluster = sameStartingCluster;
    }


    public void setEnemy(boolean enemy) {
        this.enemy = enemy;
    }


    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }
}
