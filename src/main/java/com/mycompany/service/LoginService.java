package com.mycompany.service;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.libx4j.rdb.jsql.mycompany;

import com.mycompany.AccountPrincipal;

import jjb.api;

@RolesAllowed("user")
@Produces(api.mimeType)
public class LoginService {
  @GET
  @Path("/login")
  public api.Account login(@Context final SecurityContext securityContext) {
    final mycompany.Account a = ((AccountPrincipal)securityContext.getUserPrincipal()).getAccount();
    if (a == null)
      throw new NotAuthorizedException("Incorrect username or password");

    final api.Account account = new api.Account();
    account.id.set(a.id.get());
    account.email.set(a.email.get());
    account.firstName.set(a.firstName.get());
    account.lastName.set(a.lastName.get());
    return account;
  }
}