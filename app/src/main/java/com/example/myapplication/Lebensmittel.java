package com.example.myapplication;
import java.util.*;

public class Lebensmittel {
    String Bar;
    String Nam;
    String Inh;
    int Anz;
    String Min;
    String Max;
    String VPE;
    String Ene;
public Lebensmittel(String Barcode, String Name, String Inhalt, int Anzahl, String MhdMin, String MhdMax, String VPE1, String Energie)
{
     Bar = Barcode;
     Nam = Name;
     Inh = Inhalt;
     Anz = Anzahl;
     Min = MhdMin;
     Max = MhdMax;
     VPE = VPE1;
     Ene = Energie;
}
}
