package bootcamp.petclinic.service;

import bootcamp.petclinic.dto.visit.VisitRequestDTO;
import bootcamp.petclinic.dto.visit.VisitResponseDTO;
import bootcamp.petclinic.exceptions.VisitNotFoundException;
import bootcamp.petclinic.model.Visit;
import bootcamp.petclinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final AuthService authService;


    public VisitResponseDTO createVisit(VisitRequestDTO visitRequestDTO) {
        String userId = authService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"))
                .getUserId();

        Visit visit = new Visit();
        visit.setVisitId(UUID.randomUUID().toString());
        visit.setPetId(visitRequestDTO.getPetId());
        visit.setUserId(userId);
        visit.setVisitDateTime(visitRequestDTO.getVisitDateTime());
        visit.setReason(visitRequestDTO.getReason());

        visitRepository.save(visit);

        return new VisitResponseDTO(visit.getVisitId(), visit.getPetId(), visit.getUserId(), visit.getVisitDateTime(), visit.getReason());
    }

    public Optional<VisitResponseDTO> getVisitById(String visitId) {
        return visitRepository.findById(visitId)
                .map(visit -> new VisitResponseDTO(visit.getVisitId(), visit.getPetId(), visit.getUserId(), visit.getVisitDateTime(), visit.getReason()));
    }

    public List<VisitResponseDTO> getVisitsByUserId(String userId) {
        return visitRepository.findByUserId(userId)
                .stream()
                .map(visit -> new VisitResponseDTO(
                        visit.getVisitId(),
                        visit.getPetId(),
                        visit.getUserId(),
                        visit.getVisitDateTime(),
                        visit.getReason()
                ))
                .collect(Collectors.toList());
    }

    public VisitResponseDTO updateVisit(String visitId, VisitRequestDTO visitRequestDTO) {
        Visit existingVisit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        String userId = authService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"))
                .getUserId();

        existingVisit.setUserId(userId);

        boolean isUpdated = false;

        if (visitRequestDTO.getVisitDateTime() != null && !visitRequestDTO.getVisitDateTime().equals(existingVisit.getVisitDateTime())) {
            existingVisit.setVisitDateTime(visitRequestDTO.getVisitDateTime());
            isUpdated = true;
        }

        if (visitRequestDTO.getReason() != null && !visitRequestDTO.getReason().equals(existingVisit.getReason())) {
            existingVisit.setReason(visitRequestDTO.getReason());
            isUpdated = true;
        }

        if (isUpdated) {
            visitRepository.save(existingVisit);
            return new VisitResponseDTO(
                    existingVisit.getVisitId(),
                    existingVisit.getPetId(),
                    existingVisit.getUserId(),
                    existingVisit.getVisitDateTime(),
                    existingVisit.getReason(),
                    "Visit updated successfully"
            );
        } else {
            return new VisitResponseDTO("No changes were detected");
        }
    }

    public void deleteVisit(String visitId) {
        visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        visitRepository.deleteVisitById(visitId);
    }
}