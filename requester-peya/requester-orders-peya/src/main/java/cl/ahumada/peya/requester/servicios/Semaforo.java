package cl.ahumada.peya.requester.servicios;

import java.util.Vector;

import org.apache.log4j.Logger;

public class Semaforo {

    private int valor;
    private Vector<Thread> lista;
    private static Logger logger = Logger.getLogger(Semaforo.class);
    ////////////////////////////////////////////////////////////////////////////
    public Semaforo() {
        this(1);
    }

    ////////////////////////////////////////////////////////////////////////////
    public Semaforo(int valor) {
        this.valor = valor;
        lista = new Vector<Thread>();
    }

    ////////////////////////////////////////////////////////////////////////////
    synchronized public void espere() {
        opP();
    }

    synchronized public void permiso() {
        opP();
    }

    ////////////////////////////////////////////////////////////////////////////
    synchronized public void forceWait() {
        lista.add(Thread.currentThread());
        logger.debug("forceWait: parte espera [" + Thread.currentThread().getName() + "]. valor=" +
                        valor);
        try {
            wait();
        } catch (InterruptedException e) {
            logger.debug("opP: interrumpido");
        }

        Thread t = (Thread) lista.elementAt(0);
        lista.removeElementAt(0);
        logger.debug("forceWait: continua Thread [" + t.getName() + "]. valor=" +
            valor);
    }
    ////////////////////////////////////////////////////////////////////////////
    synchronized public void continuarCondicionado() {
        Thread esperando;

        if (valor < 0) {
            valor++;
        } else {
            valor++;
            esperando = (Thread) lista.elementAt(0);

            String name = esperando.getName();
            logger.debug("continuarCondicionado: encontro esperando a " + name + " valor=" +
                valor);
            notify();
        }

        logger.debug("notify: Thread [" + Thread.currentThread().getName() +
            "]. valor=" + valor);
    }
    ////////////////////////////////////////////////////////////////////////////

    synchronized private void opP() {
        lista.add(Thread.currentThread());
        valor--;

        if (valor < 0) {
            logger.debug("opP: espera Thread [" +
                Thread.currentThread().getName() + "]. valor=" + valor);

            try {
                wait();
            } catch (InterruptedException e) {
                logger.debug("opP: interrumpido");
            }
        }

        Thread t = (Thread) lista.elementAt(0);
        lista.removeElementAt(0);
        logger.debug("opP: continua Thread [" + t.getName() + "]. valor=" +
            valor);
    }

    ////////////////////////////////////////////////////////////////////////////
    synchronized public void continuar() {
        opV();
    }

    /////////////////////////////////////////////////////////////////////////////
    synchronized private void opV() {
        Thread esperando;

        if (valor < 0) {
            valor++;
            esperando = (Thread) lista.elementAt(0);

            String name = esperando.getName();
            logger.debug("opV: encontro esperando a " + name + " valor=" +
                valor);
            notify();
        } else {
            valor++;
        }

        logger.debug("opV: Thread [" + Thread.currentThread().getName() +
            "]. valor=" + valor);
    }
}
