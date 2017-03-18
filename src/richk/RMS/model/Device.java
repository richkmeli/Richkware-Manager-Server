package richk.RMS.model;

public class Device {
	private int ID;
	private String name;
	private String IP;
	private Boolean online;
	private String lastConnection;

	public Device(int iD, String name, String iP, Boolean online, String lastConnection) {
		super();
		ID = iD;
		this.name = name;
		IP = iP;
		this.online = online;
		this.lastConnection = lastConnection;
	}

	public int getID() {
		return ID;
	}

	public String getIP() {
		return IP;
	}

	public String getLastConnection() {
		return lastConnection;
	}

	public String getName() {
		return name;
	}

	public Boolean getOnline() {
		return online;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public void setLastConnection(String lastConnection) {
		this.lastConnection = lastConnection;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOnline(Boolean online) {
		this.online = online;
	}

}
