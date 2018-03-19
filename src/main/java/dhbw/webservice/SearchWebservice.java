/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhbw.webservice;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import dhbw.spotify.SpotifyRequest;



/**
 *
 * @author Foecking
 */

@Path("/search")
public class SearchWebservice {
    
  @GET
  @Produces( MediaType.APPLICATION_JSON )
  @Path("query/{query}/type/{type}")
  public String searchResultJson( @PathParam("query") String query,
                                  @PathParam("type") String type
  )
          
  {
      
      
    return null;
  }
    
}

