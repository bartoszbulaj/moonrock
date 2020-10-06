package pl.bartoszbulaj.moonrock.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.entity.UserEntity;
import pl.bartoszbulaj.moonrock.repository.UserRepository;
import pl.bartoszbulaj.moonrock.service.RegistrationService;

@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

	private UserRepository userRepository;

	@Autowired
	public RegistrationServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void registerUser(String username, String password) {
		userRepository.save(new UserEntity(username, "{noop}" + password, true, "ROLE_USER"));
	}

}
