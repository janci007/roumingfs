/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roumingfs;

import java.io.File;
import java.nio.ByteBuffer;
import net.fusejna.DirectoryFiller;
import net.fusejna.ErrorCodes;
import net.fusejna.StructFuseFileInfo;
import net.fusejna.StructStat;
import net.fusejna.types.TypeMode;

/**
 *
 * @author janci
 */
class VirtualFile implements Node {

    protected byte[] contents = {};
    
    public VirtualFile() {
    }
    
    @Override
    synchronized public int getattr(StructStat.StatWrapper stat) {
        stat.setMode(TypeMode.NodeType.FILE).size(contents.length);
        return 0;
    }
    
    @Override
    synchronized public int read(final ByteBuffer buffer, final long size, final long offset, final StructFuseFileInfo.FileInfoWrapper info)
    {
        long x = Math.max(0, Math.min(Math.min(contents.length - offset, size), buffer.remaining()));
        System.out.println("Read "+offset+" "+size+" "+x);
        if(x>0){
            buffer.put(contents, (int) offset, (int) x); 
            return (int) x;
        }
        return 0;
    }

    @Override
    public Node getNode(String path) {
        return null;
    }

    @Override
    public int readdir(DirectoryFiller filler) {
        return 0;
    }

    @Override
    public int open(StructFuseFileInfo.FileInfoWrapper info) {
        info.direct_io(true);
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
