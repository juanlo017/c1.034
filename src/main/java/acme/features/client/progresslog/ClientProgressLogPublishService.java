
package acme.features.client.progresslog;

import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.contracts.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogPublishService extends AbstractService<Client, ProgressLog> {

}
