package dao;

import db.DBException;
import domain.DaoObject;
import java.util.Random;
import java.util.Set;

public class IdAssigner {

    private long idGenerator() {
        Random rand = new Random();
        long id = rand.nextLong();
        if(id<=0) {
        	id*=(-1);
        }
        return id;
    }
    
    private boolean idInUse(long id, Set<Long> assignedIds) {
        boolean inUse = false;
        if(assignedIds.contains(id)){
            inUse=true;
        }
        return inUse;
    }
    
    public void assignID(DaoObject daoObj, Set<Long> assignedIds) throws DBException{ 
        int numberOfTries = 0;
        while(daoObj.getId() == 0){
            long id = this.idGenerator();
            if(!this.idInUse(id, assignedIds)){
                daoObj.setId(id);
            }
            numberOfTries++;
            if(numberOfTries==10){
                throw new DBException("failed to assign an id to the newsfeed item");
            }
        }
    }
    
}
