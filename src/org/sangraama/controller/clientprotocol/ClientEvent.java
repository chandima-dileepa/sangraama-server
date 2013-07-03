package org.sangraama.controller.clientprotocol;

public class ClientEvent {
    private String type;
    private long userID;
    private float x;
    private float y;
    private float v_x;
    private float v_y;
    private float v_a;
    
      
    public float getV_a() {
		return v_a;
	}
	public void setV_a(float v_a) {
		this.v_a = v_a;
	}
	public String getType() {
        return type;
    }
    public long getUserID() {
        return userID;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getV_x() {
        return v_x;
    }
    public float getV_y() {
        return v_y;
    }
    
}
