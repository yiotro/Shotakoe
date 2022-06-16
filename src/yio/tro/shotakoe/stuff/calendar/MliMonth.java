package yio.tro.shotakoe.stuff.calendar;

import yio.tro.shotakoe.game.general.ColorYio;

public class MliMonth {

    public int year;
    public int monthIndex;
    public int daysQuantity;
    public ColorYio color;
    public String nameKey;


    public void setValues(int year, int monthIndex) {
        this.year = year;
        this.monthIndex = monthIndex;
        updateNameKey();
        updateColor();
        updateDaysQuantity();
    }


    private void updateDaysQuantity() {
        daysQuantity = CalendarManager.getInstance().getDaysQuantity(year, monthIndex);
    }


    private void updateColor() {
        ColorYio[] values = ColorYio.values();
        int path = 12 * (year - 2019) + monthIndex - 1;
        int index = Math.max(0, path % values.length);
        color = values[index];
    }


    void updateNameKey() {
        nameKey = getMonthNameKey(monthIndex);
    }


    public static String getMonthNameKey(int monthIndex) {
        switch (monthIndex) {
            default:
                return "unknown";
            case 1:
                return "january";
            case 2:
                return "february";
            case 3:
                return "march";
            case 4:
                return "april";
            case 5:
                return "may";
            case 6:
                return "june";
            case 7:
                return "july";
            case 8:
                return "august";
            case 9:
                return "september";
            case 10:
                return "october";
            case 11:
                return "november";
            case 12:
                return "december";
        }
    }

}
