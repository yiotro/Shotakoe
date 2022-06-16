package yio.tro.shotakoe.menu.scenes;

import yio.tro.shotakoe.menu.elements.BackgroundYio;
import yio.tro.shotakoe.menu.reactions.Reaction;

public class TempButtonData {

    public String key;
    public BackgroundYio backgroundYio;
    public Reaction reaction;


    public TempButtonData(String key, BackgroundYio backgroundYio, Reaction reaction) {
        this.key = key;
        this.backgroundYio = backgroundYio;
        this.reaction = reaction;
    }

}
