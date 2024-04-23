
package acme.features.authenticated.client.contracts;

import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.roles.Client;

@Service
public class ClientContractsShowService extends AbstractService<Client, Contract> {

}