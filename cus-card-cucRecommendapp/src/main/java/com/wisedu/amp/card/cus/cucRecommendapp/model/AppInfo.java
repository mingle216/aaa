package com.wisedu.amp.card.cus.cucRecommendapp.model;

import java.io.Serializable;

public class AppInfo implements Serializable {
    private boolean favorite;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
