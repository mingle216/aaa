package com.wisedu.amp.card.sys.seumyfavoritesx.model;

import java.util.List;

public class MyFavouritesServiceitemResponse extends AmpBaseResponse{

    private List<UserFavoriteServiceItems> data;

    public List<UserFavoriteServiceItems> getData() {
        return data;
    }

    public void setData(List<UserFavoriteServiceItems> data) {
        this.data = data;
    }
}
