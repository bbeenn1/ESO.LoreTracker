package com.bfixedit.eso;

/**
 * Created by Ben on 9/21/15.
 * http://www.vogella.com/tutorials/AndroidListView/article.html#listadvanced_interactive
 */
public class Model {

    private String name;
    private boolean selected;

    public Model(String name) {
        this.name = name;
        selected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
