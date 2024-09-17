package fourierAnim;

// Eine Klasse für komplexe Zahlen - so weit ich sie im Rahmen des Projektes
// 'FourierMachine' benötige.
//
public class Complex {

    private double re, im;

    // Erstellt eine komplexe Zahl, deren Real- und Imaginärteil beide
    // 0.0 sind.
    //
    public Complex() {
        this.re = 0.0;
        this.im = 0.0;
    }

    // Erstellt eine komplexe Zahl mit den angegebenen Werten für Real-
    // und Imaginärteil.
    //
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    // Erstellt eine komplexe Zahl mit den gleichen Werte wie die im
    // Konstruktur angegebene Zahl.
    //
    public Complex(Complex c) {
        this.re = c.re;
        this.im = c.im;
    }

    // Liefert den Realteil.
    //
    public double re() {
        return re;
    }

    // Liefert den Imaginärteil.
    //
    public double im() {
        return im;
    }

    // Liefert den Betrag.
    //
    public double abs() {
        return Math.sqrt(re * re + im * im);
    }

    // Liefert das Argument.
    //
    public double arg() {
        return Math.atan2(im, re);
    }
    
    // Für eine passende Darstellung - falls man die Zahl mal ausgeben
    // möchte.
    //
    public String toString() {
        return String.format("(%f, %f)", re, im);
    }
}
