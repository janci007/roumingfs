/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roumingfs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.fusejna.StructStat;

/**
 *
 * @author janci
 */
public class ForumThread extends CachedDirectory {

    private final String id;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("d.M.y H:m");

    ForumThread(String id) {
        this.id = id;
    }

    @Override
    protected void reload() {
        staticNodes.clear();
        try {
            System.out.println("Download forum thread");
            URL url = new URL("http://www.rouming.cz/roumingForum.php?thread="+id);
            Scanner s = new Scanner(new BufferedInputStream(url.openStream()));
            s.findWithinHorizon("class=\"roumingForum\"", 0);
            while(null != s.findWithinHorizon(Pattern.compile("<a name=\"([0-9]*)\"></a>"), 0)){
                String id = s.match().group(1);
                s.findWithinHorizon(Pattern.compile("uživateli\">([^<]*)<\\/a>"), 0);
                String user = s.match().group(1);
                s.findWithinHorizon(Pattern.compile("<font size=\"-1\">\\(([0-9.: ]*)\\)</font>"), 0);
                String dateStr = s.match().group(1);
                Date date = dateFormat.parse(dateStr);
                s.findWithinHorizon("roumingForumMessage\">", 0);
                s.findWithinHorizon("(.*)?</td>", 0);
                String text = s.match().group(1).trim();
                staticNodes.put(id+"-"+user+".txt", new VirtualFile(){
                    {
                        contents = text.getBytes("UTF-8");
                    }

                    @Override
                    public int getattr(StructStat.StatWrapper stat) {
                        super.getattr(stat); //To change body of generated methods, choose Tools | Templates.
                        stat.setAllTimesMillis(date.getTime());
                        return 0;
                    }
                    
                });
            }
        } catch (IOException|ParseException ex) {
            Logger.getLogger(ImageListDirectory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
}
