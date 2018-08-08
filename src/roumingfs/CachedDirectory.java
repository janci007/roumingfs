/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roumingfs;

import java.util.Date;
import net.fusejna.DirectoryFiller;

/**
 *
 * @author janci
 */
abstract public class CachedDirectory extends Directory {
 
    protected final long cacheExpiration = 10000;
    protected long cacheTimestamp = 0;
    
    @Override
    public int readdir(DirectoryFiller filler) {
        reloadIfExpired();
        return super.readdir(filler);
    }
    
    protected void reloadIfExpired(){
        if(cacheTimestamp + cacheExpiration < new Date().getTime()){
            reload();
            cacheTimestamp = new Date().getTime();
        }
    }

    @Override
    public Node getNode(String path) {
        reloadIfExpired();
        return super.getNode(path);
    }
    
    protected abstract void reload();    
}
