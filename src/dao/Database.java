package dao;

import db.DBException;
import domain.DaoObject;

public interface Database {
    public void add(DaoObject object) throws DBException;
    public void remove(long id) throws DBException;
    public boolean contains(long id) throws DBException;
    public boolean contains(String identifier) throws DBException;
    public DaoObject get(long id) throws DBException;
    public long getIDFromString(String identifier) throws DBException;
    public void closeConnection();    
    public void reset() throws DBException;
}
