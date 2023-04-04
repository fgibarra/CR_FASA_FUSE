package cl.ahumada.fuse.coberturaPeyaPos.service.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.CoberturaRequest;
import cl.ahumada.fuse.coberturaPeyaPos.service.json.peya.CoberturaResponse;

public interface CoberturaPeyaEndpoint {

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON )
	@Produces({ MediaType.APPLICATION_JSON })
	public CoberturaResponse getCobertura(CoberturaRequest request);
}
