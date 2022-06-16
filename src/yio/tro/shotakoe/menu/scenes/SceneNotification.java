package yio.tro.shotakoe.menu.scenes;

import yio.tro.shotakoe.menu.elements.NotificationElement;

public class SceneNotification extends ModalSceneYio {

    public NotificationElement notificationElement;


    @Override
    public void initialize() {
        double h = 0.05;
        notificationElement = uiFactory.getNotificationElement()
                .setPosition(0, 1 - h, 1, h)
                .setOnTopOfGameView(true);
    }


    @Override
    protected void onAppear() {
        super.onAppear();
        forceElementToTop(notificationElement);
    }


    public void show(String key) {
        show(key, true);
    }


    public void show(String key, boolean autoHide) {
        create();
        notificationElement.setMessage(key);
        if (autoHide) {
            notificationElement.enableAutoHide();
        }
    }
}
