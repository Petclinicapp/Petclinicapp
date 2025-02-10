package bootcamp.petclinic.service;

import bootcamp.petclinic.dto.pet.PetRequestDTO;
import bootcamp.petclinic.dto.pet.PetResponseDTO;
import bootcamp.petclinic.exceptions.PetNotFoundException;
import bootcamp.petclinic.model.Pet;
import bootcamp.petclinic.model.User;
import bootcamp.petclinic.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final AuthService authService;


    public PetResponseDTO createPet(PetRequestDTO petRequestDTO) {
        User currentUser = authService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

        Pet pet = new Pet();
        pet.setPetId(UUID.randomUUID().toString());
        pet.setPetName(petRequestDTO.getPetName());
        pet.setSpecies(petRequestDTO.getSpecies());
        pet.setBreed(petRequestDTO.getBreed());
        pet.setGender(petRequestDTO.getGender());
        pet.setAge(petRequestDTO.getAge());
        pet.setWeight(petRequestDTO.getWeight());
        pet.setUserId(currentUser.getUserId());

        petRepository.save(pet);

        return new PetResponseDTO(pet.getPetId(),
                pet.getPetName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getGender(),
                pet.getAge(),
                pet.getWeight(),
                pet.getUserId());

    }

    public Optional<PetResponseDTO> getPetById(String petId) {
        return petRepository.findById(petId)
                .map(pet -> new PetResponseDTO(pet.getPetId(),
                        pet.getPetName(),
                        pet.getSpecies(),
                        pet.getBreed(),
                        pet.getGender(),
                        pet.getAge(),
                        pet.getWeight(),
                        pet.getUserId())

                );
    }

    public List<PetResponseDTO> getPetsByUserId(String userId) {
        return petRepository.findPetsByUserId(userId)
                .stream()
                .map(pet -> new PetResponseDTO(
                        pet.getPetId(),
                        pet.getPetName(),
                        pet.getSpecies(),
                        pet.getBreed(),
                        pet.getGender(),
                        pet.getAge(),
                        pet.getWeight(),
                        pet.getUserId()))
                .collect(Collectors.toList());
    }

    public void updatePet(String petId, PetRequestDTO petRequestDTO) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        if (petRequestDTO.getPetName() != null) {
            pet.setPetName(petRequestDTO.getPetName());
        }
        if (petRequestDTO.getSpecies() != null) {
            pet.setSpecies(petRequestDTO.getSpecies());
        }
        if (petRequestDTO.getBreed() != null) {
            pet.setBreed(petRequestDTO.getBreed());
        }
        if (petRequestDTO.getGender() != null) {
            pet.setGender(petRequestDTO.getGender());
        }
        if (petRequestDTO.getAge() > 0) {
            pet.setAge(petRequestDTO.getAge());
        }
        if (petRequestDTO.getWeight() > 0) {
            pet.setWeight(petRequestDTO.getWeight());
        }
        try {
            petRepository.save(pet);
        } catch (DynamoDbException e) {
            throw new RuntimeException("Failed to update pet", e);
        }
    }

    public void deletePet(String petId) {
        petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        petRepository.deleteById(petId);
    }
}
