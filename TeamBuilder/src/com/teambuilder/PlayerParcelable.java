package com.teambuilder;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayerParcelable implements Parcelable {

	public static final String label = "Player";
	Player player;
	
	public PlayerParcelable(Player player) {
		this.player = player;
	}
	
	private PlayerParcelable(Parcel in) {
        player = (Player)in.readValue(Player.class.getClassLoader());
    }
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeValue(player);

	}

	public static final Parcelable.Creator<PlayerParcelable> CREATOR = new Parcelable.Creator<PlayerParcelable>() {
        public PlayerParcelable createFromParcel(Parcel in) {
            return new PlayerParcelable(in);
        }

        public PlayerParcelable[] newArray(int size) {
            return new PlayerParcelable[size];
        }
    };

}
