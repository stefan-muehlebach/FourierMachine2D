package fourierAnim;

import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.*;

// Diese Klasse schliesslich dient der Verwaltung einer ganzen Reihe von
// Fourierkoeffizienten.
//
public class FourierCoeffList {

    private FourierCoeff[] coeff;
    private int maxFreq;

    // As we need to sort an array according to the absolute value of the
    // fourier coefficient (the complex factor), we need a separate
    // comparator.
    //
    private class CompAbs
            implements Comparator<FourierCoeff> {

        public int compare(FourierCoeff c1, FourierCoeff c2) {
            if (c1.getFactor().abs() < c2.getFactor().abs()) {
                return -1;
            }
            if (c1.getFactor().abs() > c2.getFactor().abs()) {
                return +1;
            }
            return 0;
        }
    }

    private class CompFreq
            implements Comparator<FourierCoeff> {

        public int compare(FourierCoeff c1, FourierCoeff c2) {
            if (Math.abs(c1.getFreq()) < Math.abs(c2.getFreq())) {
                return -1;
            }
            if (Math.abs(c1.getFreq()) > Math.abs(c2.getFreq())) {
                return +1;
            }
            if (c1.getFreq() > c2.getFreq()) {
                return -1;
            }
            if (c1.getFreq() < c2.getFreq()) {
                return +1;
            }
            return 0;
        }
    }

    public Iterator<FourierCoeff> absIterator() {
        List<FourierCoeff> list = Arrays.asList(coeff.clone());
        list.sort(new CompAbs().reversed());
        return list.iterator();
    }

    public Iterator<FourierCoeff> freqIterator() {
        List<FourierCoeff> list = Arrays.asList(coeff.clone());
        list.sort(new CompFreq());
        return list.iterator();
    }

    // Erstellt eine Koeffizientenliste, welche Koeffizienten bis zur
    // Frequenz 'maxFreq' aufnehmen kann.
    //
    public FourierCoeffList(int maxFreq) {
        this.maxFreq = maxFreq;
        int len = (2 * maxFreq) + 1;
        this.coeff = new FourierCoeff[len];
    }

    // Erstellt eine Koeffizientenliste anhand eines Arrays von Koeffizienten
    // 
    public FourierCoeffList(FourierCoeff[] coeffArray) {
        this.coeff = new FourierCoeff[coeffArray.length];
        this.maxFreq = (coeffArray.length - 1) / 2;
        for (FourierCoeff c : coeffArray) {
            int i = maxFreq + c.getFreq();
            this.coeff[i] = c;
        }
    }

    // Liest die Koeffizienten aus einem File, welches folgendes Format
    // haben muss:
    //
    //     <m>
    //     <f>, <re>, <im>
    //     <f>, <re>, <im>
    //     ..
    //
    // n    : die maximale Frequenz
    // f    : die Frequenz der aktuellen Zeile
    // re/im: Real- und Imaginärteil des Koeffizienten
    //
    public FourierCoeffList(String fileName)
            throws FileNotFoundException, IOException {
        
        coeff = new FourierCoeff[1];
        maxFreq = 0;
        String skipReg = "^ *(#.*)?$";
        String freqReg = "^ *([0-9]+) *$";
        String coeffReg = "^ *([-+]?[0-9]+), *([-+]?[0-9\\.]+(?:e-?[0-9]{2})?), *([-+]?[0-9\\.e]+(?:e-?[0-9]{2})?) *$";
        Pattern freqPat = Pattern.compile(freqReg);
        Pattern coeffPat = Pattern.compile(coeffReg);
        Matcher matcher;
        
        int state = 0;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(fileName))
        );
        String line;
        while ((line = in.readLine()) != null) {
            if (Pattern.matches(skipReg, line)) {
//                System.out.println("SKIP: " + line);
                continue;
            }
            switch (state) {
                case 0:
                    matcher = freqPat.matcher(line);
                    if (matcher.matches()) {
                        maxFreq = Integer.parseInt(matcher.group(1));
                        coeff = new FourierCoeff[2 * maxFreq + 1];
                        state = 1;
//                        System.out.println("OK: " + line);
                    } else {
                        System.out.println("NOK: " + line);
                    }
                    break;
                case 1:
                    matcher = coeffPat.matcher(line);
                    if (matcher.matches()) {
                        int freq = Integer.parseInt(matcher.group(1));
                        double re = Double.parseDouble(matcher.group(2));
                        double im = Double.parseDouble(matcher.group(3));
                        set(freq, new Complex(re, im));
//                        System.out.println("OK: " + line);
                    } else {
                        System.out.println("NOK: " + line);
                    }
                    break;
            }
        }
        in.close();
    }

    public Complex get(int freq) {
        int i = maxFreq + freq;
        return coeff[i].getFactor();
    }

    public Complex set(int freq, Complex c) {
        int i = maxFreq + freq;
        coeff[i] = new FourierCoeff(freq, c);
        return c;
    }

    // Returns the maximum allowed frequency as a positive integer.
    //
    public int getMaxFreq() {
        return maxFreq;
    }

}
