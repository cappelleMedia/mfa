package domain;

public enum UpdateState {
    ADDED("added"),
    UPDATED("updated");
    
    private final String displayName;
    
    private UpdateState(String displayName){
        this.displayName = displayName;
    }
    
    public String getDisplayName(){
        return this.displayName;
    }
}
