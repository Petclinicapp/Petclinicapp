package bootcamp.petclinic.service;

import bootcamp.petclinic.dto.pet.PetRequestDTO;
import bootcamp.petclinic.dto.pet.PetResponseDTO;
import bootcamp.petclinic.model.Pet;
import bootcamp.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public PetResponseDTO createPet(PetRequestDTO petRequestDTO) {
        Pet pet = new Pet();
        pet.setPetId(UUID.randomUUID().toString());
        pet.setPetName(petRequestDTO.getPetName());
        pet.setSpecies(petRequestDTO.getSpecies());
        pet.setBreed(petRequestDTO.getBreed());
        pet.setGender(petRequestDTO.getGender());
        pet.setAge(petRequestDTO.getAge());
        pet.setWeight(petRequestDTO.getWeight());

        petRepository.save(pet);

        return new PetResponseDTO(pet.getPetId(),
                                pet.getPetName(),
                                pet.getSpecies(),
                                pet.getBreed(),
                                pet.getGender(),
                                pet.getAge(),
                                pet.getWeight());
    }

    public Optional<PetResponseDTO> getPetById(String petId) {
        return petRepository.findById(petId)
                .map(pet -> new PetResponseDTO(pet.getPetId(),
                                                    pet.getPetName(),
                                                    pet.getSpecies(),
                                                    pet.getBreed(),
                                                    pet.getGender(),
                                                    pet.getAge(),
                                                    pet.getWeight()));
    }

    public void updatePet(String petId, PetRequestDTO updatedPetDTO) {
        Optional<Pet> existingPet = petRepository.findById(petId);
        if (existingPet.isPresent()) {
            Pet pet = existingPet.get();
            pet.setPetName(updatedPetDTO.getPetName());
            pet.setSpecies(updatedPetDTO.getSpecies());
            pet.setBreed(updatedPetDTO.getBreed());
            pet.setGender(updatedPetDTO.getGender());
            pet.setAge(updatedPetDTO.getAge());
            pet.setWeight(updatedPetDTO.getWeight());
            petRepository.save(pet);
        } else {
            throw new IllegalArgumentException("Pet not found with ID: " + petId);
        }
    }

    public void deletePet(String petId) {
        petRepository.deleteById(petId);
    }
}
