package modelo;

public class Avenger {
    private final int id;
    private String nombre;
    private String mision;
    private int peligrosidad;
    private double pagoMensual;

    public Avenger(int id, String nombre, String mision, int peligrosidad, double pagoMensual) {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser positivo.");
        this.id = id;
        setNombre(nombre);
        setMision(mision);
        setPeligrosidad(peligrosidad);
        setPagoMensual(pagoMensual);
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getMision() { return mision; }
    public int getPeligrosidad() { return peligrosidad; }
    public double getPagoMensual() { return pagoMensual; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        this.nombre = nombre.trim();
    }

    public void setMision(String mision) {
        if (mision == null || mision.trim().isEmpty())
            throw new IllegalArgumentException("La misión es obligatoria.");
        this.mision = mision.trim();
    }

    public void setPeligrosidad(int peligrosidad) {
        if (peligrosidad < 1 || peligrosidad > 5)
            throw new IllegalArgumentException("La peligrosidad debe estar entre 1 y 5.");
        this.peligrosidad = peligrosidad;
    }

    public void setPagoMensual(double pagoMensual) {
        if (!Double.isFinite(pagoMensual) || pagoMensual < 0)
            throw new IllegalArgumentException("El pago mensual debe ser un número no negativo.");
        this.pagoMensual = pagoMensual;
    }


    public double calcularFondoHeroes() {
        return pagoMensual * 0.08;
    }

    /* Impuesto anual calculado sobre 12 pagos mensuales. */
    public double calcularImpuesto() {
        double ingresoAnual = pagoMensual * 12;
        double impuesto = 0;
        if (ingresoAnual > 50000) impuesto += Math.min(ingresoAnual - 50000, 50000) * 0.10;
        if (ingresoAnual > 100000) impuesto += Math.min(ingresoAnual - 100000, 100000) * 0.20;
        if (ingresoAnual > 200000) impuesto += (ingresoAnual - 200000) * 0.30;
        return impuesto;
    }

    /* Pago mensual tras restar fondo e impuesto anual prorrateado. */
    public double calcularPagoNeto() {
        return pagoMensual - calcularFondoHeroes() - calcularImpuesto() / 12;
    }
}
