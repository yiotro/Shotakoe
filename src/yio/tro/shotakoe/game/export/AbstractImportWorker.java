package yio.tro.shotakoe.game.export;

import yio.tro.shotakoe.game.general.ObjectsLayer;

public abstract class AbstractImportWorker {

    protected ObjectsLayer objectsLayer;
    protected String source;
    protected String levelCode;
    protected String sectionName;


    public AbstractImportWorker(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
    }


    public void perform(String levelCode) {
        perform(levelCode, getDefaultSectionName());
    }


    public void perform(String levelCode, String sectionName) {
        this.levelCode = levelCode;
        this.sectionName = sectionName;
        source = getSection(levelCode, sectionName);
        if (source == null) return;
        try {
            apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected abstract String getDefaultSectionName();


    protected abstract void apply();


    protected String getSection(String source, String name) {
        int sectionIndex = source.indexOf("#" + name);
        if (sectionIndex == -1) return null;
        int colonIndex = source.indexOf(":", sectionIndex);
        int hashIndex = source.indexOf("#", colonIndex);
        if (hashIndex - colonIndex < 2) return null;
        return source.substring(colonIndex + 1, hashIndex);
    }
}
