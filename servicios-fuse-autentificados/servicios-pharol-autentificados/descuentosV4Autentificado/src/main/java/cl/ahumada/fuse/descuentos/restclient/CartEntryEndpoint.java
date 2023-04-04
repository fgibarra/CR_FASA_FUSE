package cl.ahumada.fuse.descuentos.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.ahumada.fuse.descuentos.api.resources.json.CartEntryRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.CartEntryResponse;


public interface CartEntryEndpoint {

	@POST
	@Path("/cartEntry")
	@Consumes(MediaType.APPLICATION_JSON )
	@Produces({ MediaType.APPLICATION_JSON })
	public CartEntryResponse getCartEntry(CartEntryRequest request);
}
