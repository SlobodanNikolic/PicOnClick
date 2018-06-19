package dto;

public class User extends BaseObject{
	
	protected int id;
	protected String name;
	protected String password;
	protected String email;
	protected String state;
	protected int weekly;
	protected int daily;
	protected boolean blocked;
	protected boolean seller;
	protected boolean activated;
	protected boolean opRequested;
	protected boolean operator;
	protected boolean admin;
	protected boolean pending;

	
	public boolean isOpRequested() {
		return opRequested;
	}

	public void setOpRequested(boolean opRequested) {
		this.opRequested = opRequested;
	}

	public boolean isOperator() {
		return operator;
	}

	public void setOperator(boolean operator) {
		this.operator = operator;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isPending() {
		return pending;
	}

	public void setPending(boolean pending) {
		this.pending = pending;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	
	
	public User(int id, String name, String password, String email, String state, int weekly, int daily,
			boolean blocked, boolean seller, boolean activated, boolean opRequested, boolean operator, boolean admin,
			boolean pending) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.email = email;
		this.state = state;
		this.weekly = weekly;
		this.daily = daily;
		this.blocked = blocked;
		this.seller = seller;
		this.activated = activated;
		this.opRequested = opRequested;
		this.operator = operator;
		this.admin = admin;
		this.pending = pending;
	}

	public User() {
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getWeekly() {
		return weekly;
	}
	public void setWeekly(int weekly) {
		this.weekly = weekly;
	}
	public int getDaily() {
		return daily;
	}
	public void setDaily(int daily) {
		this.daily = daily;
	}
	
	public boolean isBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public boolean isSeller() {
		return seller;
	}
	public void setSeller(boolean seller) {
		this.seller = seller;
	}
	
	
}
