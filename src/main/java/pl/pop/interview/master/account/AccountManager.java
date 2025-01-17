package pl.pop.interview.master.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.pop.interview.master.practitioner.Practitioner;
import pl.pop.interview.master.practitioner.PractitionerFacade;

import java.util.List;

@Service
@RequiredArgsConstructor
class AccountManager implements AccountFacade {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final PractitionerFacade practitionerManager;

    @Override
    public AccountDTO createNewAccount(AccountDTO accountDTO) {
        if (accountRepository.existsById(accountDTO.getEmail())) {
            throw new AccountServiceException("An account with this email address already exists");
        }
        String hashedPassword = passwordEncoder.encode(accountDTO.getPassword());
        Account account = new Account();
        account.setEmail(accountDTO.getEmail());
        account.setPassword(hashedPassword);

        Practitioner practitioner = practitionerManager.createNewPractitioner();
        account.setPractitioner(practitioner);
        return AccountDTO.mapToDto(accountRepository.save(account));
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::mapToDto)
                .toList();
    }
}
