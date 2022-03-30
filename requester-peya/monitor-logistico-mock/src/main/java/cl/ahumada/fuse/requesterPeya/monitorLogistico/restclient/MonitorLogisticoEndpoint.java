package cl.ahumada.fuse.requesterPeya.monitorLogistico.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public interface MonitorLogisticoEndpoint {

	@GET
	@Consumes(MediaType.APPLICATION_JSON+";charset=UTF-8")
	@Produces(MediaType.APPLICATION_JSON+";charset=UTF-8")
    @Path("/peyaRequester?numeroOrden=00000000&confirma=X")
	public Response SendConfirmaRechaza();
}
