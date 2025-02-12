package bootcamp.petclinic.service;

import bootcamp.petclinic.dto.pet.PetRequestDTO;
import bootcamp.petclinic.dto.pet.PetResponseDTO;
import bootcamp.petclinic.exceptions.PetNotFoundException;
import bootcamp.petclinic.model.Pet;
import bootcamp.petclinic.model.User;
import bootcamp.petclinic.repository.PetRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private PetService petService;

    private PetRequestDTO createValidPetRequest() {
        return new PetRequestDTO("Buddy", "Dog", "Labrador", "Male", 3, 25.5, null);
    }

    @Test
    @DisplayName("createPet_success")
    void createPetSuccess() {

        PetRequestDTO petRequestDTO = createValidPetRequest();

        User user = User.builder()
                .userId("user123")
                .build();

        Optional<User> userOptional = Optional.of(user);

        doReturn(userOptional).when(authService).getCurrentUser();

        PetResponseDTO response = petService.createPet(petRequestDTO);

        assertNotNull(response);
        assertEquals(petRequestDTO.getPetName(), response.getPetName());
        verify(authService).getCurrentUser();
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    @DisplayName("createPet_ThrowsException_WhenUserNotAuthenticated")
    void createPetThrowsExceptionWhenUserNotAuthenticated() {

        PetRequestDTO requestDTO = createValidPetRequest();
        when(authService.getCurrentUser()).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> petService.createPet(requestDTO));
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    @DisplayName("getPetById_Success")
    void getPetByIdSuccess() {

        String petId = "pet123";

        Pet pet = Pet.builder()
                .petId(petId)
                .petName("Buddy")
                .species("Dog")
                .breed("Labrador")
                .gender("Male")
                .age(5)
                .weight(20.5)
                .userId("user123")
                .build();

        doReturn(Optional.of(pet)).when(petRepository).findById(petId);

        Optional<PetResponseDTO> response = petService.getPetById(petId);

        verify(petRepository).findById(petId);
        assertTrue(response.isPresent(), "Response should be present");
        assertEquals(petId, response.get().getPetId());
    }

    @Test
    @DisplayName("getPetById_ReturnsEmpty_WhenPetNotFound")
    void getPetByIdReturnsEmptyWhenPetNotFound() {

        String petId = "unknown";
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        Optional<PetResponseDTO> response = petService.getPetById(petId);

        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("getPetsByUserId_Success")
    void getPetsByUserIdSuccess() {
        String userId = "user123";

        Pet pet1 = Pet.builder()
                .petId("pet1")
                .petName("Joseph")
                .species("Dog")
                .breed("Labrador")
                .gender("Male")
                .age(5)
                .weight(20.5)
                .userId(userId)
                .build();

        Pet pet2 = Pet.builder()
                .petId("pet2")
                .petName("Max")
                .species("Cat")
                .breed("Persian")
                .gender("Female")
                .age(3)
                .weight(15.0)
                .userId(userId)
                .build();

        List<Pet> pets = List.of(pet1, pet2);

        doReturn(pets).when(petRepository).findPetsByUserId(eq(userId));

        List<PetResponseDTO> response = petService.getPetsByUserId(userId);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(2, response.size());
        assertEquals("Joseph", response.get(0).getPetName());
        assertEquals("Max", response.get(1).getPetName());

        verify(petRepository).findPetsByUserId(userId);
    }

    @Test
    @DisplayName("updatePet_Success")
    void updatePetSuccess() {

        String petId = "pet123";

        Pet existingPet = Pet.builder()
                .petId(petId)
                .petName("OldName")
                .species("Dog")
                .age(5)
                .weight(20.5)
                .userId("user123")
                .build();

        PetRequestDTO updateDTO = new PetRequestDTO("NewName", "Cat", "Persian", "Female", 4, 5.0, null);

        doReturn(Optional.of(existingPet)).when(petRepository).findById(petId);

        assertDoesNotThrow(() -> petService.updatePet(petId, updateDTO));

        verify(petRepository).findById(petId);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    @DisplayName("updatePet_ThrowsException_WhenPetNotFound")
    void updatePetThrowsExceptionWhenPetNotFound() {

        String petId = "notfound";
        PetRequestDTO petRequestDTO = createValidPetRequest();

        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.updatePet(petId, petRequestDTO));

        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    @DisplayName("deletePet_Success")
    void deletePetSuccess() {
        String petId = "pet123";
        Pet pet = Pet.builder()
                .petId(petId)
                .build();

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));

        assertDoesNotThrow(() -> petService.deletePet(petId));

        verify(petRepository).findById(petId);
        verify(petRepository).deleteById(petId);
    }

    @Test
    @DisplayName("deletePet_ThrowsException_WhenPetNotFound")
    void deletePetThrowsExceptionWhenPetNotFound() {

        String petId = "notfound";
        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.deletePet(petId));

        verify(petRepository, never()).deleteById(anyString());
    }
}