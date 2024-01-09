package com.cbh.userservice.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cbh.exceptionservice.exception.AlreadyExistException;
import com.cbh.exceptionservice.exception.NotFoundException;
import com.cbh.userservice.models.Address;
import com.cbh.userservice.models.User;
import com.cbh.userservice.repository.UserRepository;
import com.cbh.userservice.requestdto.AddUserDto;
import com.cbh.userservice.requestdto.DoKycDto;
import com.cbh.userservice.service.UserService;
import com.cbh.userservice.util.Constant;
import java.util.Objects;

import com.cbh.userservice.models.Kyc;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final ModelMapper mapper;

	private final UserRepository userRepository;

	public User addUser(AddUserDto addUserDto) {
		userRepository.findByEmail(addUserDto.getEmail()).ifPresent(existingUser -> {
			throw new AlreadyExistException("email", "Email already exist");
		});
		User user = mapper.map(addUserDto, User.class);
		Address address = mapper.map(addUserDto, Address.class);
		user.setKycStatus(Constant.KYCSTATUS_NOTINITIALISED);
		user.setAddress(address);
		userRepository.save(user);
		
		return user;
	}

	public User kyc(DoKycDto doKycDto) {
		User user = fetchUserByEmail(doKycDto.getEmail());
		if (!Objects.equals(user.getKycStatus(), Constant.KYCSTATUS_NOTINITIALISED)) {
			throw new AlreadyExistException("Kyc", "You have already applied");
		}
		Kyc kyc = mapper.map(doKycDto, Kyc.class);
		user.setKyc(kyc);
		user.setKycStatus(Constant.KYCSTATUS_APPROVED);
		userRepository.save(user);
		return user;
	}

	public String getKycStatus(String userId) {
		User user = fetchUserById(userId);
		return user.getKycStatus();
	}

	public User fetchUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("email", "User not found"));
	}

	public User fetchUserById(String userId) {
		return userRepository.findByUserId(userId)
				.orElseThrow(() -> new NotFoundException("User not found with userId: ", userId));
	}

}
