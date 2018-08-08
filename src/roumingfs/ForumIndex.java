/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roumingfs;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author janci
 */
public class ForumIndex extends CachedDirectory {
   
    
    @Override
    protected void reload() {
        staticNodes.clear();
        try {
            System.out.println("Download forum index");
            URL url = new URL("http://www.rouming.cz/roumingForum.php");
            Scanner s = new Scanner(new BufferedInputStream(url.openStream()));
            while(null != s.findWithinHorizon(Pattern.compile("<td class=\"roumingForumTitle\"><a href=\"\\?thread=([0-9]*)&amp;[^\"]*\">(.*)<\\/a>"), 0)){
                String f = s.match().group(2);
                String id = s.match().group(1);
                staticNodes.put(id+"-"+f, new ForumThread(id));
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageListDirectory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
