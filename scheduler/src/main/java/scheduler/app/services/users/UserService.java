package scheduler.app.services.users;

import org.springframework.transaction.annotation.Transactional;
import scheduler.app.models.User;
import scheduler.app.models.UserSecureDetails;

public interface UserService {

	User findByLogin(String login);

	@Transactional
	User create(User user, UserSecureDetails userSecureDetails);

	@Transactional
	User modify(User user);

	UserSecureDetails loadUserSecureDetails(Long userId);
}
