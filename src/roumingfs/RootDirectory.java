/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roumingfs;

/**
 *
 * @author janci
 */
public class RootDirectory extends Directory{

    public RootDirectory() {
        staticNodes.put("Rouming", new ImageListDirectory());
        staticNodes.put("Fórum", new ForumIndex());
        staticNodes.put("Kiwipedia", new KiwiIndex());
    }
    
}
