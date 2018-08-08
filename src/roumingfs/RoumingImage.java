/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roumingfs;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.fusejna.ErrorCodes;
import net.fusejna.StructFuseFileInfo.FileInfoWrapper;
import net.fusejna.StructStat;

/**
 *
 * @author janci
 */
class RoumingImage extends VirtualFile {

    private final String name;
    private int length;
    protected int referenceCount = 0;
    protected Date date;
    private WeakReference<byte[]> weakContents = new WeakReference<>(null);

    public RoumingImage(String f, int length, Date date) {
        this.name = f;
        this.length = 0;
        this.date = date;
    }

    @Override
    synchronized public int getattr(StructStat.StatWrapper stat) {
        super.getattr(stat);
        if(length == 0){
            System.out.println("Get size of "+name);
            try{
                URL url = new URL("https://www.rouming.cz/upload/"+name);
                HttpURLConnection c = (HttpURLConnection)url.openConnection();
                c.setRequestMethod("HEAD");
                length = c.getContentLength();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        if(date!=null) stat.setAllTimesMillis(date.getTime());
        stat.size(length);
        return 0;
    }
    
    synchronized public int open(final FileInfoWrapper info){
        referenceCount++;
        byte[] ref = weakContents.get();
        if(ref != null) contents = ref;
        if(contents.length == 0){
            System.out.println("Download "+name);
            try {
                URL url = new URL("https://www.rouming.cz/upload/"+name);
                System.out.println("Downloading "+url.toExternalForm());
                InputStream is = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                is.close();
                this.contents = buffer.toByteArray();
                return super.open(info);
            } catch (IOException ex) {
                Logger.getLogger(RoumingImage.class.getName()).log(Level.SEVERE, null, ex);
            }
            return -ErrorCodes.ENOENT();
        }
        return super.open(info);
    }
    
    @Override
    synchronized public int release(final FileInfoWrapper info){
        referenceCount--;
        if(referenceCount <= 0){
            weakContents = new WeakReference<>(this.contents);
            contents = new byte[0];
            referenceCount = 0;
        }
        return 0;
    }
    
    
    
}
