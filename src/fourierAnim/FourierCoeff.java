package fourierAnim;

// Klasse für einen Fourierkoeffizieten. Im Wesentlichen ist darin die
// Frequenz als ganze Zahl und der jeweilige Faktor (komplexe Zahl) enthalten.
//
public class FourierCoeff {

    private int freq;
    private Complex factor;

    // Erstellt einen Koeffizienten mit der angegebenen Frequenz und dem
    // komplexen Faktor.
    //
    public FourierCoeff(int freq, Complex factor) {
        this.freq = freq;
        this.factor = factor;
    }
    
    // Erstellt einen Koeffizieten mit Frequenz und dem Real-/Imaginärteil
    // des Faktors. Das Objekt für die komplexe Zahl wird automatisch
    // erzeugt.
    //
    public FourierCoeff(int freq, double re, double im) {
        this(freq, new Complex(re, im));
    }

    // Liefert die Frequenz als ganze Zahl.
    //
    public int getFreq() {
        return freq;
    }

    // Liefert den Faktor (komplexe Zahl).
    //
    public Complex getFactor() {
        return factor;
    }
    
    // Für eine Darstellung als Zeichenkette.
    //
    public String toString() {
        return String.format("(%d, (%f, %f))", freq, factor.re(), factor.im());
    }
}
