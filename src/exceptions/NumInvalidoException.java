package exceptions;

public class NumInvalidoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public String visor;

    public NumInvalidoException(String msg, String visor) {
        super(msg);
        this.visor = visor;
    }

}