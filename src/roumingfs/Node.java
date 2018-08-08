/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roumingfs;

import java.nio.ByteBuffer;
import net.fusejna.DirectoryFiller;
import net.fusejna.StructFuseFileInfo;
import net.fusejna.StructStat;

/**
 *
 * @author janci
 */
public interface Node {
    public int getattr(final StructStat.StatWrapper stat);
    public int read(final ByteBuffer buffer, final long size, final long offset, final StructFuseFileInfo.FileInfoWrapper info);
    public Node getNode(String path);
    public int readdir(DirectoryFiller filler);
    public int open(final StructFuseFileInfo.FileInfoWrapper info);
    public int release(final StructFuseFileInfo.FileInfoWrapper info);
    public int fgetattr(StructStat.StatWrapper stat, StructFuseFileInfo.FileInfoWrapper info);
}
