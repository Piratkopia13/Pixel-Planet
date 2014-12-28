package net.brainstorm_labs.spacegame;

public class NetworkFormat {
	
	int id;
	Object data;
	boolean privat;
    String data_name;
	
	public NetworkFormat(){}
	public NetworkFormat(String data_name, Object data, int id, boolean privat){
        this.data_name = data_name;
		this.data = data;
		this.id = id;
		this.privat = privat;
	}
	
}
