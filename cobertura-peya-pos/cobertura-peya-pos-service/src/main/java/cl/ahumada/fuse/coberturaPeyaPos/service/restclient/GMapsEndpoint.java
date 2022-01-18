package cl.ahumada.fuse.coberturaPeyaPos.service.restclient;

import javax.ws.rs.core.MediaType;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.gmaps.GMapsResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

public interface GMapsEndpoint {

	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON )
	@Produces({ MediaType.APPLICATION_JSON })
	public GMapsResponse getCoordenadas();
	
}
