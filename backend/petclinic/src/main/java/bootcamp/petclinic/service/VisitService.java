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


    public VisitRequestDTO createVisit(VisitResponseDTO visitResponseDTO) {
        String userId = authService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"))
                .getUserId();

        Visit visit = new Visit();
        visit.setVisitId(UUID.randomUUID().toString());
        visit.setPetId(visitResponseDTO.getPetId());
        visit.setUserId(userId);
        visit.setVisitDateTime(visitResponseDTO.getVisitDateTime());
        visit.setReason(visitResponseDTO.getReason());

        visitRepository.save(visit);

        return new VisitRequestDTO(visit.getVisitId(), visit.getPetId(), visit.getUserId(), visit.getVisitDateTime(), visit.getReason());
    }

    public Optional<VisitRequestDTO> getVisitById(String visitId) {
        return visitRepository.findById(visitId)
                .map(visit -> new VisitRequestDTO(visit.getVisitId(), visit.getPetId(), visit.getUserId(), visit.getVisitDateTime(), visit.getReason()));
    }

    public List<VisitRequestDTO> getVisitsByUserId(String userId) {
        return visitRepository.findByUserId(userId)
                .stream()
                .map(visit -> new VisitRequestDTO(
                        visit.getVisitId(),
                        visit.getPetId(),
                        visit.getUserId(),
                        visit.getVisitDateTime(),
                        visit.getReason()
                ))
                .collect(Collectors.toList());
    }

    public Optional<VisitRequestDTO> updateVisit(String visitId, VisitRequestDTO visitRequestDTO) {
        Visit existingVisit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        String userId = authService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"))
                .getUserId();

        existingVisit.setUserId(userId);

        if (visitRequestDTO.getVisitDateTime() != null) {
            existingVisit.setVisitDateTime(visitRequestDTO.getVisitDateTime());
        }
        if (visitRequestDTO.getReason() != null) {
            existingVisit.setReason(visitRequestDTO.getReason());
        }

        visitRepository.save(existingVisit);

        return Optional.of(new VisitRequestDTO(
                existingVisit.getVisitId(),
                existingVisit.getPetId(),
                existingVisit.getUserId(),
                existingVisit.getVisitDateTime(),
                existingVisit.getReason()
        ));
    }

    public void deleteVisit(String visitId) {
        visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        visitRepository.deleteVisitById(visitId);
    }
}