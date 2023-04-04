package cl.ahumada.fuse.descuentos.restclient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cl.ahumada.fuse.descuentos.api.resources.json.BalanceInquiryRequest;
import cl.ahumada.fuse.descuentos.api.resources.json.BalanceInquiryResponse;

public interface BalanceInquiryEndpoint {

	@POST
	@Path("/balanceInquiry")
	@Consumes(MediaType.APPLICATION_JSON )
	@Produces({ MediaType.APPLICATION_JSON })
	public BalanceInquiryResponse getBalanceInquiry(BalanceInquiryRequest request);
}
