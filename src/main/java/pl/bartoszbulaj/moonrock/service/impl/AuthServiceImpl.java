package pl.bartoszbulaj.moonrock.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.bartoszbulaj.moonrock.service.AuthService;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

	@Override
	public boolean authenticateUser() {
		// TODO Auto-generated method stub
		return false;
	}

}
