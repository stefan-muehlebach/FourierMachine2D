/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fourierAnim;

import java.util.Iterator;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author stefan
 */
public class FourierCoeffTest {
    
    static FourierCoeffList coeffList = new FourierCoeffList(new FourierCoeff[] {
        new FourierCoeff(0, 5.0, 0.0),
        new FourierCoeff(1, 4.0, 0.0),
        new FourierCoeff(-1, 3.0, 0.0),
        new FourierCoeff(2, 2.0, 0.0),
        new FourierCoeff(-2, 1.0, 0.0),
    });
    
    public static void main(String args[]) {
        FourierCoeffList cl;
        
        try {
            cl = new FourierCoeffList("./cat.csv");
        } catch (FileNotFoundException e) {
            System.out.println(e);
            return;
        } catch (IOException e) {
            System.out.println(e);
            return;
        }
        
        System.out.printf("max freq: %d\n", cl.getMaxFreq());

        System.out.println("order by freq");
        Iterator<FourierCoeff> iter = cl.freqIterator();
        while (iter.hasNext()) {
            FourierCoeff coeff = iter.next();
            System.out.println(coeff);
        }
        
        System.out.println("[-1]: " + cl.get(-1));
    }    
}
