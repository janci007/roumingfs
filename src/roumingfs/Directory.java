/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roumingfs;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.fusejna.DirectoryFiller;
import net.fusejna.ErrorCodes;
import net.fusejna.StructFuseFileInfo;
import net.fusejna.StructStat;
import net.fusejna.types.TypeMode;

/**
 *
 * @author janci
 */
public class Directory implements Node {
    
    protected Map<String, Node> staticNodes = new HashMap<>();
    
    @Override
    public Node getNode(String path) {        
        String[] parts = path.split(File.separator, 3);
        switch (parts.length) {
            case 3:
                if(staticNodes.containsKey(parts[1])){
                    return staticNodes.get(parts[1]).getNode(File.separator + parts[2]);
                }   break;
            case 2:
                if(parts[1].isEmpty()) return this;
                return staticNodes.get(parts[1]);
            case 1:
                return this;
        }
        return null;
    }

    @Override
    public int readdir(DirectoryFiller filler) {
        filler.add(staticNodes.keySet());
        return 0;
    }

    @Override
    public int getattr(StructStat.StatWrapper stat) {
        stat.setMode(TypeMode.NodeType.DIRECTORY);
        return 0;
    }    

    @Override
    public int read(ByteBuffer buffer, long size, long offset, StructFuseFileInfo.FileInfoWrapper info) {
        return 0;
    }

    @Override
    public int open(StructFuseFileInfo.FileInfoWrapper info) {
        return 0;
    }

    @Override
    public int release(StructFuseFileInfo.FileInfoWrapper info) {
        return 0;
    }

    @Override
    public int fgetattr(StructStat.StatWrapper stat, StructFuseFileInfo.FileInfoWrapper info) {
        return getattr(stat);
    }
    
}
