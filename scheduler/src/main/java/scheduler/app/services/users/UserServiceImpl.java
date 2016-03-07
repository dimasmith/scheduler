package scheduler.app.services.users;

import org.springframework.stereotype.Service;
import scheduler.app.converters.entity.UserEntityConverter;
import scheduler.app.converters.entity.UserSecureDetailsConverter;
import scheduler.app.entities.UserEntity;
import scheduler.app.models.User;
import scheduler.app.models.UserSecureDetails;
import scheduler.app.repositories.UserRepository;

import javax.inject.Inject;

@Service
public class UserServiceImpl implements UserService {

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserEntityConverter userEntityConverter;

	@Inject
	private UserSecureDetailsConverter userSecureDetailsConverter;

	@Override
	public User findByLogin(final String login) {
		return userEntityConverter.toModel(userRepository.findByLogin(login));
	}

	@Override
	public User create(final User user, final UserSecureDetails userSecureDetails) {
		final UserEntity userEntity = new UserEntity();

		userEntityConverter.populateEntity(userEntity, user);
		userSecureDetailsConverter.createEntity(userEntity, userSecureDetails);

		final UserEntity saved = userRepository.saveAndFlush(userEntity);

		return userEntityConverter.toModel(saved);
	}

	@Override
	public User save(final User user) {
		final UserEntity userEntity = userRepository.findById(user.getId());
		userEntityConverter.populateEntity(userEntity, user);
		final UserEntity saved = userRepository.saveAndFlush(userEntity);
		return userEntityConverter.toModel(saved);
	}

	@Override
	public UserSecureDetails getUserSecureDetails(final Long userId) {
		return userSecureDetailsConverter.toModel(userRepository.findById(userId).getSecureDetails());
	}
}
