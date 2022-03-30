package cl.ahumada.fuse.requesterPeya.monitorLogistico.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;

public class Espera implements Processor {

    @PropertyInject(value = "tiempo", defaultValue="1")
    private String tiempo;

	@Override
	public void process(Exchange exchange) throws Exception {
		Long waittime = Long.valueOf(getTiempo()) * 1000l;
		
		Thread.sleep(waittime);
	}

	public String getTiempo() {
		return tiempo;
	}

	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

}
