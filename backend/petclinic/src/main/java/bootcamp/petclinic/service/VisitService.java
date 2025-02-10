package bootcamp.petclinic.service;

import bootcamp.petclinic.dto.visit.VisitRequestDTO;
import bootcamp.petclinic.dto.visit.VisitResponseDTO;
import bootcamp.petclinic.enums.VisitStatus;
import bootcamp.petclinic.model.Visit;
import bootcamp.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VisitService {

    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public VisitResponseDTO createVisit(VisitRequestDTO visitRequestDTO) {
        Visit visit = new Visit();
        visit.setVisitId(UUID.randomUUID().toString());
        visit.setPetId(visitRequestDTO.getPetId());
        visit.setUserId(visitRequestDTO.getUserId());
        visit.setDoctorId(visitRequestDTO.getDoctorId());
        visit.setVisitDate(visitRequestDTO.getVisitDateTime().toLocalDate());
        visit.setVisitTime(visitRequestDTO.getVisitDateTime().toLocalTime());
        visit.setReason(visitRequestDTO.getReason());
        visit.setStatus(VisitStatus.PENDING);

        visitRepository.save(visit);

        return new VisitResponseDTO(
                visit.getVisitId(),
                visit.getPetId(),
                visit.getUserId(),
                visit.getDoctorId(),
                visit.getVisitDate(),
                visit.getVisitTime(),
                visit.getReason(),
                visit.getStatus(),
                null, null, null, null
        );
    }

    public Optional<VisitResponseDTO> getVisitById(String visitId) {
        return visitRepository.findById(visitId)
                .map(visit -> new VisitResponseDTO(
                        visit.getVisitId(),
                        visit.getPetId(),
                        visit.getUserId(),
                        visit.getDoctorId(),
                        visit.getVisitDate(),
                        visit.getVisitTime(),
                        visit.getReason(),
                        visit.getStatus(),
                        visit.getExaminationResults(),
                        visit.getPerformedTests(),
                        visit.getDiagnosis(),
                        visit.getPrescribedTreatment()
                ));
    }

    public Optional<VisitResponseDTO> updateVisit(String visitId, VisitResponseDTO visitUpdateDTO) {
        Optional<Visit> existingVisit = visitRepository.findById(visitId);

        if (existingVisit.isPresent()) {
            Visit visit = getVisit(visitUpdateDTO, existingVisit);

            visitRepository.save(visit);
            return Optional.of(new VisitResponseDTO(
                    visit.getVisitId(),
                    visit.getPetId(),
                    visit.getUserId(),
                    visit.getDoctorId(),
                    visit.getVisitDate(),
                    visit.getVisitTime(),
                    visit.getReason(),
                    visit.getStatus(),
                    visit.getExaminationResults(),
                    visit.getPerformedTests(),
                    visit.getDiagnosis(),
                    visit.getPrescribedTreatment()
            ));
        }
        return Optional.empty();
    }

    private static Visit getVisit(VisitResponseDTO visitUpdateDTO, Optional<Visit> existingVisitOpt) {
        Visit visit = existingVisitOpt.orElseThrow(() -> new IllegalArgumentException("Visit not found"));

        visit.setExaminationResults(visitUpdateDTO.getExaminationResults());
        visit.setPerformedTests(visitUpdateDTO.getPerformedTests());
        visit.setDiagnosis(visitUpdateDTO.getDiagnosis());
        visit.setPrescribedTreatment(visitUpdateDTO.getPrescribedTreatment());

        if (visitUpdateDTO.getStatus() != null) {
            visit.setStatus(visitUpdateDTO.getStatus());
        }

        return visit;
    }


}
