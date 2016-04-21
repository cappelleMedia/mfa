package service;

public class MenuItem {
    private String action;
    private String description;
    private String icon="";
    private boolean authenticationNeeded =false;
    
    public MenuItem(String action, String desciption) {
        super();
        this.setAction(action);
        this.setDescription(desciption);
    }
    public MenuItem(String action, String desciption, boolean authenticationNeeded) {
        this(action, desciption);
        this.setAuthenticationNeeded(authenticationNeeded);
    }


    public MenuItem(String action, String desciption, boolean authenticationNeeded, String icon) {
        this(action, desciption, authenticationNeeded);
        this.setIcon(icon);
    }


    public String getIcon(){
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desciption) {
        this.description = desciption;
    }

    public boolean isAuthenticationNeeded() {
        return authenticationNeeded;
    }

    public void setAuthenticationNeeded(boolean authenticationNeeded) {
        this.authenticationNeeded = authenticationNeeded;
    }
}

