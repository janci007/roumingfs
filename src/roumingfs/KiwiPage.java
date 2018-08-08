/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roumingfs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.fusejna.StructFuseFileInfo;
import net.fusejna.StructStat;

/**
 *
 * @author janci
 */
class KiwiPage extends VirtualFile {

    private final String page;
    private final Date date;

    public KiwiPage(String page, Date date) {
        this.page = page;
        this.date = date;
    }

    @Override
    synchronized  public int getattr(StructStat.StatWrapper stat) {
        super.getattr(stat);
        stat.setAllTimesMillis(date.getTime());
        return 0;
    }

    @Override
    synchronized public int open(StructFuseFileInfo.FileInfoWrapper info) {
        if(contents.length == 0){
            try {
                URL url = new URL("http://www.rouming.cz/roumingKiwi.php?page="+page);
                System.out.println("Download "+url.toExternalForm());
                Scanner s = new Scanner(new BufferedInputStream(url.openStream()));
                s.findWithinHorizon("<td class=\"roumingForumMessage\" align=\"left\" colspan=\"2\">", 0);
                s.findWithinHorizon(Pattern.compile("(.*?)</td>", Pattern.MULTILINE | Pattern.DOTALL), 0);
                String html = s.match().group(1);
                contents = html.getBytes("UTF-8");
            } catch (IOException ex) {
                Logger.getLogger(KiwiPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.open(info);
    }
    
    
    
    
    
}
