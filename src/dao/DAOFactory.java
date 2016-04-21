package dao;

import dao.newsfeed.NewsfeedDAO;
import dao.newsfeed.NewsfeedMemDB;
import dao.newsfeed.NewsfeedRelDB;
import dao.poll.PollDAO;
import dao.poll.PollMemDB;
import dao.poll.PollRelDB;
import dao.user.UserDAO;
import dao.user.UserMemDB;
import dao.user.UserRelDB;
import db.DBException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class DAOFactory {
    private volatile static DAOFactory instance = null;
    private static Set<String> supportedTypes;
    
    private DAOFactory() {
        supportedTypes = new HashSet<>();
    }
    
    public static DAOFactory getInstance() {
        if(instance == null) {
            synchronized(DAOFactory.class) {
                if(instance == null) {
                    instance = new DAOFactory();
                    
                }
            }
        }
        setSupportedTypes();
        return instance;
    }
    
    public static Set<String> getSupportedTypes() {
        return supportedTypes;
    }
    
    private static void setSupportedTypes(){
        for(DAOType type : DAOType.values()){
            supportedTypes.add(type.getDisplayname());
        }
    }
    
    public PollDAO getPollDB(Properties props) throws DBException {
        PollDAO database = null;
        DAOType type;
        String dbName;
        if(!props.containsKey("dbtype.poll")){
            throw new DBException("The option database type was not found for pollDB!");
        }
         if(!props.containsKey("dbname.poll")) {
            throw new DBException("The option");
        }
        String typeString = props.getProperty("dbtype.poll");
        if(!supportedTypes.contains(typeString)){
            throw new DBException("The database type '"+typeString+"' is not (yet) supported");
        }
        type = DAOType.getDAOType(typeString);
        dbName = props.getProperty("dbname.poll");
        switch(type){
            case MEM :
                database = new PollMemDB("Poll memory db");
                break;
            case JPA: 
                database = new PollRelDB(dbName);
                break;
            // TODO Add other cases
            default :
                throw new DBException("Somthing went wrong while getting the poll database");
        }
        return database;
    }
    
    public NewsfeedDAO getNewsfeedDB(Properties props) throws DBException {
        NewsfeedDAO database = null;
        DAOType type;
        String dbName;
        if(!props.containsKey("dbtype.newsfeed")){
            throw new DBException("The option database type was not found for newsfeedDB!");
        }
        if(!props.containsKey("dbname.newsfeed")) {
            throw new DBException("The option");
        }
        String typeString = props.getProperty("dbtype.newsfeed");
        if(!supportedTypes.contains(typeString)){
            throw new DBException("The database type '"+typeString+"' is not (yet) supported");
        }
        type = DAOType.getDAOType(typeString);
        dbName = props.getProperty("dbname.newsfeed");
        switch(type){
            case MEM :
                database = new NewsfeedMemDB("newfeed memory db");
                break;
            case JPA :
                database = new NewsfeedRelDB(dbName);
                break;
            // TODO Add other cases
            default :
                throw new DBException("Somthing went wrong while getting the newsfeed database");
        }
        return database;
    }
    
    public UserDAO getUserDB(Properties props) throws DBException {
        UserDAO database = null;
        DAOType type;
        String dbName;
        if(!props.containsKey("dbtype.user")){
            throw new DBException("The option database type was not found for UserDB!");
        }
        if(!props.containsKey("dbname.user")) {
            throw new DBException("The option");
        }
        String typeString = props.getProperty("dbtype.user");
        if(!supportedTypes.contains(typeString)){
            throw new DBException("The database type '"+typeString+"' is not (yet) supported");
        }
        type = DAOType.getDAOType(typeString);
        dbName = props.getProperty("dbname.user");
        switch(type){
            case MEM :
                database = new UserMemDB();
                break;
            case JPA :
                database = new UserRelDB(dbName);
                break;
            // TODO Add other cases
            default :
                throw new DBException("Somthing went wrong while getting the User database");
        }
        return database;
    }
       
}
