/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roumingfs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 *
 * @author janci
 */
public class KiwiIndex extends CachedDirectory {
   
    
    @Override
    protected void reload() {
        staticNodes.clear();
        try {
            System.out.println("Download kiwi index");
            URL url = new URL("http://www.rouming.cz/roumingKiwiList.php");
            Scanner s = new Scanner(new BufferedInputStream(url.openStream()));
            while(null != s.findWithinHorizon(Pattern.compile("<a href=\"roumingKiwi\\.php\\?page=(.*?)\">(.*?)</a>"), 0)){
                MatchResult r = s.match();
                String page = r.group(1);
                String name = r.group(2);
                s.findWithinHorizon(Pattern.compile("([0-9.]+)&nbsp;([0-9:]+)"), 0);
                r = s.match();
                String dateStr = r.group(1)+" "+r.group(2);
                Date date = new Date();
                try {
                    date = new SimpleDateFormat("d.M.y H:m:s").parse(dateStr);
                } catch (ParseException ex) {
                    Logger.getLogger(KiwiIndex.class.getName()).log(Level.SEVERE, null, ex);
                }
                staticNodes.put(name+".html", new KiwiPage(page, date));
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageListDirectory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
