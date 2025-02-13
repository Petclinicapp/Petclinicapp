package bootcamp.petclinic.service;

import bootcamp.petclinic.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private DynamoDbEnhancedClient enhancedClient;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DynamoDbTable<User> userTable;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        when(enhancedClient.table(eq("Users"), any(TableSchema.class))).thenReturn(userTable);
        userService = new UserService(enhancedClient);
    }

    @Test
    public void save_ShouldCallPutItem() {
        User user = new User();
        user.setUsername("testuser");

        userService.save(user);

        verify(userTable).putItem(user);
    }

    @Test
    public void findByUsername_WhenUserExists_ShouldReturnUser() {
        User user = new User();
        user.setUsername("john");
        List<User> users = Arrays.asList(user);

        PageIterable<User> pageIterable = mock(PageIterable.class);
        when(userTable.scan()).thenReturn(pageIterable);

        SdkIterable<User> sdkIterable = new SdkIterable<User>() {
            @Override
            public Iterator<User> iterator() {
                return users.iterator();
            }
        };
        when(pageIterable.items()).thenReturn(sdkIterable);

        Optional<User> result = userService.findByUsername("john");

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    public void findByUsername_WhenUserDoesNotExist_ShouldReturnEmpty() {
        PageIterable<User> pageIterable = mock(PageIterable.class);
        when(userTable.scan()).thenReturn(pageIterable);
        SdkIterable<User> emptySdkIterable = new SdkIterable<User>() {
            @Override
            public Iterator<User> iterator() {
                return Collections.emptyIterator();
            }
        };
        when(pageIterable.items()).thenReturn(emptySdkIterable);

        Optional<User> result = userService.findByUsername("nonexistent");

        assertFalse(result.isPresent());
    }

    @Test
    public void findByEmail_WhenUserExists_ShouldReturnUser() {
        User user = new User();
        user.setEmail("john@example.com");
        List<User> users = Arrays.asList(user);

        PageIterable<User> pageIterable = mock(PageIterable.class);
        when(userTable.scan()).thenReturn(pageIterable);
        SdkIterable<User> sdkIterable = new SdkIterable<User>() {
            @Override
            public Iterator<User> iterator() {
                return users.iterator();
            }
        };
        when(pageIterable.items()).thenReturn(sdkIterable);

        Optional<User> result = userService.findByEmail("john@example.com");

        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
    }

    @Test
    public void findByEmail_WhenUserDoesNotExist_ShouldReturnEmpty() {
        PageIterable<User> pageIterable = mock(PageIterable.class);
        when(userTable.scan()).thenReturn(pageIterable);
        SdkIterable<User> emptySdkIterable = new SdkIterable<User>() {
            @Override
            public Iterator<User> iterator() {
                return Collections.emptyIterator();
            }
        };
        when(pageIterable.items()).thenReturn(emptySdkIterable);

        Optional<User> result = userService.findByEmail("nonexistent@example.com");

        assertFalse(result.isPresent());
    }
}
