package store.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.store.entity.Client;
import store.store.repository.ClientRepository;

import java.util.List;

@Service
public class ClientServiceImpl {

    ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}
