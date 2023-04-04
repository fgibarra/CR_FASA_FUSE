package cl.ahumada.fuse.descuentos.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.ahumada.fuse.descuentos.api.resources.json.CalculateDiscountRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.CalculateDiscountResponse;

public interface CalculateDiscountEndpoint {

	@POST
	@Path("/calculateDiscountsV2")
	@Consumes(MediaType.APPLICATION_JSON )
	@Produces({ MediaType.APPLICATION_JSON })
	public CalculateDiscountResponse getCalculateDiscount(CalculateDiscountRequest request);
}
