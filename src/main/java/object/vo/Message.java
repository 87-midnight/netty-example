package object.vo;

import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	private int sn;
	private String msg;
	public int getSn() {
		return sn;
	}
	public void setSn(int sn) {
		this.sn = sn;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "Message:[sn:"+this.sn+",msg:"+this.msg+"]";
	}
	
}
