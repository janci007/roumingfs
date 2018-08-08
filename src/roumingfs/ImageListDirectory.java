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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.fusejna.DirectoryFiller;

/**
 *
 * @author janci
 */
class ImageListDirectory extends CachedDirectory {
    
    @Override
    protected void reload() {
        Map<String, Node> oldNodes = new HashMap<>();
        oldNodes.putAll(staticNodes);
        staticNodes.clear();
        try {
            System.out.println("Download file list");
            URL url = new URL("http://www.rouming.cz/");
            Scanner s = new Scanner(new BufferedInputStream(url.openStream()));
            s.findWithinHorizon("<table width=\"100%\" border=\"0\">", 0);
            while(null != s.findWithinHorizon("<tr>", 0)){
                try{
                    s.findWithinHorizon(Pattern.compile("<td align=\"right\">([0-9.:]*)</td>"), 0);
                    String dateStr = s.match().group(1);
                    Date date = null;
                    try{ date = new SimpleDateFormat("d.M.y").parse(dateStr); }catch(Exception e){}
                    try{
                        date = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS).plus(new SimpleDateFormat("H:m").parse(dateStr).getTime(), ChronoUnit.MILLIS));
                     }catch(Exception e){}
                    s.findWithinHorizon(Pattern.compile("\"https://www\\.rouming\\.cz/roumingShow\\.php\\?file=([^\"]*)\""), 0);
                    String f = s.match().group(1);
                    if(oldNodes.containsKey(f)){
                        staticNodes.put(f, oldNodes.get(f));
                    }else{
                        staticNodes.put(f, new RoumingImage(f, 1024*1024*666, date));
                    }
                }catch(IllegalStateException e){
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImageListDirectory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageListDirectory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    
}
