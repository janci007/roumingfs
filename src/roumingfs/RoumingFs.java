/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roumingfs;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;
import net.fusejna.DirectoryFiller;
import net.fusejna.ErrorCodes;
import net.fusejna.FuseException;
import net.fusejna.StructFuseFileInfo;
import net.fusejna.StructStat;
import net.fusejna.util.FuseFilesystemAdapterFull;

/**
 *
 * @author janci
 */
public class RoumingFs extends FuseFilesystemAdapterFull {
    
    protected Directory root = new RootDirectory();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FuseException {
        args = new String[]{"./mnt"};
        if(args.length != 1){
            System.err.println("Usage: java -jar RoumingFs-bundled.jar <mountpoint>");
            System.err.println("    <mountpoint>  -  path to mount directory");
            System.err.println("    If does not exists, it will be created");
        }
        if(!new File(args[0]).exists()) new File(args[0]).mkdir();
        new RoumingFs().log(false).mount(args[0]);
    }
    
    @Override
    public int getattr(final String path, final StructStat.StatWrapper stat)
    {
        Node n = root.getNode(path);
        if(n!=null) return n.getattr(stat);
        return -ErrorCodes.ENOENT();
    }
    
    @Override
    public int readdir(final String path, final DirectoryFiller filler) {
        Node n = root.getNode(path);
        if(n!=null) return n.readdir(filler);
        return 0;
    }

    @Override
    public int read(String path, ByteBuffer buffer, long size, long offset, StructFuseFileInfo.FileInfoWrapper info) {
        Node n = root.getNode(path);
        if(n!=null) return n.read(buffer, size, offset, info);
        return 0;
    }

    @Override
    public int open(String path, StructFuseFileInfo.FileInfoWrapper info) {
        Node n = root.getNode(path);
        if(n!=null) return n.open(info);
        return super.open(path, info); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int release(String path, StructFuseFileInfo.FileInfoWrapper info) {
        Node n = root.getNode(path);
        if(n!=null) return n.release(info);
        return super.release(path, info); //To change body of generated methods, choose Tools | Templates.
    }   

    @Override
    public int fgetattr(String path, StructStat.StatWrapper stat, StructFuseFileInfo.FileInfoWrapper info) {
        Node n = root.getNode(path);
        if(n!=null) return n.fgetattr(stat, info);
        return super.fgetattr(path, stat, info); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
