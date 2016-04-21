package dao;

public enum DAOType {
    MEM("memory"),
    JPA("jpa");
    
    /* TODO add these when ready 
    PGSQL(",postgresql"),
    MYSQL("mysql");
    */
    
    private final String displayname;
    
    private DAOType(String displayname) {
        this.displayname = displayname;
    }
    
    public String getDisplayname() {
        return this.displayname;
    }
    
    public static DAOType getDAOType(String displayname) {
        DAOType typeToGet = DAOType.MEM;
        if(displayname != null && !displayname.trim().isEmpty()) {
            for(DAOType daoType : DAOType.values()){
                if(daoType.getDisplayname().equalsIgnoreCase(displayname)){
                    typeToGet = daoType;
                }
            }
        }
        return typeToGet;
    }
}
