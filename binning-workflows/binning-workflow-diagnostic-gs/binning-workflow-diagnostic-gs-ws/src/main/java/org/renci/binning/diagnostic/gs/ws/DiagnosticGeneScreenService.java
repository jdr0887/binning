package org.renci.binning.diagnostic.gs.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/DiagnosticGeneScreenService/")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface DiagnosticGeneScreenService {

    @POST
    @Path("/submit")
    public Response submit(DiagnosticBinningJobInfo info);

}
