// 📌 VisitService.java
package bootcamp.petclinic.service;

import bootcamp.petclinic.dto.visit.VisitRequestDTO;
import bootcamp.petclinic.dto.visit.VisitResponseDTO;
import bootcamp.petclinic.dto.visit.VisitDetailsUpdateDTO;
import bootcamp.petclinic.exceptions.VisitNotFoundException;
import bootcamp.petclinic.model.Visit;
import bootcamp.petclinic.model.VisitDetails;
import bootcamp.petclinic.repository.VisitRepository;
import bootcamp.petclinic.repository.VisitDetailsRepository;
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
    private final VisitDetailsRepository visitDetailsRepository;
    private final AuthService authService;

    public VisitRequestDTO createVisit(VisitRequestDTO visitRequestDTO) {
        String userId = authService.getCurrentUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"))
                .getUserId();

        Visit visit = new Visit();
        visit.setVisitId(UUID.randomUUID().toString());
        visit.setPetId(visitRequestDTO.getPetId());
        visit.setUserId(userId);
        visit.setVisitDateTime(visitRequestDTO.getVisitDateTime());
        visit.setReason(visitRequestDTO.getReason());

        VisitDetails visitDetails = new VisitDetails();
        visitDetails.setVisitDetailsId(UUID.randomUUID().toString());
        visitDetailsRepository.save(visitDetails);
        visit.setVisitDetailsId(visitDetails.getVisitDetailsId());

        visitRepository.save(visit);

        return new VisitRequestDTO(visit.getVisitId(), visit.getPetId(), visit.getUserId(), visit.getVisitDateTime(), visit.getReason());
    }

    public Optional<VisitResponseDTO> getVisitById(String visitId) {
        return visitRepository.findById(visitId)
                .map(visit -> {
                    VisitDetails visitDetails = visitDetailsRepository.findById(visit.getVisitDetailsId())
                            .orElse(null);
                    return new VisitResponseDTO(visit.getVisitId(), visit.getPetId(), visit.getUserId(), visit.getVisitDateTime(), visit.getReason(), visitDetails);
                });
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

    public Optional<VisitResponseDTO> updateVisitDetails(VisitDetailsUpdateDTO visitDetailsUpdateDTO) {
        Visit visit = visitRepository.findById(visitDetailsUpdateDTO.getVisitId())
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        VisitDetails visitDetails = visitDetailsRepository.findById(visit.getVisitDetailsId())
                .orElseThrow(() -> new VisitNotFoundException("Visit details not found"));

        visitDetails.setExaminationResults(visitDetailsUpdateDTO.getExaminationResults());
        visitDetails.setPerformedTests(visitDetailsUpdateDTO.getPerformedTests());
        visitDetails.setDiagnosis(visitDetailsUpdateDTO.getDiagnosis());
        visitDetails.setPrescribedTreatment(visitDetailsUpdateDTO.getPrescribedTreatment());

        visitDetailsRepository.save(visitDetails);

        return Optional.of(new VisitResponseDTO(
                visit.getVisitId(),
                visit.getPetId(),
                visit.getUserId(),
                visit.getVisitDateTime(),
                visit.getReason(),
                visitDetails
        ));
    }

    public void deleteVisit(String visitId) {
        visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found"));

        visitRepository.deleteVisitById(visitId);
    }
}
