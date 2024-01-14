package com.example.moviapp.Data;

import com.example.moviapp.R;

import java.util.ArrayList;
import java.util.List;

public class Avatar {
    private int avatarResourceId;
    private String avatarName;
    public Avatar(String avatarName, int avatarResourceId){
        this.avatarResourceId = avatarResourceId;
        this.avatarName = avatarName;
    }

    public int getAvatarResourceId() {
        return avatarResourceId;
    }

    public String getAvatarName() {
        return avatarName;
    }

    // Sabit bir avatar listesi oluştur
    public static List<Avatar> getAvatarList() {
        List<Avatar> avatarList = new ArrayList<>();
        avatarList.add(new Avatar("avatar1", R.drawable.avatar1));
        avatarList.add(new Avatar("avatar2", R.drawable.avatar2));
        avatarList.add(new Avatar("avatar3", R.drawable.avatar3));
        avatarList.add(new Avatar("avatar4", R.drawable.avatar4));
        avatarList.add(new Avatar("avatar5", R.drawable.avatar5));
        avatarList.add(new Avatar("avatar6", R.drawable.avatar6));
        avatarList.add(new Avatar("avatar7", R.drawable.avatar7));
        avatarList.add(new Avatar("avatar8", R.drawable.avatar8));

        // Diğer avatarları da ekleyin...
        return avatarList;
    }
}
