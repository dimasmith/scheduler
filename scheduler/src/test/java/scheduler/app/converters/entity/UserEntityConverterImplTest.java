package scheduler.app.converters.entity;

import org.junit.Test;
import org.mockito.InjectMocks;
import scheduler.app.entities.UserEntity;
import scheduler.app.entities.UserSecureDetailsEntity;
import scheduler.app.models.User;
import scheduler.app.utils.TestData;
import scheduler.app.utils.TestDataEntities;
import scheduler.app.utils.TestDataModels;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserEntityConverterImplTest {

    @InjectMocks
    private UserEntityConverterImpl sut = new UserEntityConverterImpl();

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfEntityIsNullWhenPopulatesEntity() {
        sut.populateEntity(null, new User());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfUserIsNullWhenPopulatesEntity() {
        sut.populateEntity(new UserEntity(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfUserIsNullWhenConvertsToModel() {
        sut.toModel(null);
    }

    @Test
    public void shouldPopulateEntityIfUserDetailsIsNull() {
        final UserEntity userEntity = new UserEntity();
        sut.populateEntity(userEntity, TestDataModels.user());

        assertEquals(TestData.USER_ID, userEntity.getId());
        assertEquals(TestData.USER_NAME, userEntity.getUsername());
        assertNull(userEntity.getSecureDetails());
    }

    @Test
    public void shouldPopulateEntityIfUserDetailsIsNotNull() {
        final UserEntity userEntity = TestDataEntities.user();
        sut.populateEntity(userEntity, TestDataModels.user());

        assertEquals(TestData.USER_ID, userEntity.getId());
        assertEquals(TestData.USER_NAME, userEntity.getUsername());

        final UserSecureDetailsEntity secureDetails = userEntity.getSecureDetails();
        assertEquals(TestData.USER_SECURE_DETAILS_ID, secureDetails.getId());
        assertEquals(TestData.USER_LOGIN, secureDetails.getLogin());
        assertEquals(TestData.USER_PASSWORD, secureDetails.getPassword());
        assertEquals(TestData.USER_ROLE, secureDetails.getRole());
        assertEquals(userEntity, secureDetails.getUser());
    }

    @Test
    public void shouldConvertEntityToModel() {
        final User model = sut.toModel(TestDataEntities.user());

        assertEquals(TestData.USER_ID, model.getId());
        assertEquals(TestData.USER_NAME, model.getUsername());
    }
}