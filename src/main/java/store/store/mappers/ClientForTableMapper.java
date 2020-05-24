package store.store.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.store.services.ClientServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientForTableMapper {
    ClientServiceImpl clientService;

    @Autowired
    public ClientForTableMapper(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    public List<ClientsForTable> getClientsForTable() {
        return clientService.getAllClients().stream()
                .map(client ->
                        new ClientsForTable(client.getFirstName(), client.getLastName(), client.getPhoneNumber(), client.getEmail(), client.getAddressId().getStreet(), client.getAddressId().getCity(), client.getAddressId().getZipCode()))
                .collect(Collectors.toList());
    }
}
