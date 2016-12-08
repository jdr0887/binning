package org.renci.binning.diagnostic.ncgenes.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/DiagnosticNCGenesService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface DiagnosticNCGenesService {

    @POST
    @Path("/submit/")
    public Integer submit(String participant, String gender, Integer dxId, Integer listVersion);

}
